package kpdfCompare;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;

import java.io.IOException;


public class GraphicsState {
    private PDColor strokingColor = null;
    private PDColor nonStrokingColor = null;
    private String unicode = "";
    private RenderingMode renderingMode = null;
    private String value;

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

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public RenderingMode getRenderingMode() {
        return renderingMode;
    }

    public void setRenderingMode(RenderingMode renderingMode) {
        this.renderingMode = renderingMode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                // if deriving: appendSuper(super.hashCode()).
                        append(getNonStrokingColor()).
                        append(getStrokingColor()).
                        append(getRenderingMode()).
                        toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GraphicsState))
            return false;
        if (obj == this)
            return true;

        GraphicsState rhs = (GraphicsState) obj;
        try {
            return new EqualsBuilder().
                    append(getRenderingMode().toString(), rhs.getRenderingMode().toString()).
                    append(getStrokingColor().toRGB(), rhs.getStrokingColor().toRGB()).
                    append(getNonStrokingColor().toRGB(), rhs.getNonStrokingColor().toRGB()).
                    isEquals();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}