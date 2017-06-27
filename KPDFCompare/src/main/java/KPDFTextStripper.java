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
    private List<KFilter> filters = new ArrayList<KFilter>();
    private Map<GraphicsState, String> graphicsStates = new HashMap<GraphicsState, String>();
    private PDDocument pdDoc;

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
            if ((null != filter.getFontSize()) && !(String.valueOf(getPdfTextInfos().get(i).getFontSize()).equals(filter.getFontSize()))) {
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

        writeString(String.format("[Font:%s]", textPositions.get(0).getFont()));
        pti.setFont(textPositions.get(0).getFont().toString());
        writeString(String.format("[FontSize:%s]", textPositions.get(0).getFontSize()));
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
        writeString(String.format("[width:%s]", width));
        pti.setWidth(width);
        writeString(String.format("[height:%s]", height));
        pti.setHeight(height);
        writeString(String.format("[startX:%s]", startX));
        pti.setStartX(startX);
        writeString(String.format("[startY:%s]", startY));
        pti.setStartY(startY);
        PDColor strokingColor = getGraphicsState().getStrokingColor();
        PDColor nonStrokingColor = getGraphicsState().getNonStrokingColor();
        RenderingMode renderingMode = getGraphicsState().getTextState().getRenderingMode();

        //writeString(String.format("[%s]", firstProsition.getTextMatrix()));
        pti.setValue(text);
        pti.setNonStrokingColor(nonStrokingColor);
        pti.setStrokingColor(strokingColor);
        pti.setRenderingMode(renderingMode);
        pdfTextInfos.add(pti);
        //    super.writeString(text, textPositions);
    }


    @Override
    protected void processTextPosition(TextPosition text) {
        super.processTextPosition(text);


        PDColor strokingColor = getGraphicsState().getStrokingColor();
        PDColor nonStrokingColor = getGraphicsState().getNonStrokingColor();
        String unicode = text.getUnicode();
        RenderingMode renderingMode = getGraphicsState().getTextState().getRenderingMode();
//            System.out.println("Unicode:            " + unicode);
//            System.out.println("Rendering mode:     " + renderingMode);
//            System.out.println("Stroking color:     " + strokingColor);
//            System.out.println("Non-Stroking color: " + nonStrokingColor);
//            System.out.println();

        GraphicsState graphicsState = new GraphicsState();
        try {
            graphicsState.setNonStrokingColor(String.valueOf(nonStrokingColor.toRGB()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        graphicsState.setRenderingMode(renderingMode.name());
        try {
            graphicsState.setStrokingColor(String.valueOf(strokingColor.toRGB()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //  graphicsState.setUnicode(unicode);

        if ((graphicsStates == null) || (graphicsStates.size() == 0) || !graphicsStates.containsKey(graphicsState)) {
            graphicsStates.put(graphicsState, text.toString());
        } else {
            //check if exists


            String s = graphicsStates.get(graphicsState);
            graphicsStates.put(graphicsState, s + text.toString());

        }
        // See the PrintTextLocations for more attributes
    }


}