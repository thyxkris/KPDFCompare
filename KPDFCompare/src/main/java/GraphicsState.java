
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


public class GraphicsState {
    private String strokingColor = "";
    private String nonStrokingColor = "";
    private String unicode = "";
    private String renderingMode = "";
    private String value;

    public String getStrokingColor() {
        return strokingColor;
    }

    public void setStrokingColor(String strokingColor) {
        this.strokingColor = strokingColor;
    }

    public String getNonStrokingColor() {
        return nonStrokingColor;
    }

    public void setNonStrokingColor(String nonStrokingColor) {
        this.nonStrokingColor = nonStrokingColor;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public String getRenderingMode() {
        return renderingMode;
    }

    public void setRenderingMode(String renderingMode) {
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
                        append(strokingColor).
                        append(nonStrokingColor).
                        append(renderingMode).
                        toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GraphicsState))
            return false;
        if (obj == this)
            return true;

        GraphicsState rhs = (GraphicsState) obj;
        return new EqualsBuilder().
                // if deriving: appendSuper(super.equals(obj)).
                        append(strokingColor, rhs.strokingColor).
                        append(nonStrokingColor, rhs.nonStrokingColor).
                        append(renderingMode, rhs.renderingMode).
                        isEquals();
    }
}