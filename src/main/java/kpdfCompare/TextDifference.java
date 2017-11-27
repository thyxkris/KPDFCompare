package kpdfCompare;


public class TextDifference {
    public KPDFTextInfo expected;
    public KPDFTextInfo actual;
    public String reason;

    public TextDifference() {
        expected = new KPDFTextInfo();
        actual = new KPDFTextInfo();
        reason = null;
    }

}
