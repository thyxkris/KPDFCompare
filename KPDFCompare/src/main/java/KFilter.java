import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import java.util.HashMap;

public class KFilter {
    String value = null;
    String font = null;
    String fontSize = null;
    String startX = null;
    String startY = null;
    String width = null;
    String height = null;
    String offset = null;
    String index = null;
    String replacement = null;
    PDColor strokingColor = null;
    PDColor nonStrokingColor = null;
    RenderingMode renderingMode = null;
    public HashMap<Integer, GraphicsState> getGraphicsStateHashMap() {
        return graphicsStateHashMap;
    }

    public void setGraphicsStateHashMap(HashMap<Integer, GraphicsState> graphicsStateHashMap ) {
        this.graphicsStateHashMap= graphicsStateHashMap;
    }
    public void setGraphicsStateHashMap(Integer index, GraphicsState graphicsState) {
        this.graphicsStateHashMap.put(index, graphicsState);
    }

    private HashMap<Integer,GraphicsState> graphicsStateHashMap = new HashMap<Integer, GraphicsState>();

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

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = String.valueOf(fontSize);
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getStartX() {
        return startX;
    }

    public void setStartX(String startX) {
        this.startX = startX;
    }

    public String getStartY() {
        return startY;
    }

    public void setStartY(String startY) {
        this.startY = startY;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

}