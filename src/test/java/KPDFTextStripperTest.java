import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.pdmodel.graphics.color.PDCalRGB;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.util.Matrix;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD;
import static org.junit.Assert.assertEquals;

/**
 * Created by makri on 2/07/2017.
 */

public class KPDFTextStripperTest {

    private static List<KPDFTextInfo> kpdfTextInfos = new ArrayList<KPDFTextInfo>();
    KPDFTextStripper kpdfTextStripper = new KPDFTextStripper();
    ;

    public KPDFTextStripperTest() throws IOException {
    }

    @Before
    public void init() {

        KPDFTextInfo kpdfTextInfo = new KPDFTextInfo();
        kpdfTextInfo.setValue("TestValue");
        kpdfTextInfo.setFont("TestFont");
        kpdfTextInfo.setStartX("800");
        kpdfTextInfo.setStartY("100");
        kpdfTextInfo.setFontSize("15");
        kpdfTextInfo.setHeight("10");
        kpdfTextInfo.setWidth("8");
        COSArray cosArray = new COSArray();
        PDColorSpace pdColorSpace = new PDCalRGB();
        PDColor nonStroking = new PDColor(cosArray, pdColorSpace);
        kpdfTextInfo.setNonStrokingColor(nonStroking);
        PDColor StrokingColor = new PDColor(cosArray, pdColorSpace);
        kpdfTextInfo.setStrokingColor(StrokingColor);
        RenderingMode renderingMode = RenderingMode.FILL;
        kpdfTextInfo.setRenderingMode(renderingMode);
        this.kpdfTextInfos.add(kpdfTextInfo);

        KPDFTextInfo kpdfTextInfo1 = new KPDFTextInfo();
        kpdfTextInfo1.setValue("TestValue");
        kpdfTextInfo1.setFont("TestFont1");
        kpdfTextInfo1.setStartX("801");
        kpdfTextInfo1.setStartY("101");
        kpdfTextInfo1.setFontSize("16");
        kpdfTextInfo1.setHeight("11");
        kpdfTextInfo1.setWidth("9");


        PDColor nonStroking1 = new PDColor(cosArray, pdColorSpace);
        kpdfTextInfo1.setNonStrokingColor(nonStroking1);
        PDColor StrokingColor1 = new PDColor(cosArray, pdColorSpace);
        kpdfTextInfo1.setStrokingColor(StrokingColor1);
        RenderingMode renderingMode1 = RenderingMode.STROKE;
        kpdfTextInfo1.setRenderingMode(renderingMode1);
        this.kpdfTextInfos.add(kpdfTextInfo1);

        KPDFTextInfo kpdfTextInfo2 = new KPDFTextInfo();
        kpdfTextInfo2.setValue("TestValue");
        kpdfTextInfo2.setFont("TestFont2");
        kpdfTextInfo2.setStartX("801");
        kpdfTextInfo2.setStartY("101");
        kpdfTextInfo2.setFontSize("17");
        kpdfTextInfo2.setHeight("11");
        kpdfTextInfo2.setWidth("9");
        kpdfTextInfo2.setNonStrokingColor(nonStroking1);

        kpdfTextInfo2.setStrokingColor(StrokingColor1);

        kpdfTextInfo2.setRenderingMode(renderingMode1);

        this.kpdfTextInfos.add(kpdfTextInfo2);

        kpdfTextStripper.setPdfTextInfos(kpdfTextInfos);

    }

    @Test
    public void findTextLinesByFilter() throws Exception {

        KPDFTextInfo KPDFTextInfo = new KPDFTextInfo();
        KPDFTextInfo.setValue("TestValue1");
        assertEquals(-1, kpdfTextStripper.findTextLinesByFilter(KPDFTextInfo));
        KPDFTextInfo.setValue("TestValue");
//        assertEquals(-1, kpdfTextStripper.findTextLinesByFilter(KPDFTextInfo));
//        KPDFTextInfo.setValue("TestValue");
//        KPDFTextInfo.setFont("TestFont2");
//        assertEquals(2, kpdfTextStripper.findTextLinesByFilter(KPDFTextInfo));
//        KPDFTextInfo.setValue("TestValue");
//        KPDFTextInfo.setFont(null);
//        assertEquals(0, kpdfTextStripper.findTextLinesByFilter(KPDFTextInfo));
//        KPDFTextInfo.setValue("TestValue");
//        KPDFTextInfo.setFontSize("17");
//        assertEquals(2, kpdfTextStripper.findTextLinesByFilter(KPDFTextInfo));
//
//        KPDFTextInfo.setValue("TestValue");
//        KPDFTextInfo.setFontSize("17");
//        assertEquals(2, kpdfTextStripper.findTextLinesByFilter(KPDFTextInfo));
    }


    @Test
    public void startPage() throws Exception {

    }

    @Test
    public void writeLineSeparator() throws Exception {

    }

    @Test
    public void writeString() throws Exception {
//        TextPosition textPosition = new TextPosition(600, 600, 600, new Matrix(1,1,1,1,1,1), 600, 600, 600, 600, 600, "U", new int[2], HELVETICA_BOLD,
//                600, 600);
//        TextPosition textPosition1 = new TextPosition(600, 600, 600, new Matrix(1,1,1,1,1,1), 600, 600, 600, 600, 600, "R", new int[2], HELVETICA_BOLD,600, 600);
//        List<TextPosition> textPositions = new ArrayList<TextPosition>();
//        textPositions.add(textPosition);
//        textPositions.add(textPosition1);
//
//        kpdfTextStripper.writeString("test", textPositions);
//
//        assertEquals(kpdfTextStripper.getPdfTextInfos().size(), 5);
    }

    @Test
    public void processTextPosition() throws Exception {

    }

}