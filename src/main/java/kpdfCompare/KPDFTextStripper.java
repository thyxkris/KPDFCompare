package kpdfCompare;

import org.apache.pdfbox.contentstream.operator.color.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KPDFTextStripper extends PDFTextStripper {
    private List<KPDFTextInfo> pdfTextInfos = new ArrayList<KPDFTextInfo>();
    private List<GraphicsState> graphicsStateList = new ArrayList<GraphicsState>();
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

    public int findTextLinesByFilter(KPDFTextInfo filter) {

        for (int i = 0; i < getPdfTextInfos().size(); i++) {
            boolean found = true;
            if ((null != filter.getFont()) && !getPdfTextInfos().get(i).getFont().equals(filter.getFont())) {
                found = false;
            }
            if (!(getPdfTextInfos().get(i).getFontSize() == filter.getFontSize())) {
                found = false;
            }
            if ((null != filter.getValue()) && !getPdfTextInfos().get(i).getValue().equals(filter.getValue())) {
                found = false;
            }
            if (!(String.valueOf(getPdfTextInfos().get(i).getStartX()).equals(filter.getStartX()))) {
                found = false;
            }
            if (!(String.valueOf(getPdfTextInfos().get(i).getStartY()).equals(filter.getStartY()))) {
                found = false;
            }

            if (found) {
                return i;
            }
        }
        return -1;
    }

    public List<KPDFTextInfo> getPdfTextInfos() {
        return pdfTextInfos.subList(0, pdfTextInfos.size());
    }

    public void setPdfTextInfos(List<KPDFTextInfo> pdfTextInfos) {
        this.pdfTextInfos = pdfTextInfos;
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

        pti.setPageNumber(this.getCurrentPageNo());


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
        RenderingMode renderingMode = getGraphicsState().getTextState().getRenderingMode();

        GraphicsState graphicsState = new GraphicsState();
        graphicsState.setNonStrokingColor(nonStrokingColor);
        graphicsState.setRenderingMode(renderingMode);
        graphicsState.setStrokingColor(strokingColor);
        graphicsState.setValue(text.toString());
        //debug
        graphicsState.setValue(text.getUnicode());
        graphicsStateList.add(graphicsState);


    }

    public List<GraphicsState> getGraphicsStateList() {
        return graphicsStateList;
    }
}