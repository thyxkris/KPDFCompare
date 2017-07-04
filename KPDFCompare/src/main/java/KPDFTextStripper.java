import org.apache.pdfbox.contentstream.operator.color.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KPDFTextStripper extends PDFTextStripper {
    private List<KPDFTextInfo> pdfTextInfos = new ArrayList<KPDFTextInfo>();
    private List<KPDFTextInfo> pdfTextInfosColor = new ArrayList<KPDFTextInfo>();
    private List<KPDFTextInfo> pdfTextInfosColor2 = new ArrayList<KPDFTextInfo>();
    private List<KFilter> filters = new ArrayList<KFilter>();
    private Map<GraphicsState, String> graphicsStates = new HashMap<GraphicsState, String>();
    List<GraphicsState> graphicsStateList = new ArrayList<GraphicsState>();
    private PDDocument pdDoc;
    private int cursor = 0;

    public KPDFTextStripper() throws IOException {
        addOperator(new SetStrokingColorSpace());
        addOperator(new SetNonStrokingColorSpace());
        addOperator(new SetStrokingDeviceCMYKColor());
        addOperator(new SetNonStrokingDeviceCMYKColor());
        addOperator(new SetNonStrokingDeviceRGBColor());
        addOperator(new SetStrokingDeviceRGBColor());
        addOperator(new SetNonStrokingDeviceGrayColor());
        addOperator(new SetStrokingDeviceGrayColor());
        addOperator(new SetStrokingColor());
        addOperator(new SetStrokingColorN());
        addOperator(new SetNonStrokingColor());
        addOperator(new SetNonStrokingColorN());
    }

    protected int findTextLinesByFilter(KFilter filter) {

        for (int i = 0; i < getPdfTextInfos().size(); i++) {
            boolean found = true;
            if ((null != filter.getFont()) && !getPdfTextInfos().get(i).getFont().equals(filter.getFont())) {
                found = false;
            }
            if ((null != filter.getFontSize()) && !(getPdfTextInfos().get(i).getFontSize() == Float.valueOf(filter.getFontSize()))) {
                found = false;
            }
            if ((null != filter.getValue()) && !getPdfTextInfos().get(i).getValue().equals(filter.getValue())) {
                found = false;
            }
            if ((null != filter.getStartX()) && !(String.valueOf(getPdfTextInfos().get(i).getStartX()).equals(filter.getStartX()))) {
                found = false;
            }
            if ((null != filter.getStartY()) && !(String.valueOf(getPdfTextInfos().get(i).getStartY()).equals(filter.getStartY()))) {
                found = false;
            }

            if (found) {
                return i;
            }
        }
        return -1;
    }

    public Map<GraphicsState, String> getGraphicsStates() {
        return graphicsStates;
    }

    public void setGraphicsStates(Map<GraphicsState, String> graphicsStates) {
        this.graphicsStates = graphicsStates;
    }

    public List<KPDFTextInfo> getPdfTextInfos() {
        return pdfTextInfos.subList(0, pdfTextInfos.size());
    }

    public void setPdfTextInfos(List<KPDFTextInfo> pdfTextInfos) {
        this.pdfTextInfos = pdfTextInfos;
    }

    public List<KFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<KFilter> filters) {
        this.filters = filters;
    }

    public PDDocument getPdDoc() {
        return pdDoc;
    }

    public void setPdDoc(PDDocument pdDoc) {
        this.pdDoc = pdDoc;
    }

    @Override
    protected void startPage(PDPage page) throws IOException {
        //   startOfLine = true;
        super.startPage(page);
    }

    @Override
    protected void writeLineSeparator() throws IOException {
        //  startOfLine = true;
        super.writeLineSeparator();
    }


    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {

        KPDFTextInfo pti = new KPDFTextInfo();
        float height = textPositions.get(0).getFontSizeInPt();
        float width = 0;
        float startX = textPositions.get(0).getTextMatrix().getTranslateX();
        pti.setFont(textPositions.get(0).getFont().toString());
        pti.setFontSize(textPositions.get(0).getFontSize());
        float startY = textPositions.get(0).getTextMatrix().getTranslateY();
        for (TextPosition textPosition : textPositions) {
            if (startX > textPosition.getTextMatrix().getTranslateX()) {
                startX = textPosition.getTextMatrix().getTranslateX();
            }
            if (startY > textPosition.getTextMatrix().getTranslateY()) {
                startY = textPosition.getTextMatrix().getTranslateY();
            }
            if (height < textPosition.getFontSizeInPt()) {
                height = textPosition.getFontSizeInPt();
            }
            width = width + textPosition.getWidth();
        }
        pti.setWidth(width);
        pti.setHeight(height);
        pti.setStartX(startX);
        pti.setStartY(startY);
        pti.setValue(text);


        GraphicsState currentGS = new GraphicsState();
        GraphicsState lastGS = null;
        for (int i = 0; i < textPositions.size(); i++) {
            cursor++;

            currentGS = this.getGraphicsStateList().get(cursor - 1);
            if ((lastGS == null) || (currentGS == null) || (!lastGS.equals(currentGS))) {
                pti.setGraphicsStateHashMap(i, currentGS);
                if (i > 0) {
                    System.out.println(text);
                    System.out.println(i);
                }

            }
            lastGS = currentGS;
        }

        pdfTextInfos.add(pti);

    }


    @Override
    protected void processTextPosition(TextPosition text) {
        super.processTextPosition(text);


        PDColor strokingColor = getGraphicsState().getStrokingColor();
        PDColor nonStrokingColor = getGraphicsState().getNonStrokingColor();
        String unicode = text.getUnicode();
        RenderingMode renderingMode = getGraphicsState().getTextState().getRenderingMode();
/*        System.out.println("Unicode:            " + unicode);
        System.out.println("Rendering mode:     " + renderingMode);
        System.out.println("Stroking color:     " + strokingColor);
        System.out.println("Non-Stroking color: " + nonStrokingColor);
        System.out.println();*/

        GraphicsState graphicsState = new GraphicsState();

        graphicsState.setNonStrokingColor(nonStrokingColor);

        graphicsState.setRenderingMode(renderingMode);

        graphicsState.setStrokingColor(strokingColor);
        graphicsState.setValue(text.toString());
        //  graphicsState.setUnicode(unicode);

        GraphicsState lastGraphicsState = new GraphicsState();
        if (graphicsStateList == null || graphicsStateList.size() == 0) {
            lastGraphicsState = null;
        } else {
            lastGraphicsState = this.graphicsStateList.get(graphicsStateList.size() - 1);
        }
        GraphicsState currentGraphicsState = graphicsState;

        //debug
        graphicsState.setValue(text.getUnicode());
        graphicsStateList.add(graphicsState);

      /*  if (!currentGraphicsState.equals(lastGraphicsState)) {
            //it means the graphic state now has changed, put a new graphicState in the list
            this.graphicsStateList.add(currentGraphicsState);

        } else {
            // just add the new charactor
            String s = graphicsStates.get(graphicsState);
            //graphicsStates.put(graphicsState, s + text.toString());
            this.graphicsStateList.get(graphicsStateList.size() - 1).setValue(s + text.toString());
            //KPDFTextInfo kpdfTextInfo = new KPDFTextInfo();
            //kpdfTextInfo.setValue(s);
            //kpdfTextInfo.setRenderingMode();
        }*/

//        if ((graphicsStates == null) || (graphicsStates.size() == 0) || !graphicsStates.containsKey(graphicsState)) {
//            graphicsStates.put(graphicsState, text.toString());
//        } else {
//            //check if exists
//
//
//            String s = graphicsStates.get(graphicsState);
//            graphicsStates.put(graphicsState, s + text.toString());
//
//        }
        // See the PrintTextLocations for more attributes
    }


    public List<KPDFTextInfo> getPdfTextInfosColor() {
        return pdfTextInfosColor;
    }

    public List<GraphicsState> getGraphicsStateList() {
        return graphicsStateList;
    }
}