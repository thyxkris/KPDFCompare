import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import java.util.HashMap;

public class KPDFTextInfo {
    String value = null;
    String font = null;
    float fontSize = -1;
    float startX = -1;
    float startY = -1;
    float width = -1;
    float height = -1;
    int offset = -1;
    int index = -1;
    int pageNumber = -1;
    String replacement = null;
    PDColor strokingColor = null;
    PDColor nonStrokingColor = null;
    RenderingMode renderingMode = null;

    private HashMap<Integer, GraphicsState> graphicsStateHashMap = new HashMap<Integer, GraphicsState>();

    public HashMap<Integer, GraphicsState> getGraphicsStateHashMap() {
        return graphicsStateHashMap;
    }

    public void setGraphicsStateHashMap(HashMap<Integer, GraphicsState> graphicsStateHashMap) {
        this.graphicsStateHashMap = graphicsStateHashMap;
    }

    public void setGraphicsStateHashMap(Integer index, GraphicsState graphicsState) {
        this.graphicsStateHashMap.put(index, graphicsState);
    }

    public PDColor getStrokingColor() {
        return strokingColor;
    }

    public void setStrokingColor(PDColor strokingColor) {
        this.strokingColor = strokingColor;
    }

    public PDColor getNonStrokingColor() {
        return nonStrokingColor;
    }

    public void setNonStrokingColor(PDColor nonStrokingColor) {
        this.nonStrokingColor = nonStrokingColor;
    }

    public RenderingMode getRenderingMode() {
        return renderingMode;
    }

    public void setRenderingMode(RenderingMode renderingMode) {
        this.renderingMode = renderingMode;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = Float.valueOf(fontSize);
    }

    public void setFontSize(Float fontSize) {
        this.fontSize = fontSize;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(Float startX) {
        this.startX = startX;
    }

    public float getCoordinateOrWitdhOrHeight(String coordinate)
    {
        if(coordinate.toLowerCase().contains("x"))return  getStartX();
        if(coordinate.toLowerCase().contains("y")) return getStartY();
        if(coordinate.toLowerCase().contains("width")) return getWidth();
        if(coordinate.toLowerCase().contains("height")) return getHeight();
        throw  new IllegalArgumentException("coordinate can be only X , Y, WIDTH, or HEIGHT");
    }

    public void setStartX(String startX) {
        this.startX = Float.valueOf(startX);
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(Float startY) {
        this.startY = startY;
    }

    public void setStartY(String startY) {
        this.startY = Float.valueOf(startY);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public void setWidth(String width) {
        this.width = Float.valueOf(width);
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height =  height;
    }

    public void setHeight(String height) {
        this.height = Float.valueOf(height);
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public void setOffset(String offset) {
        this.offset = Integer.valueOf(offset);
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setIndex(String index) {
        this.index = Integer.valueOf(index);
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = Integer.valueOf(pageNumber);
    }
}