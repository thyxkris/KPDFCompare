
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


/**
 * Created by makri on 15/06/2017.
 */
public class KPDFCompare {


    List<KFilter> linesDifferentInAcutalPDF = new ArrayList<KFilter>();
    List<KFilter> linesDifferentInExpectedPDF = new ArrayList<KFilter>();

    String workingDirectory = ConfigHelper.getTestResourcesFolderPath();
    private Logger logger = LogManager.getLogger();
    private HashMap<String, Integer> comparisonResult = new HashMap<String, Integer>();

    public KPDFCompare() {
    }

    public boolean compareTwoImages(File fileOne, File fileTwo) {
        Boolean isTrue = true;
        try {


            Image imgOne = ImageIO.read(fileOne);
            Image imgTwo = ImageIO.read(fileTwo);
            BufferedImage bufImgOne = ImageIO.read(fileOne);
            BufferedImage bufImgTwo = ImageIO.read(fileTwo);

            int imgOneHt = bufImgOne.getHeight();
            int imgTwoHt = bufImgTwo.getHeight();
            int imgOneWt = bufImgOne.getWidth();
            int imgTwoWt = bufImgTwo.getWidth();
            if (imgOneHt != imgTwoHt || (imgOneWt != imgTwoWt)) {
                System.out.println(" size are not equal ");
                isTrue = false;
            }
            for (int x = 0; x < imgOneWt; x++) {
                for (int y = 0; y < imgOneHt; y++) {
                    if (bufImgOne.getRGB(x, y) != bufImgTwo.getRGB(x, y)) {
                        System.out.println(" size are not equal ");
                        isTrue = false;
                        bufImgOne.setRGB(x, y, 100);
                        // break;
                    }
                }
            }
            File outputfile = new File(fileOne.getPath() + "result.png");
            ImageIO.write(bufImgOne, "png", outputfile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isTrue;
    }

    public HashMap<String, Integer> comparePdfByTextContent(PDDocument pdDocExpected, PDDocument pdDocActual, List<KFilter> filters) throws IOException {

        KPDFTextStripper actual = new KPDFTextStripper();
        actual.getText(pdDocActual);
        KPDFTextStripper expected = new KPDFTextStripper();
        expected.getText(pdDocExpected);

        return comparePdfByTextContent(expected, actual, filters, true, 0.03f, 0, 0, 0);
    }

    public HashMap<String, Integer> comparePdfByTextContent(KPDFTextStripper expected, KPDFTextStripper actual, List<KFilter> filters) throws IOException {


        return comparePdfByTextContent(expected, actual, filters, true, 0.03f, 0, 0.01f, 0);
    }

    public HashMap<String, Integer> comparePdfByTextContent(KPDFTextStripper expected, KPDFTextStripper actual, List<KFilter> filters, boolean isTolerant, float deviationStartX, float deviationStartY, float deviationWidth, float deviationHeight) throws IOException {


        int differenceCount = 0;
        int countHeightNotSame = 0;
        int countWidthNotSame = 0;
        int countStartXNotSame = 0;
        int countStartYNotSame = 0;
        int countFontNotSame = 0;
        int countFontSizeNotSame = 0;
        int countValueNotSame = 0;
        int countStrokingColorNotSame = 0;
        int countNonStrokingColorNotSame = 0;
        int countRenderingModeNotSame = 0;

        if (Math.abs(expected.getPdfTextInfos().size() - actual.getPdfTextInfos().size()) > 0) {
            comparisonResult.put("the defference in conent Lines is ", Math.abs(expected.getPdfTextInfos().size() - actual.getPdfTextInfos().size()));
            logger.info("comparison stops since the difference is too apparent");
            return comparisonResult;
        }

        //if it's passed above, continue to compare other parts
        for (int i = 0; i < expected.getPdfTextInfos().size(); i++) {
            boolean isSame = true;


            if (expected.getPdfTextInfos().get(i).getHeight() != actual.getPdfTextInfos().get(i).getHeight()) {
                isSame = false;
                countHeightNotSame++;// = false;
            }
            if (expected.getPdfTextInfos().get(i).getWidth() != actual.getPdfTextInfos().get(i).getWidth()) {
                isSame = false;
                countWidthNotSame++;//
            }
            if (expected.getPdfTextInfos().get(i).getStartX() != actual.getPdfTextInfos().get(i).getStartX()) {
                isSame = false;
                countStartXNotSame++;// = false;
            }
            if (expected.getPdfTextInfos().get(i).getStartY() != actual.getPdfTextInfos().get(i).getStartY()) {
                isSame = false;
                countStartYNotSame++;// = false;
            }
            if (!expected.getPdfTextInfos().get(i).getFont().equals(actual.getPdfTextInfos().get(i).getFont())) {
                isSame = false;
                countFontNotSame++;// = false;
            }
            if (expected.getPdfTextInfos().get(i).getFontSize() != actual.getPdfTextInfos().get(i).getFontSize()) {
                isSame = false;
                countFontSizeNotSame++;// = false;
            }
            if (!expected.getPdfTextInfos().get(i).getValue().equals(actual.getPdfTextInfos().get(i).getValue())) {
                isSame = false;
                countValueNotSame++;// = false;
            }
            if (!expected.getPdfTextInfos().get(i).getStrokingColor().equals(actual.getPdfTextInfos().get(i).getStrokingColor())) {
                isSame = false;
                countStrokingColorNotSame++;// = false;
            }
            if (!expected.getPdfTextInfos().get(i).getNonStrokingColor().equals(actual.getPdfTextInfos().get(i).getNonStrokingColor())) {
                isSame = false;
                countNonStrokingColorNotSame++;// = false;
            }
            if (!expected.getPdfTextInfos().get(i).getRenderingMode().equals(actual.getPdfTextInfos().get(i).getRenderingMode())) {
                isSame = false;
                countRenderingModeNotSame++;// = false;
            }
            if (!isSame) {
                differenceCount++;
                KFilter lineDifferentInActualPDF = new KFilter();
                KFilter lineDifferentInExpectedPDF = new KFilter();
                //output the miss match
                logger.info("line " + Integer.toString(i) + " :");
                lineDifferentInExpectedPDF.setIndex(String.valueOf(i));
                lineDifferentInActualPDF.setIndex(String.valueOf(i));

                lineDifferentInExpectedPDF.setValue(expected.getPdfTextInfos().get(i).getValue());
                lineDifferentInActualPDF.setValue(actual.getPdfTextInfos().get(i).getValue());
                lineDifferentInExpectedPDF.setStartX(String.valueOf(expected.getPdfTextInfos().get(i).getStartX()));
                lineDifferentInActualPDF.setStartX(String.valueOf(actual.getPdfTextInfos().get(i).getStartX()));
                lineDifferentInExpectedPDF.setStartY(String.valueOf(expected.getPdfTextInfos().get(i).getStartY()));
                lineDifferentInActualPDF.setStartY(String.valueOf(actual.getPdfTextInfos().get(i).getStartY()));
                lineDifferentInExpectedPDF.setWidth(String.valueOf(expected.getPdfTextInfos().get(i).getWidth()));
                lineDifferentInActualPDF.setWidth(String.valueOf(actual.getPdfTextInfos().get(i).getWidth()));
                lineDifferentInExpectedPDF.setHeight(String.valueOf(expected.getPdfTextInfos().get(i).getHeight()));
                lineDifferentInActualPDF.setHeight(String.valueOf(actual.getPdfTextInfos().get(i).getHeight()));
                lineDifferentInExpectedPDF.setFont(expected.getPdfTextInfos().get(i).getFont());
                lineDifferentInActualPDF.setFont(actual.getPdfTextInfos().get(i).getFont());
                lineDifferentInExpectedPDF.setFontSize(String.valueOf(expected.getPdfTextInfos().get(i).getFontSize()));
                lineDifferentInActualPDF.setFontSize(String.valueOf(actual.getPdfTextInfos().get(i).getFontSize()));

                logger.info("column: expected | actual");
                logger.info("value: " + expected.getPdfTextInfos().get(i).getValue() + " | " + actual.getPdfTextInfos().get(i).getValue());
                logger.info("x: " + expected.getPdfTextInfos().get(i).getStartX() + " | " + actual.getPdfTextInfos().get(i).getStartX());
                logger.info("y: " + expected.getPdfTextInfos().get(i).getStartY() + " | " + actual.getPdfTextInfos().get(i).getStartY());
                logger.info("width: " + expected.getPdfTextInfos().get(i).getWidth() + " | " + actual.getPdfTextInfos().get(i).getWidth());
                logger.info("height: " + expected.getPdfTextInfos().get(i).getHeight() + " | " + actual.getPdfTextInfos().get(i).getHeight());
                logger.info("font: " + expected.getPdfTextInfos().get(i).getFont() + " | " + actual.getPdfTextInfos().get(i).getFont());
                logger.info("font size: " + expected.getPdfTextInfos().get(i).getFontSize() + " | " + actual.getPdfTextInfos().get(i).getFontSize());
                logger.info("StrokingColor: " + expected.getPdfTextInfos().get(i).getStrokingColor() + " | " + actual.getPdfTextInfos().get(i).getStrokingColor());
                logger.info("NonStrokingColor: " + expected.getPdfTextInfos().get(i).getNonStrokingColor() + " | " + actual.getPdfTextInfos().get(i).getNonStrokingColor());
                logger.info("RenderingMode: " + expected.getPdfTextInfos().get(i).getRenderingMode() + " | " + actual.getPdfTextInfos().get(i).getRenderingMode());
                logger.info("====================================");

                linesDifferentInAcutalPDF.add(lineDifferentInActualPDF);
                linesDifferentInExpectedPDF.add(lineDifferentInExpectedPDF);
            }
        }


        if (isTolerant) {

            //AI decidsion: 1. if all the incorrect X is grouped with the same Y , then it should be fine
            int countNotAlignedX = 0;
            boolean isStillAlignedX = true;
            for (int i = 0; i < linesDifferentInExpectedPDF.size(); i++) {
                String actualX = linesDifferentInAcutalPDF.get(i).getStartX();
                String expectedX = linesDifferentInExpectedPDF.get(i).getStartX();
                for (int j = i; j < linesDifferentInAcutalPDF.size(); j++) {
                    if (linesDifferentInAcutalPDF.get(j).getStartX().equals(actualX)) {
                        if (!expectedX.equals(linesDifferentInExpectedPDF.get(j).getStartX())) {
                            isStillAlignedX = false;
                            logger.info("there is one spot not aligned between expected pdf and actual pdf, even after adjustment , expected startX= " + actualX);
                            logger.info("value: " + linesDifferentInExpectedPDF.get(i).getValue() + " | " + linesDifferentInAcutalPDF.get(j).getValue());
                            logger.info("x " + linesDifferentInExpectedPDF.get(i).getStartX() + " | " + linesDifferentInAcutalPDF.get(j).getStartX());
                            logger.info("y " + linesDifferentInExpectedPDF.get(i).getStartY() + " | " + linesDifferentInAcutalPDF.get(j).getStartY());
                            logger.info("width " + linesDifferentInExpectedPDF.get(i).getWidth() + " | " + linesDifferentInAcutalPDF.get(j).getWidth());
                            logger.info("height " + linesDifferentInExpectedPDF.get(i).getHeight() + " | " + linesDifferentInAcutalPDF.get(j).getHeight());
                            logger.info("font " + linesDifferentInExpectedPDF.get(i).getFont() + " | " + linesDifferentInAcutalPDF.get(j).getFont());
                            logger.info("font size " + linesDifferentInExpectedPDF.get(i).getFontSize() + " | " + linesDifferentInAcutalPDF.get(j).getFontSize());
                            //logger.info("StrokingColor " + linesDifferentInExpectedPDF.get(i).getFontSize() + " | " + linesDifferentInAcutalPDF.get(j).getFontSize());
                            countNotAlignedX++;
                        } else {
                            logger.info("the difference in X is ignored after adjustment of alignment of X : " + linesDifferentInExpectedPDF.get(i).getStartX() + " | " + linesDifferentInAcutalPDF.get(j).getStartX());

                            countStartXNotSame--;
                        }

                    }
                }
            }

            int ccountNotAlignedY = 0;
            boolean isStillAlignedY = true;
            for (int i = 0; i < linesDifferentInExpectedPDF.size(); i++) {
                String actualY = linesDifferentInAcutalPDF.get(i).getStartY();
                String expectedY = linesDifferentInExpectedPDF.get(i).getStartY();
                for (int j = i; j < linesDifferentInAcutalPDF.size(); j++) {
                    if (linesDifferentInAcutalPDF.get(j).getStartX().equals(actualY)) {
                        if (!expectedY.equals(linesDifferentInExpectedPDF.get(j).getStartY())) {
                            isStillAlignedY = false;
                            logger.info("there is one spot not aligned between expected pdf and actual pdf, even after adjustment , expected startX= " + actualY);
                            logger.info("value: " + linesDifferentInExpectedPDF.get(i).getValue() + " | " + linesDifferentInAcutalPDF.get(j).getValue());
                            logger.info("x " + linesDifferentInExpectedPDF.get(i).getStartX() + " | " + linesDifferentInAcutalPDF.get(j).getStartX());
                            logger.info("y " + linesDifferentInExpectedPDF.get(i).getStartY() + " | " + linesDifferentInAcutalPDF.get(j).getStartY());
                            logger.info("width " + linesDifferentInExpectedPDF.get(i).getWidth() + " | " + linesDifferentInAcutalPDF.get(j).getWidth());
                            logger.info("height " + linesDifferentInExpectedPDF.get(i).getHeight() + " | " + linesDifferentInAcutalPDF.get(j).getHeight());
                            logger.info("font " + linesDifferentInExpectedPDF.get(i).getFont() + " | " + linesDifferentInAcutalPDF.get(j).getFont());
                            logger.info("font size " + linesDifferentInExpectedPDF.get(i).getFontSize() + " | " + linesDifferentInAcutalPDF.get(j).getFontSize());
                            ccountNotAlignedY++;
                        } else {
                            logger.info("the difference in Y is ignored after adjustment of alignment of Y : " + linesDifferentInExpectedPDF.get(i).getStartY() + " | " + linesDifferentInAcutalPDF.get(j).getStartY());
                            countStartYNotSame--;
                        }
                    }
                }
            }

            // 2. the deviation is in certain scope
            int deviationStartXTooBigCount = 0;
            for (int i = 0; i < linesDifferentInExpectedPDF.size(); i++) {
                if (Math.abs(Float.valueOf(linesDifferentInExpectedPDF.get(i).getStartX()) - Float.valueOf(linesDifferentInAcutalPDF.get(i).getStartX())) / Float.valueOf(linesDifferentInExpectedPDF.get(i).getStartX()) > deviationStartX) {
                    deviationStartXTooBigCount++;
                    logger.info("the difference in X is ignored : " + linesDifferentInExpectedPDF.get(i).getStartX() + " | " + linesDifferentInAcutalPDF.get(i).getStartX());

                } else {
                    // countStartXNotSame--;
                }
            }
            int deviationStartYTooBigCount = 0;
            for (int i = 0; i < linesDifferentInExpectedPDF.size(); i++) {
                if (Math.abs(Float.valueOf(linesDifferentInExpectedPDF.get(i).getStartY()) - Float.valueOf(linesDifferentInAcutalPDF.get(i).getStartY())) / Float.valueOf(linesDifferentInExpectedPDF.get(i).getStartY()) > deviationStartY) {
                    deviationStartXTooBigCount++;
                    logger.info("the difference in y is ignored : " + linesDifferentInExpectedPDF.get(i).getStartY() + " | " + linesDifferentInAcutalPDF.get(i).getStartY());
                } else {
                    //  countStartYNotSame--;
                }
            }
            int deviationWidthooBigCount = 0;
            for (int i = 0; i < linesDifferentInExpectedPDF.size(); i++) {
                if (Math.abs(Float.valueOf(linesDifferentInExpectedPDF.get(i).getWidth()) - Float.valueOf(linesDifferentInAcutalPDF.get(i).getWidth())) / Float.valueOf(linesDifferentInExpectedPDF.get(i).getWidth()) > deviationWidth) {
                    deviationWidthooBigCount++;
                    logger.info("the difference in X is ignored : " + linesDifferentInExpectedPDF.get(i).getWidth() + " | " + linesDifferentInAcutalPDF.get(i).getWidth());

                } else {
                    countWidthNotSame--;
                }
            }
            //value expected changes

            for (int i = 0; i < linesDifferentInExpectedPDF.size(); i++) {

                if (!linesDifferentInExpectedPDF.get(i).getValue().equals(linesDifferentInAcutalPDF.get(i).getValue())) {

                    boolean found = false;
                    //deal with values
                    for (KFilter filter : filters) {
                        int index = expected.findTextLinesByFilter(filter);
                        if ((index > 0) && (Integer.valueOf(linesDifferentInExpectedPDF.get(i).getIndex()) == index) && (!found)) {
                            //
                            if (filter.getReplacement() == null) {
                                //it means this line should be ignored
                                logger.info("due to no expected value, the difference in value is ignored : " + linesDifferentInExpectedPDF.get(i).getValue() + " | " + linesDifferentInAcutalPDF.get(i).getValue());
                                countValueNotSame--;
                                countWidthNotSame--;
                                found = true;
                            } else {
                                if (filter.getReplacement().equals(linesDifferentInAcutalPDF.get(i).getValue()) && (linesDifferentInExpectedPDF.get(i).getValue().equals(filter.getValue()))) {
                                    // the replacement (from what we calculate) equals the actual pdf's actual value and this is the line we have made a template on (cuz the values in expected/filter/expected are the same, it means the same line)
                                    countValueNotSame--;
                                    countWidthNotSame--;
                                    found = true;
                                } else {
                                    logger.info("there is one line not having the same text value as expected ");
                                    logger.info("value: " + linesDifferentInExpectedPDF.get(i).getValue() + " | " + linesDifferentInAcutalPDF.get(i).getValue());
                                    logger.info("x " + linesDifferentInExpectedPDF.get(i).getStartX() + " | " + linesDifferentInAcutalPDF.get(i).getStartX());
                                    logger.info("y " + linesDifferentInExpectedPDF.get(i).getStartY() + " | " + linesDifferentInAcutalPDF.get(i).getStartY());
                                    logger.info("width " + linesDifferentInExpectedPDF.get(i).getWidth() + " | " + linesDifferentInAcutalPDF.get(i).getWidth());
                                    logger.info("height " + linesDifferentInExpectedPDF.get(i).getHeight() + " | " + linesDifferentInAcutalPDF.get(i).getHeight());
                                    logger.info("font " + linesDifferentInExpectedPDF.get(i).getFont() + " | " + linesDifferentInAcutalPDF.get(i).getFont());
                                    logger.info("font size " + linesDifferentInExpectedPDF.get(i).getFontSize() + " | " + linesDifferentInAcutalPDF.get(i).getFontSize());
                                    found = true;
                                }
                            }
                        }
                    }
                    if (!found) {
                        //it means cannot find filter ,it's a new bug
                        logger.info("there is one line not having not been mentioned in the template ");
                        logger.info("value: " + linesDifferentInExpectedPDF.get(i).getValue() + " | " + linesDifferentInAcutalPDF.get(i).getValue());
                        logger.info("x " + linesDifferentInExpectedPDF.get(i).getStartX() + " | " + linesDifferentInAcutalPDF.get(i).getStartX());
                        logger.info("y " + linesDifferentInExpectedPDF.get(i).getStartY() + " | " + linesDifferentInAcutalPDF.get(i).getStartY());
                        logger.info("width " + linesDifferentInExpectedPDF.get(i).getWidth() + " | " + linesDifferentInAcutalPDF.get(i).getWidth());
                        logger.info("height " + linesDifferentInExpectedPDF.get(i).getHeight() + " | " + linesDifferentInAcutalPDF.get(i).getHeight());
                        logger.info("font " + linesDifferentInExpectedPDF.get(i).getFont() + " | " + linesDifferentInAcutalPDF.get(i).getFont());
                        logger.info("font size " + linesDifferentInExpectedPDF.get(i).getFontSize() + " | " + linesDifferentInAcutalPDF.get(i).getFontSize());
                    }
                }
            }
            //save the filters
            if ((0 >= countValueNotSame) && (0 >= deviationStartXTooBigCount) && (0 == deviationStartYTooBigCount) && (0 == ccountNotAlignedY) && (0 == countNotAlignedX)) {
                differenceCount = 0;
                comparisonResult.put("deviationStartXTooBigCount", deviationStartXTooBigCount);
                comparisonResult.put("deviationStartYTooBigCount", deviationStartYTooBigCount);
                comparisonResult.put("ccountNotAlignedY", ccountNotAlignedY);
                comparisonResult.put("countNotAlignedX", countNotAlignedX);
                comparisonResult.put("differenceCount", differenceCount);
            }


        }


        comparisonResult.put("countValueNotSame", countValueNotSame);
        comparisonResult.put("countHeightNotSame", countHeightNotSame);
        comparisonResult.put("countWidthNotSame", countWidthNotSame);
        comparisonResult.put("countStartXNotSame", countStartXNotSame);
        comparisonResult.put("countStartYNotSame", countStartYNotSame);
        comparisonResult.put("countFontSizeNotSame", countFontSizeNotSame);
        comparisonResult.put("countFontNotSame", countFontNotSame);
        comparisonResult.put("countStrokingColorNotSame", countStrokingColorNotSame);
        comparisonResult.put("countNonStrokingColorNotSame", countNonStrokingColorNotSame);
        comparisonResult.put("countRenderingModeNotSame", countRenderingModeNotSame);

        for (Map.Entry<String, Integer> entry : comparisonResult.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            logger.info(key.toString() + " : " + value.toString());
        }

        return comparisonResult;// > 0 ? false : true;

    }

    public boolean comparePdfByTextColor(PDDocument pdDocExpected, PDDocument pdDocActual, List<KFilter> filters) throws IOException {
        KPDFTextStripper actual = new KPDFTextStripper();
        actual.getText(pdDocActual);
        KPDFTextStripper expected = new KPDFTextStripper();
        expected.getText(pdDocExpected);
        return comparePdfByTextColor(actual, expected, filters);
    }

    public HashMap<String, Integer> comparePDFs(String expectedPath, String actualPath, String ignoreJson) throws Exception {
        return comparePDFs(expectedPath, actualPath, ignoreJson, true, true, true);
    }

    //visual compare; text content compare; text color compare
    public HashMap<String, Integer> comparePDFs(String expectedPdfFullpath, String actuaPDFFulllPath, String ignoreJson, boolean visualCompare, boolean textContentCompare, boolean textColorCompare) throws Exception {


        HashMap<String, Integer> result = new HashMap<String, Integer>();

        List<KFilter> filtersOnContext = generateFilters(ignoreJson);

        //get all the text info
        PDDocument pdDocActual = getPDDocument(actuaPDFFulllPath);
        PDDocument pdDocExpected = getPDDocument(expectedPdfFullpath);
        KPDFTextStripper stripperActual = new KPDFTextStripper();
        stripperActual.getText(pdDocActual);
        KPDFTextStripper stripperExpected = new KPDFTextStripper();
        stripperExpected.getText(pdDocExpected);


        if (textContentCompare) {
            result = comparePdfByTextContent(stripperExpected, stripperActual, filtersOnContext);
        }

//        if (textColorCompare) {
//             comparePdfByTextColor(stripperExpected, stripperActual, filtersOnContext);
//        }
        //cover the changed parts
        if (visualCompare) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String expectedPdf = "expected" + dateFormat.format(new Date()) + ".pdf";
            String actualPdf = "actual" + dateFormat.format(new Date()) + ".pdf";
            File fileExpected = new File(workingDirectory + expectedPdf);
            File fileActual = new File(workingDirectory + actualPdf);

            List<KFilter> kFilters = new ArrayList<KFilter>();
            for (int i = 0; i < linesDifferentInExpectedPDF.size(); i++) {
                KFilter kFilter = new KFilter();
                float startX = Math.min(Float.valueOf(linesDifferentInAcutalPDF.get(i).getStartX()), Float.valueOf(linesDifferentInExpectedPDF.get(i).getStartX()));
                kFilter.setStartX(String.valueOf(startX));
                float startY = Math.min(Float.valueOf(linesDifferentInAcutalPDF.get(i).getStartY()), Float.valueOf(linesDifferentInExpectedPDF.get(i).getStartY()));
                kFilter.setStartY(String.valueOf(startY));
                float maxHeight = Math.max(Float.valueOf(linesDifferentInAcutalPDF.get(i).getHeight()) + Float.valueOf(linesDifferentInAcutalPDF.get(i).getStartY()), Float.valueOf(linesDifferentInExpectedPDF.get(i).getHeight()) + Float.valueOf(linesDifferentInExpectedPDF.get(i).getStartY())) - startY;
                kFilter.setHeight(String.valueOf(maxHeight));
                float maxWidth = Math.max(Float.valueOf(linesDifferentInAcutalPDF.get(i).getWidth()) + Float.valueOf(linesDifferentInAcutalPDF.get(i).getStartX()), Float.valueOf(linesDifferentInExpectedPDF.get(i).getWidth()) + Float.valueOf(linesDifferentInExpectedPDF.get(i).getStartX())) - startX;
                kFilter.setWidth(String.valueOf(maxWidth));
                kFilters.add(kFilter);
            }

            generatePDFOnRectangles(pdDocExpected, kFilters, fileExpected);
            generatePDFOnRectangles(pdDocActual, kFilters, fileActual);

            result.put("visualComparison", (comparePDFsVisually(pdDocExpected, pdDocActual) ? 0 : 1));
        }
        pdDocExpected.close();
        pdDocActual.close();
        return result;
    }

    private boolean comparePdfByTextColor(KPDFTextStripper stripperExpected, KPDFTextStripper stripperActual, List<KFilter> filtersOnContext) {

        List<KFilter> filters = filtersOnContext;
        boolean isSame = true;

        if ((stripperExpected.getGraphicsStates() == null) && (stripperActual.getGraphicsStates() == null)) {
            return true;
        } else if ((stripperExpected.getGraphicsStates() != null) && (stripperActual.getGraphicsStates() != null)) {
            if (stripperExpected.getGraphicsStates().size() != stripperActual.getGraphicsStates().size())
                return false;
            else {
                for (GraphicsState graphicsState : stripperExpected.getGraphicsStates().keySet()) {
                    while (!stripperExpected.getGraphicsStates().get(graphicsState).equals(stripperActual.getGraphicsStates().get(graphicsState)) && filters.size() > 0) {
                        isSame = false;
                        for (KFilter filter : filters) {
                            if (stripperExpected.getGraphicsStates().get(graphicsState).contains(filter.getValue())) {
                                stripperExpected.getGraphicsStates().get(graphicsState).replace(filter.getValue(), filter.getReplacement());
                                filters.remove(filter);
                                isSame = true;
                            }
                        }

                    }
                }
                return isSame;

                //return stripperExpected.getGraphicsStates().equals(stripperActual.getGraphicsStates());
            }
        } else {
            return false;
        }

    }

    public boolean comparePDFsVisually(PDDocument pdDocExpected, PDDocument pdDocActual) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        String jpgPathNameToSaveExpected = workingDirectory + File.separator + "expected" + dateFormat.format(new Date());
        List<String> jpgPathNameToSaveExpecteds = pdfToImage(pdDocExpected, jpgPathNameToSaveExpected);

        String jpgPathNameToSaveActual = workingDirectory + File.separator + "actual" + dateFormat.format(new Date());
        List<String> jpgPathNameToSaveActuals = pdfToImage(pdDocActual, jpgPathNameToSaveActual);

        String jpagPathNameResult = workingDirectory + File.separator + "result" + dateFormat.format(new Date());

        //compare pixels
        boolean result = (jpgPathNameToSaveActuals.size() == jpgPathNameToSaveExpecteds.size());
        if (result) {
            for (int i = 0; i < jpgPathNameToSaveActuals.size(); i++) {

                ImageComparison imageComparison = new ImageComparison(10, 10, 0.05);
                result = (imageComparison.fuzzyEqual(jpgPathNameToSaveExpecteds.get(i), jpgPathNameToSaveActuals.get(i), jpagPathNameResult + String.valueOf(i) + ".jpg"));
                logger.info(jpgPathNameToSaveExpecteds.get(i) + " is the same as " + jpgPathNameToSaveActuals.get(i) + " : " + result);
            }
        }
        return result;
    }

    private PDDocument getPDDocument(String fileName) throws IOException {
        PDFParser parser = null;
        COSDocument cosDoc = null;
        String parsedText = "";
        File file = new File(fileName);

        parser = new PDFParser(new RandomAccessFile(file, "r"));
        parser.parse();
        cosDoc = parser.getDocument();
        //  pdfStripper = new PDFTextStripper();
        return new PDDocument(cosDoc);


    }

    public List<String> pdfToImage(PDDocument pdDocument, String jpgPathNameToSaveNoSuffix) throws IOException {

        PDFRenderer renderer = new PDFRenderer(pdDocument);

        List<String> jpgPathNameToSaves = new ArrayList<String>();

        //Rendering an image from the PDF document
        for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {
            BufferedImage image = renderer.renderImage(i);

            String jpgPathNameToSave = jpgPathNameToSaveNoSuffix + String.valueOf(i);
            //Writing the image to a file
            ImageIO.write(image, "JPEG", new File(jpgPathNameToSave + ".jpg"));

            jpgPathNameToSaves.add(jpgPathNameToSave + ".jpg");
        }
        //Closing the document
        // document.close();
        return jpgPathNameToSaves;
    }

    public void testPDFBoxExtractImages(PDDocument pdDocument) throws Exception {

        PDPageTree list = pdDocument.getPages();
        for (PDPage page : list) {
            PDResources pdResources = page.getResources();
            for (COSName c : pdResources.getXObjectNames()) {
                PDXObject o = pdResources.getXObject(c);
                if (o instanceof org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) {
                    File file = new File("C:/source/" + System.nanoTime() + ".png");
                    ImageIO.write(((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) o).getImage(), "png", file);
                }
            }
        }
    }

    public List<KFilter> generateFilters(String configJson) throws IOException {

        List<KFilter> filters = new ArrayList<KFilter>();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = mapper.readValue(configJson, Map.class);

        JsonNode rootNode = mapper.readTree(configJson);
        for (JsonNode jsonNode : rootNode.get("filter")) {
            KFilter filter = new KFilter();
            if ((jsonNode.get("value") != null) && !jsonNode.get("value").equals("")) {
                filter.setValue(jsonNode.get("value").textValue());
            }
            if ((jsonNode.get("FontSize") != null) && !jsonNode.get("FontSize").equals("")) {
                filter.setFontSize(jsonNode.get("FontSize").textValue());
            }
            if ((jsonNode.get("font") != null) && !jsonNode.get("font").equals("")) {
                filter.setFont(jsonNode.get("font").textValue());
            }
            if ((jsonNode.get("startX") != null) && !jsonNode.get("startX").equals("")) {
                filter.setStartX(jsonNode.get("startX").textValue());
            }
            if ((jsonNode.get("startY") != null) && !jsonNode.get("startY").equals("")) {
                filter.setStartY(jsonNode.get("startY").textValue());
            }
            if ((jsonNode.get("width") != null) && !jsonNode.get("width").equals("")) {
                filter.setWidth(jsonNode.get("width").textValue());
            }
            if ((jsonNode.get("height") != null) && !jsonNode.get("height").equals("")) {
                filter.setHeight(jsonNode.get("height").textValue());
            }
            if ((jsonNode.get("offset") != null) && !jsonNode.get("offset").equals("")) {
                filter.setOffset(jsonNode.get("offset").textValue());
            }
            if ((jsonNode.get("index") != null) && !jsonNode.get("index").equals("")) {
                filter.setIndex(jsonNode.get("index").textValue());
            }
            if ((jsonNode.get("replacement") != null) && !jsonNode.get("replacement").equals("")) {
                filter.setReplacement(jsonNode.get("replacement").textValue());
            }

            filters.add(filter);

        }
        return filters;
    }

    public void generatePDFOnRectangles(PDDocument pddDocument, List<KFilter> filtersToExcludeRectangles, File newPDFPath) throws IOException {

        PDPage page = pddDocument.getPage(0);
        PDPageContentStream contentStream = new PDPageContentStream(pddDocument, page, true, true);

        //Setting the non stroking color

        contentStream.setNonStrokingColor(Color.green);

        for (KFilter filter : filtersToExcludeRectangles) {
            int x = Math.round(Float.valueOf(filter.getStartX()));
            int y = Math.round(Float.valueOf(filter.getStartY()));
            int width = Math.round(Float.valueOf(filter.getWidth()));
            int height = Math.round(Float.valueOf(filter.getHeight()));
            contentStream.addRect(x - 4, y - 4, width + 5, height + 5);//9091335
            // break;
        }
        contentStream.fill();

        System.out.println("rectangle added");

        //Closing the ContentStream object
        contentStream.close();

        //Saving the document
        pddDocument.save(newPDFPath);

    }

    public void generatePDFOnFilters(PDDocument pddDocument, List<KFilter> filtersToExcludeRectangles, List<KPDFTextInfo> contentLinesToCompareWithFilters, File newPDFPath) throws IOException {
        //change the actual pdf to the format of expected pdf, in order to see if there is new change , if not, pass
        //Loading an existing document


        //Retrieving a page of the PDF Document
        PDPage page = pddDocument.getPage(0);

        //Instantiating the PDPageContentStream class
        PDPageContentStream contentStream = new PDPageContentStream(pddDocument, page, true, true);


        //Setting the non stroking color

        contentStream.setNonStrokingColor(Color.white);

        for (KFilter filter : filtersToExcludeRectangles) {
            if ((contentLinesToCompareWithFilters != null) || (contentLinesToCompareWithFilters.size() != 0)) {
                //locate the rectangle by meaning full text content, font size and only a few postion information
                if (!filter.getIndex().equals("")) {
                    int x = (int) contentLinesToCompareWithFilters.get(Integer.valueOf(filter.getIndex())).getStartX();
                    int y = (int) contentLinesToCompareWithFilters.get(Integer.valueOf(filter.getIndex())).getStartY();
                    int width = (int) contentLinesToCompareWithFilters.get(Integer.valueOf(filter.getIndex())).getWidth();
                    int height = (int) contentLinesToCompareWithFilters.get(Integer.valueOf(filter.getIndex())).getHeight();
                    contentStream.addRect(x - 1, y - 1, width + 3, height + 2);//9091335
                    // break;
                }

                for (KPDFTextInfo pdfTextInfo : contentLinesToCompareWithFilters) {
                    boolean found = true;
                    if ((!filter.getValue().equals("")) && (!pdfTextInfo.getValue().contains(filter.getValue()))) {
                        found = false;
                    }
                    if ((!filter.getFont().equals("")) && (!filter.getFont().equals(pdfTextInfo.getFont()))) {
                        found = false;

                    }
                    if ((!filter.getFontSize().equals("")) && (!filter.getFontSize().equals(String.valueOf(pdfTextInfo.getFontSize())))) {
                        found = false;

                    }
                    if ((!filter.getStartX().equals("")) && (!filter.getStartX().equals(String.valueOf(pdfTextInfo.getStartX())))) {
                        found = false;

                    }
                    if ((!filter.getStartY().equals("")) && (!filter.getStartY().equals(String.valueOf(pdfTextInfo.getStartY())))) {
                        found = false;

                    }
                    if (found) {
                        int x = (int) pdfTextInfo.getStartX();
                        int y = (int) pdfTextInfo.getStartY();
                        int width = (int) pdfTextInfo.getWidth();
                        int height = (int) pdfTextInfo.getHeight();
                        contentStream.addRect(x - 1, y - 1, width + 3, height + 2);//9091335
                    }

                }
            } else {//locate the rectangle area exclusively by postion information
                int x = Integer.valueOf(filter.getStartX());
                int y = Integer.valueOf(filter.getStartY());
                int width = Integer.valueOf(filter.getWidth());
                int height = Integer.valueOf(filter.getHeight());
                contentStream.addRect(x, y, width, height);
            }
        }

        //Drawing a rectangle
        contentStream.fill();

        System.out.println("rectangle added");

        //Closing the ContentStream object
        contentStream.close();

        //Saving the document
        pddDocument.save(newPDFPath);


    }


}
