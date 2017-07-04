
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
import org.apache.pdfbox.text.PDFTextStripperByArea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
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

/*    public boolean compareTwoImages(File fileOne, File fileTwo) {
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
    }*/

    public List<ResultTextCompare> comparePdfByText(PDDocument pdDocExpected, PDDocument pdDocActual, List<KFilter> filters) throws IOException {

        KPDFTextStripper actual = new KPDFTextStripper();
        actual.getText(pdDocActual);
        KPDFTextStripper expected = new KPDFTextStripper();
        expected.getText(pdDocExpected);

        return comparePdfByText(expected, actual, filters, true, 0.03f, 0, 0, 0);
    }

    public List<ResultTextCompare> comparePdfByText(KPDFTextStripper expected, KPDFTextStripper actual, List<KFilter> filters) throws IOException {


        return comparePdfByText(expected, actual, filters, true, 0.03f, 0, 0.01f, 0);
    }

    public class ResultTextCompare {
        public KFilter expected;
        public KFilter actual;
        public String reason;

        public ResultTextCompare() {
            expected = new KFilter();
            actual = new KFilter();
            reason = null;
        }

    }

    public List<ResultTextCompare> checkIfAlignedInCooridnate(List<ResultTextCompare> resultTextCompares, String coordinate) {
        int countNotAligned = 0;
        boolean isStillAligned = true;


        if (!coordinate.toLowerCase().equals("x") && !coordinate.toLowerCase().equals("y"))
            throw new IllegalArgumentException("coordinate can be only x or y");

        List<String> actualCordinatelist = new ArrayList<String>();
        for (KFilter kFilter : linesDifferentInAcutalPDF) {
            if (coordinate.toLowerCase().equals("x")) {
                actualCordinatelist.add(kFilter.getStartX());
            }
            if (coordinate.toLowerCase().equals("y")) {
                actualCordinatelist.add(kFilter.getStartY());
            }
        }
        List<String> expectedCordinatelist = new ArrayList<String>();
        for (KFilter kFilter : linesDifferentInExpectedPDF) {
            if (coordinate.toLowerCase().equals("x")) {
                expectedCordinatelist.add(kFilter.getStartX());
            }
            if (coordinate.toLowerCase().equals("y")) {
                expectedCordinatelist.add(kFilter.getStartY());
            }
        }
        String actualCordinate;
        String expectedCoridnate;
        for (int i = 0; i < linesDifferentInExpectedPDF.size(); i++) {
            if (coordinate.toLowerCase().equals("x")) {
                actualCordinate = linesDifferentInAcutalPDF.get(i).getStartX();
                expectedCoridnate = linesDifferentInExpectedPDF.get(i).getStartX();
            } else {
                actualCordinate = linesDifferentInAcutalPDF.get(i).getStartY();
                expectedCoridnate = linesDifferentInExpectedPDF.get(i).getStartY();
            }
            boolean foundMatch = false;
            for (int j = i + 1; j < linesDifferentInExpectedPDF.size(); j++) {

                if (expectedCordinatelist.get(j).equals(expectedCoridnate)) {
                    if (actualCordinatelist.get(j).equals(actualCordinate)) {
                        //the deviation in x alignment is ignored
                        if (coordinate.toLowerCase().equals("x")) {
                            logger.info("the difference in X is ignored after adjustment of alignment of X : " + linesDifferentInExpectedPDF.get(i).getStartX() + " | " + linesDifferentInAcutalPDF.get(j).getStartX());
                        }

                        if (coordinate.toLowerCase().equals("y")) {
                            logger.info("the difference in Y is ignored after adjustment of alignment of Y : " + linesDifferentInExpectedPDF.get(i).getStartY() + " | " + linesDifferentInAcutalPDF.get(j).getStartY());
                        }

                        foundMatch = true;
                        actualCordinatelist.set(j, "");
                        expectedCordinatelist.set(j, "");
                    } else {

                        actualCordinatelist.set(j, "");
                        expectedCordinatelist.set(j, "");
                        countNotAligned++;

                        String reason = coordinate + " cordinate is not aligned after ajustment";
                        String message = "there is one spot not aligned between expected pdf and actual pdf, even after adjustment , expected start" + coordinate + "= " + actualCordinate;
                        resultTextCompares = logDifference(message, resultTextCompares, j, reason);
                    }
                }
            }

            if (foundMatch) {
                actualCordinatelist.set(i, "");
                expectedCordinatelist.set(i, "");
                //   countStartXNotSame--;
            }
            {
                //so if one deviation in X cannot find at least one pair, then computer has no idea if it's right or wrong, in this case , will output wrong anyway
                ResultTextCompare resultTextCompare = new ResultTextCompare();
                resultTextCompare.expected = linesDifferentInExpectedPDF.get(i);
                resultTextCompare.actual = linesDifferentInAcutalPDF.get(i);
                resultTextCompare.reason = coordinate + " cordinate is not aligned after ajustment";
                resultTextCompares.add(resultTextCompare);
            }
        }

        return resultTextCompares;
    }

    public List<ResultTextCompare> comparePDFByDeviation(String coordiante, List<ResultTextCompare> resultTextCompares, Float deviationCoordinate) {
        int deviationTooBigCount = 0;
        String message = null;
        Float deviation = Float.valueOf(0);
        for (int i = 0; i < resultTextCompares.size(); i++) {
            if (coordiante.toLowerCase().equals("x")) {
                deviation = Math.abs(Float.valueOf(resultTextCompares.get(i).actual.getStartX()) - Float.valueOf(resultTextCompares.get(i).expected.getStartX())) / Math.max(Float.valueOf(resultTextCompares.get(i).expected.getStartX()), Float.valueOf(resultTextCompares.get(i).actual.getStartX()));
                message = "the difference in " + coordiante.toUpperCase() + " is too big : " + resultTextCompares.get(i).expected.getStartX() + " | " + linesDifferentInAcutalPDF.get(i).getStartX();
            }
            if (coordiante.toLowerCase().equals("y")) {
                deviation = Math.abs(Float.valueOf(resultTextCompares.get(i).actual.getStartY()) - Float.valueOf(resultTextCompares.get(i).expected.getStartY())) / Math.max(Float.valueOf(resultTextCompares.get(i).expected.getStartX()), Float.valueOf(resultTextCompares.get(i).actual.getStartX()));
                message = "the difference in " + coordiante.toUpperCase() + " is too big : " + resultTextCompares.get(i).expected.getStartY() + " | " + linesDifferentInAcutalPDF.get(i).getStartY();
            }
            if (coordiante.toLowerCase().equals("witdh")) {
                deviation = Math.abs(Float.valueOf(resultTextCompares.get(i).actual.getWidth()) - Float.valueOf(resultTextCompares.get(i).expected.getWidth())) / Math.max(Float.valueOf(resultTextCompares.get(i).expected.getWidth()), Float.valueOf(resultTextCompares.get(i).actual.getWidth()));
                message = "the difference in " + coordiante.toUpperCase() + " is too big : " + resultTextCompares.get(i).expected.getWidth() + " | " + linesDifferentInAcutalPDF.get(i).getWidth();
            }
            if (coordiante.toLowerCase().equals("height")) {
                deviation = Math.abs(Float.valueOf(resultTextCompares.get(i).actual.getHeight()) - Float.valueOf(resultTextCompares.get(i).expected.getHeight())) / Math.max(Float.valueOf(resultTextCompares.get(i).expected.getHeight()), Float.valueOf(resultTextCompares.get(i).actual.getHeight()));
                message = "the difference in " + coordiante.toUpperCase() + " is too big : " + resultTextCompares.get(i).expected.getHeight() + " | " + linesDifferentInAcutalPDF.get(i).getHeight();

                throw new IllegalArgumentException("the values of coordinate can be only x or y");

            }
            if (deviation != 0) {

                if (deviation > deviationCoordinate) {
                    deviationTooBigCount++;

                    String reason = "deviation in " + coordiante.toUpperCase() + " coordinate is out of the tolerance region";
                    resultTextCompares = logDifference(message, resultTextCompares, i, reason);


                } else {
                    // countStartXNotSame--;
                    //it's not cocordinate deviation
                }
            }
        }
        return resultTextCompares;
    }


    public List<ResultTextCompare> compareTextNoTolerance(KPDFTextStripper expected, KPDFTextStripper actual) {
        List<ResultTextCompare> resultTextCompares = new ArrayList<ResultTextCompare>();

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

        //int deviationLines =
        KFilter lineDifferentInActualPDF = new KFilter();
        KFilter lineDifferentInExpectedPDF = new KFilter();

        //if it's passed above, continue to compare other parts
        for (int i = 0; i < Math.min(expected.getPdfTextInfos().size(), actual.getPdfTextInfos().size()); i++) {
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
//            if (!expected.getPdfTextInfos().get(i).getStrokingColor().equals(actual.getPdfTextInfos().get(i).getStrokingColor())) {
//                isSame = false;
//                countStrokingColorNotSame++;// = false;
//                logger.info(countStrokingColorNotSame);
//            }
//            if (!expected.getPdfTextInfos().get(i).getNonStrokingColor().equals(actual.getPdfTextInfos().get(i).getNonStrokingColor())) {
//                isSame = false;
//                countNonStrokingColorNotSame++;// = false;
//                logger.info(countNonStrokingColorNotSame);
//            }
//            if (!expected.getPdfTextInfos().get(i).getRenderingMode().equals(actual.getPdfTextInfos().get(i).getRenderingMode())) {
//                isSame = false;
//                countRenderingModeNotSame++;// = false;
//                logger.info(countRenderingModeNotSame);
//            }

            if (!isSame) {
                differenceCount++;
                String message = "line " + Integer.toString(i) + " : expected | actual";
                ResultTextCompare resultTextCompare = new ResultTextCompare();
                resultTextCompare.expected.setValue(expected.getPdfTextInfos().get(i).getValue());
                resultTextCompare.expected.setStartX(String.valueOf(expected.getPdfTextInfos().get(i).getStartX()));
                resultTextCompare.expected.setStartY(String.valueOf(expected.getPdfTextInfos().get(i).getStartY()));
                resultTextCompare.expected.setWidth(String.valueOf(expected.getPdfTextInfos().get(i).getWidth()));
                resultTextCompare.expected.setHeight(String.valueOf(expected.getPdfTextInfos().get(i).getHeight()));
                resultTextCompare.expected.setFont(expected.getPdfTextInfos().get(i).getFont());
                resultTextCompare.expected.setFontSize(String.valueOf(expected.getPdfTextInfos().get(i).getFontSize()));
                resultTextCompare.expected.setIndex(String.valueOf(i));
//                resultTextCompare.expected.setStrokingColor(expected.getPdfTextInfos().get(i).getStrokingColor());
//                resultTextCompare.expected.setNonStrokingColor(expected.getPdfTextInfos().get(i).getNonStrokingColor());
//                resultTextCompare.expected.setRenderingMode(expected.getPdfTextInfos().get(i).getRenderingMode());

                resultTextCompare.actual.setIndex(String.valueOf(i));
                resultTextCompare.actual.setValue(actual.getPdfTextInfos().get(i).getValue());
                resultTextCompare.actual.setStartX(String.valueOf(actual.getPdfTextInfos().get(i).getStartX()));
                resultTextCompare.actual.setStartY(String.valueOf(actual.getPdfTextInfos().get(i).getStartY()));
                resultTextCompare.actual.setWidth(String.valueOf(actual.getPdfTextInfos().get(i).getWidth()));
                resultTextCompare.actual.setHeight(String.valueOf(actual.getPdfTextInfos().get(i).getHeight()));
                resultTextCompare.actual.setFont(actual.getPdfTextInfos().get(i).getFont());
                resultTextCompare.actual.setFontSize(String.valueOf(actual.getPdfTextInfos().get(i).getFontSize()));
//                resultTextCompare.actual.setStrokingColor(expected.getPdfTextInfos().get(i).getStrokingColor());
//                resultTextCompare.actual.setNonStrokingColor(expected.getPdfTextInfos().get(i).getNonStrokingColor());
//                resultTextCompare.actual.setRenderingMode(expected.getPdfTextInfos().get(i).getRenderingMode());
                String reason = "no tolerance";
                resultTextCompares.add(resultTextCompare);

                logDifference(message, resultTextCompares, null, reason);
            }
        }

        if (Math.abs(expected.getPdfTextInfos().size() - actual.getPdfTextInfos().size()) > 0) {
            comparisonResult.put("the defference in content Lines is ", Math.abs(expected.getPdfTextInfos().size() - actual.getPdfTextInfos().size()));
            logger.info("comparison stops since the difference is too apparent");
            if (expected.getPdfTextInfos().size() > actual.getPdfTextInfos().size()) {
                for (int i = Math.min(expected.getPdfTextInfos().size(), actual.getPdfTextInfos().size()); i < expected.getPdfTextInfos().size(); i++) {
                    lineDifferentInExpectedPDF.setValue(expected.getPdfTextInfos().get(i).getValue());
                    lineDifferentInExpectedPDF.setStartX(String.valueOf(expected.getPdfTextInfos().get(i).getStartX()));
                    lineDifferentInExpectedPDF.setStartY(String.valueOf(expected.getPdfTextInfos().get(i).getStartY()));
                    lineDifferentInExpectedPDF.setWidth(String.valueOf(expected.getPdfTextInfos().get(i).getWidth()));
                    lineDifferentInExpectedPDF.setHeight(String.valueOf(expected.getPdfTextInfos().get(i).getHeight()));
                    lineDifferentInExpectedPDF.setFont(expected.getPdfTextInfos().get(i).getFont());
                    lineDifferentInExpectedPDF.setFontSize(String.valueOf(expected.getPdfTextInfos().get(i).getFontSize()));

                    ResultTextCompare resultTextCompare = new ResultTextCompare();
                    resultTextCompare.expected = lineDifferentInExpectedPDF;
                    resultTextCompare.actual = null;
                    resultTextCompare.reason = "no tolerance";
                    resultTextCompares.add(resultTextCompare);
                }
            }

            if (expected.getPdfTextInfos().size() < actual.getPdfTextInfos().size()) {
                for (int i = Math.min(expected.getPdfTextInfos().size(), actual.getPdfTextInfos().size()); i < actual.getPdfTextInfos().size(); i++) {
                    lineDifferentInActualPDF.setValue(actual.getPdfTextInfos().get(i).getValue());
                    lineDifferentInActualPDF.setStartX(String.valueOf(actual.getPdfTextInfos().get(i).getStartX()));
                    lineDifferentInActualPDF.setStartY(String.valueOf(actual.getPdfTextInfos().get(i).getStartY()));
                    lineDifferentInActualPDF.setWidth(String.valueOf(actual.getPdfTextInfos().get(i).getWidth()));
                    lineDifferentInActualPDF.setHeight(String.valueOf(actual.getPdfTextInfos().get(i).getHeight()));
                    lineDifferentInActualPDF.setFont(actual.getPdfTextInfos().get(i).getFont());
                    lineDifferentInActualPDF.setFontSize(String.valueOf(actual.getPdfTextInfos().get(i).getFontSize()));

                    ResultTextCompare resultTextCompare = new ResultTextCompare();
                    resultTextCompare.expected = null;
                    resultTextCompare.actual = lineDifferentInActualPDF;
                    resultTextCompare.reason = "no tolerance";
                    resultTextCompares.add(resultTextCompare);
                }
            }
            //stop tolerant comparison as it's already meaningless, too many differences

        }
        logger.info(expected.getPdfTextInfos().size());
        logger.info(actual.getPdfTextInfos().size());
        return resultTextCompares;

    }

    public List<ResultTextCompare> comparePdfByText(KPDFTextStripper expected, KPDFTextStripper actual, List<KFilter> filters, boolean isTolerant, float deviationStartX, float deviationStartY, float deviationWidth, float deviationHeight) throws IOException {

        List<ResultTextCompare> resultTextCompares = compareTextNoTolerance(expected, actual);

        if ((resultTextCompares.size() != 0) && (expected.getPdfTextInfos().size() != actual.getPdfTextInfos().size())) {
            //stop the toleranceCompare as the counts of lines are already different.
            return resultTextCompares;
        }

        //it can be triggered only when the counts of lines in two pdfs are the same
        if (isTolerant) {

            int countDifferentLines = resultTextCompares.size();
            //AI decidsion: 1. if all the incorrect X is grouped with the same Y , then it should be fine
            resultTextCompares = checkIfAlignedInCooridnate(resultTextCompares, "x");
            int countNotAlignedX = resultTextCompares.size() - countDifferentLines;
            resultTextCompares = checkIfAlignedInCooridnate(resultTextCompares, "y");
            int countNotAlignedY = resultTextCompares.size() - countNotAlignedX;


            // 2. the deviation is in certain scope
            resultTextCompares = comparePDFByDeviation("x", resultTextCompares.subList(0, countDifferentLines), deviationStartX);
            resultTextCompares = comparePDFByDeviation("y", resultTextCompares.subList(0, countDifferentLines), deviationStartY);
            resultTextCompares = comparePDFByDeviation("width", resultTextCompares.subList(0, countDifferentLines), deviationWidth);
            resultTextCompares = comparePDFByDeviation("hight", resultTextCompares.subList(0, countDifferentLines), deviationHeight);

            //save the filters
            resultTextCompares = compareTextContents(resultTextCompares, filters);


            //3. compare text by stroking/nonStocking color and rendering mode
            resultTextCompares = comparedTextByColorRenderingMode(expected, actual, filters, resultTextCompares);
        }
        return resultTextCompares;
    }

    public List<ResultTextCompare> comparedTextByColorRenderingMode(KPDFTextStripper kpdfTextStripperExpected, KPDFTextStripper kpdfTextStripperActual, List<KFilter> filters, List<ResultTextCompare> resultTextCompares) throws IOException {
        //value expected changes
        int countDifferenceInRenderingMode = 0;
        int countIgnoredDifferenceInTexts = 0;
        int countUnexpectecDifferenceInTexts = 0;

        int countKnownDifference = resultTextCompares.size();

        for (int i = 0; i < kpdfTextStripperExpected.getPdfTextInfos().size(); i++) {
            boolean sameColor = true;
            if (!kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap().equals(kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap())) {
                sameColor = false;
                logger.info(kpdfTextStripperExpected.getPdfTextInfos().get(i).getValue());
//                //conitnue comparison
//               for(int key: kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap().keySet())
//               {
//                   try {
//                       if (kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(key).equals(kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(key))) {
//                         /*  logger.info("E: "+kpdfTextStripperExpected.getPdfTextInfos().get(i).getValue());
//                           logger.info("E:"+(kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(key)).getNonStrokingColor());
//                           logger.info("E:"+(kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(key)).getStrokingColor());
//                           logger.info("E:"+(kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(key)).getRenderingMode());
//                           logger.info("E: "+kpdfTextStripperActual.getPdfTextInfos().get(i).getValue());
//                           logger.info("A:"+ (kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(key)).getNonStrokingColor());
//                           logger.info("A:"+ (kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(key)).getStrokingColor());
//                           logger.info("A:"+ (kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(key)).getRenderingMode());*/
//                        //   logger.info(kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(key).getValue());
//                           sameColor = true;
//
//                       } else {
//                           sameColor = false;
//                           logger.info("fdfasdfsa");
//                       }
//                   }
//                   catch(Exception e)
//                       {
//
//                       sameColor = false;
//                   }
//
            }
            if (!sameColor) {
                ResultTextCompare resultTextCompare = new ResultTextCompare();
                resultTextCompare.expected.setGraphicsStateHashMap(kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap());
                //   resultTextCompare.expected.setStrokingColor(kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(0).getStrokingColor());
                //   resultTextCompare.expected.setNonStrokingColor(kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(0).getNonStrokingColor());
                resultTextCompare.expected.setValue(kpdfTextStripperExpected.getPdfTextInfos().get(i).getValue());

                resultTextCompare.actual.setValue(kpdfTextStripperActual.getPdfTextInfos().get(i).getValue());
                resultTextCompare.actual.setGraphicsStateHashMap(kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap());
                //   resultTextCompare.actual.setStrokingColor(kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(0).getStrokingColor());
                //    resultTextCompare.actual.setNonStrokingColor(kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(0).getNonStrokingColor());
                //   resultTextCompare.actual.setRenderingMode(kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap().get(0).getRenderingMode());

                resultTextCompare.reason = "Color and Rendering Mode difference caught";

                resultTextCompares = logDifference(resultTextCompare.reason, resultTextCompares, resultTextCompare);
            }

        }

        return resultTextCompares;
       /* int index = 0;
        for (GraphicsState graphicsState : kpdfTextStripperExpected.getGraphicsStateList()) {
            if(!kpdfTextStripperActual.getGraphicsStateList().get(index).equals(kpdfTextStripperExpected.getGraphicsStateList().get(index)))
            {
                ResultTextCompare resultTextCompare = new ResultTextCompare();
                resultTextCompare.expected.setRenderingMode(kpdfTextStripperExpected.graphicsStateList.get(index).getRenderingMode());
                resultTextCompare.expected.setStrokingColor(kpdfTextStripperExpected.graphicsStateList.get(index).getStrokingColor());
                resultTextCompare.expected.setNonStrokingColor(kpdfTextStripperExpected.graphicsStateList.get(index).getNonStrokingColor());
                resultTextCompare.expected.setValue(kpdfTextStripperExpected.graphicsStateList.get(index).getValue());

                resultTextCompare.actual.setValue(kpdfTextStripperActual.graphicsStateList.get(index).getValue());
                resultTextCompare.actual.setStrokingColor(kpdfTextStripperActual.graphicsStateList.get(index).getStrokingColor());
                resultTextCompare.actual.setNonStrokingColor(kpdfTextStripperActual.graphicsStateList.get(index).getNonStrokingColor());
                resultTextCompare.actual.setRenderingMode(kpdfTextStripperActual.graphicsStateList.get(index).getRenderingMode());

                resultTextCompare.reason = "Color and Rendering Mode difference caught";

                resultTextCompares = logDifference(resultTextCompare.reason, resultTextCompares, resultTextCompare);
            }
            index++;

        }
        if(countKnownDifference< resultTextCompares.size())
        {
            //there is new difference even in the sum of colors
            return resultTextCompares;
        }



        for (index = 0; index<kpdfTextStripperExpected.graphicsStateList.size(); index++) {
            if (kpdfTextStripperExpected.graphicsStateList.get(index).getValue().equals(kpdfTextStripperActual.graphicsStateList.get(index).getValue())) {
                //it the string's values are the same, compare them dirrectly
            } else {
                //make an ajustment, remove the strings in the expected template as the content of them is not important (the actual values may have a different length, the key is make sure the string will be seperated into couple of strings, and each one will be in the right order and appears in the actual String
                logger.info(kpdfTextStripperExpected.graphicsStateList.get(index).getValue());
                logger.info(kpdfTextStripperActual.graphicsStateList.get(index).getValue());
                checkIfStringsSimilar(kpdfTextStripperExpected.graphicsStateList.get(index).getValue(), kpdfTextStripperActual.graphicsStateList.get(index).getValue(), filters);
            }

            if (kpdfTextStripperExpected.graphicsStateList.get(index).equals(kpdfTextStripperActual.graphicsStateList.get(index))) {
                // it's correct
            } else {
                ResultTextCompare resultTextCompare = new ResultTextCompare();
                resultTextCompare.expected.setRenderingMode(kpdfTextStripperExpected.graphicsStateList.get(index).getRenderingMode());
                resultTextCompare.expected.setStrokingColor(kpdfTextStripperExpected.graphicsStateList.get(index).getStrokingColor());
                resultTextCompare.expected.setNonStrokingColor(kpdfTextStripperExpected.graphicsStateList.get(index).getNonStrokingColor());
                resultTextCompare.expected.setValue(kpdfTextStripperExpected.graphicsStateList.get(index).getValue());

                resultTextCompare.actual.setValue(kpdfTextStripperActual.graphicsStateList.get(index).getValue());
                resultTextCompare.actual.setStrokingColor(kpdfTextStripperActual.graphicsStateList.get(index).getStrokingColor());
                resultTextCompare.actual.setNonStrokingColor(kpdfTextStripperActual.graphicsStateList.get(index).getNonStrokingColor());
                resultTextCompare.actual.setRenderingMode(kpdfTextStripperActual.graphicsStateList.get(index).getRenderingMode());

                resultTextCompare.reason = "Color and Rendering Mode difference caught";

                logDifference(resultTextCompare.reason, resultTextCompares, resultTextCompare);
            }


        }*/


    }


    protected int findTextLinesByFilter(List<KFilter> filters, ResultTextCompare resultTextCompare) {

        if ((null != filters) && (filters.size() != 1)) {

            for (int i = 0; i < filters.size(); i++) {
                boolean found = true;
                if ((null != filters.get(i).getFont()) && !resultTextCompare.expected.getFont().equals(filters.get(i).getFont())) {
                    found = false;
                }
                if ((null != filters.get(i).getFontSize()) && !(resultTextCompare.expected.getFontSize().equals(filters.get(i).getFontSize()))) {
                    found = false;
                }
                if ((null != filters.get(i).getValue()) && !resultTextCompare.expected.getValue().equals(filters.get(i).getValue())) {
                    found = false;
                }
                if ((null != filters.get(i).getStartX()) && !(String.valueOf(resultTextCompare.expected.getStartX()).equals(filters.get(i).getStartX()))) {
                    found = false;
                }
                if ((null != filters.get(i).getStartY()) && !(String.valueOf(resultTextCompare.expected.getStartY()).equals(filters.get(i).getStartY()))) {
                    found = false;
                }


                if (found) {
                    return i;
                }
            }
        }
        return -1;
    }

    private List<ResultTextCompare> logDifference(String message, List<ResultTextCompare> resultTextCompares, Integer index, String reason) {

        //get from already known resultTextCompares and analyse them to see if they can be grouped
        if (null == index) {
            //only print the log info
            index = resultTextCompares.size() - 1;

        } else {
            //otherwise, add
            ResultTextCompare resultTextCompare = new ResultTextCompare();
            resultTextCompare.expected = resultTextCompares.get(index).expected;
            resultTextCompare.actual = resultTextCompares.get(index).actual;
            resultTextCompare.reason = reason;// "unexpected Text content found";
            resultTextCompares.add(resultTextCompare);
        }

        logger.info(message);
        logger.info("value: " + "|" + resultTextCompares.get(index).expected.getValue() + "|" + resultTextCompares.get(index).actual.getValue() + "|");
        logger.info("x " + "|" + resultTextCompares.get(index).expected.getStartX() + " | " + resultTextCompares.get(index).actual.getStartX());
        logger.info("y " + "|" + resultTextCompares.get(index).expected.getStartY() + " | " + resultTextCompares.get(index).actual.getStartY());
        logger.info("width " + "|" + resultTextCompares.get(index).expected.getWidth() + " | " + resultTextCompares.get(index).actual.getWidth());
        logger.info("height " + "|" + resultTextCompares.get(index).expected.getHeight() + " | " + resultTextCompares.get(index).actual.getHeight());
        logger.info("font " + "|" + resultTextCompares.get(index).expected.getFont() + " | " + resultTextCompares.get(index).actual.getFont());
        logger.info("font size : " + "|" + resultTextCompares.get(index).expected.getFontSize() + " | " + resultTextCompares.get(index).actual.getFontSize());
        logger.info("getStrokingColor : " + "|" + resultTextCompares.get(index).expected.getStrokingColor() + " | " + resultTextCompares.get(index).actual.getStrokingColor());
        logger.info("getNonStrokingColor : " + "|" + resultTextCompares.get(index).expected.getNonStrokingColor() + " | " + resultTextCompares.get(index).actual.getNonStrokingColor());
        logger.info("getRenderingMode : " + "|" + resultTextCompares.get(index).expected.getRenderingMode() + " | " + resultTextCompares.get(index).actual.getRenderingMode());


        return resultTextCompares;
    }

    private List<ResultTextCompare> logDifference(String message, List<ResultTextCompare> resultTextCompares, ResultTextCompare resultTextCompare) throws IOException {


        resultTextCompares.add(resultTextCompare);
        int index = resultTextCompares.size() - 1;


        logger.info(message);
        logger.info("value: " + "|" + resultTextCompares.get(index).expected.getValue() + "|" + resultTextCompares.get(index).actual.getValue() + "|");
        logger.info("x " + "|" + resultTextCompares.get(index).expected.getStartX() + " | " + resultTextCompares.get(index).actual.getStartX());
        logger.info("y " + "|" + resultTextCompares.get(index).expected.getStartY() + " | " + resultTextCompares.get(index).actual.getStartY());
        logger.info("width " + "|" + resultTextCompares.get(index).expected.getWidth() + " | " + resultTextCompares.get(index).actual.getWidth());
        logger.info("height " + "|" + resultTextCompares.get(index).expected.getHeight() + " | " + resultTextCompares.get(index).actual.getHeight());
        logger.info("font " + "|" + resultTextCompares.get(index).expected.getFont() + " | " + resultTextCompares.get(index).actual.getFont());
        logger.info("font size : " + "|" + resultTextCompares.get(index).expected.getFontSize() + " | " + resultTextCompares.get(index).actual.getFontSize());

        logger.info("expected: ");
        for (Map.Entry<Integer, GraphicsState> entry : resultTextCompares.get(index).expected.getGraphicsStateHashMap().entrySet()) {
            logger.info("till " + entry.getKey() + " :" + "getRenderingMode : " + entry.getValue().getRenderingMode() + " getNonStrokingColor: " + entry.getValue().getNonStrokingColor().toRGB() + " geStrokingColor: " + entry.getValue().getStrokingColor().toRGB());
        }
        logger.info("actual: ");
        for (Map.Entry<Integer, GraphicsState> entry : resultTextCompares.get(index).actual.getGraphicsStateHashMap().entrySet()) {
            logger.info("till " + entry.getKey() + " :" + "getRenderingMode : " + entry.getValue().getRenderingMode() + " getNonStrokingColor: " + entry.getValue().getNonStrokingColor().toRGB() + " geStrokingColor: " + entry.getValue().getStrokingColor().toRGB());
        }

        return resultTextCompares;
    }

    public List<ResultTextCompare> compareTextContents(List<ResultTextCompare> resultTextCompares, List<KFilter> filters) {
        //value expected changes
        int countDifferenceInTexts = 0;
        int countIgnoredDifferenceInTexts = 0;
        int countUnexpectecDifferenceInTexts = 0;

        int size = resultTextCompares.size();


        for (int i = 0; i < size; i++)

        {

            if (!resultTextCompares.get(i).expected.getValue().equals(resultTextCompares.get(i).actual.getValue())) {
                countDifferenceInTexts++;
                boolean found = false;
                int index = findTextLinesByFilter(filters, resultTextCompares.get(i));
                //if index > 0, it indicates the difference is an expected oone
                if (index >= 0) {
                    //it means this line should be ignored
                    if (filters.get(index).getReplacement() == null) {
                        logger.info("due to no expected value, the difference in value is ignored : " + resultTextCompares.get(i).expected.getValue() + " | " + resultTextCompares.get(i).actual.getValue());
                        countIgnoredDifferenceInTexts++;
                        found = true;
                    } else {
                        //it means this value of the line euals replacment
                        if (filters.get(index).getReplacement().equals(resultTextCompares.get(i).actual.getValue())) {
                            //it means this line should be ignored; the replacement is identical to the actual value
                            logger.info("due to replacement value equals actual value, the difference in value is ignored : " + resultTextCompares.get(i).expected.getValue() + " | " + resultTextCompares.get(i).actual.getValue());
                            countIgnoredDifferenceInTexts++;
                            found = true;
                        } else {
                            //it means this value of the line doesnot equal replacment
                            resultTextCompares = logDifference("there is one line not having the same text value as expected ", resultTextCompares, i, "unexpected Text content found");
                            found = true;
                            countUnexpectecDifferenceInTexts++;
                        }
                    }
                } else {
                    //it means , there is a difference in the two lines conent, however, the filter  doesnot expect this difference. thus ,it's a new bug!
                    resultTextCompares = logDifference("there is one line not having not been mentioned in the template ", resultTextCompares, i, "unexpected Text content found");
                    countUnexpectecDifferenceInTexts++;
                    found = true;
                }
            }

        }

        return resultTextCompares;
    }


    public List<ResultTextCompare> comparePDFs(String expectedPath, String actualPath, String ignoreJson) throws Exception {
        return comparePDFs(expectedPath, actualPath, ignoreJson, true, true, true);
    }

    //visual compare; text content compare; text color compare
    public List<ResultTextCompare> comparePDFs(String expectedPdfFullpath, String actuaPDFFulllPath, String ignoreJson, boolean visualCompare, boolean textCompare, boolean textColorCompare) throws Exception {


        List<ResultTextCompare> result = new ArrayList<ResultTextCompare>();

        List<KFilter> filtersOnContext = generateFilters(ignoreJson);

        //get all the text info
        PDDocument pdDocActual = getPDDocument(actuaPDFFulllPath);
        PDDocument pdDocExpected = getPDDocument(expectedPdfFullpath);
        KPDFTextStripper stripperActual = new KPDFTextStripper();
        stripperActual.getText(pdDocActual);
        KPDFTextStripper stripperExpected = new KPDFTextStripper();
        stripperExpected.getText(pdDocExpected);


        if (textCompare) {
            result = comparePdfByText(stripperExpected, stripperActual, filtersOnContext);
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

            ResultTextCompare resultTextCompare = new ResultTextCompare();
            resultTextCompare.actual = null;
            resultTextCompare.expected = null;
            resultTextCompare.reason = String.valueOf(comparePDFsVisually(pdDocExpected, pdDocActual));
            result.add(resultTextCompare);

//            result.add(new ResultTextCompare{"dfsf",expec});
        }
        pdDocExpected.close();
        pdDocActual.close();
        return result;
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

        int coutnNotSame = 0;
        for (int i = 0; i < jpgPathNameToSaveActuals.size(); i++) {

            ImageComparison imageComparison = new ImageComparison(10, 10, 0.05);
            result = (imageComparison.fuzzyEqual(jpgPathNameToSaveExpecteds.get(i), jpgPathNameToSaveActuals.get(i), jpagPathNameResult + String.valueOf(i) + ".jpg"));
            if(!result) coutnNotSame++;
            logger.info(jpgPathNameToSaveExpecteds.get(i) + " is the same as " + jpgPathNameToSaveActuals.get(i) + " : " + result);

        }
        if(coutnNotSame>0)return  false;
        return true;
    }

    public PDDocument getPDDocument(String fileName) throws IOException {
        PDFParser parser = null;
        COSDocument cosDoc = null;
        String parsedText = "";
        File file = new File(fileName);


        try {
            parser = new PDFParser(new RandomAccessFile(file, "r"));
        } catch (FileNotFoundException e) {
            return null;
        }
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


    //according to the input json String to generate a list of KFilter objects
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

        contentStream.setNonStrokingColor(Color.white);

        for (KFilter filter : filtersToExcludeRectangles) {
            int x = Math.round(Float.valueOf(filter.getStartX()));
            int y = Math.round(Float.valueOf(filter.getStartY()));
            int width = Math.round(Float.valueOf(filter.getWidth()));
            int height = Math.round(Float.valueOf(filter.getHeight()));
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            Rectangle rect = new Rectangle(x - 4, y - 4, width + 5, height + 5);
            stripper.addRegion("class1", rect);
            //   PDPage firstPage = document.getPage(0);
            stripper.extractRegions(page);
            System.out.println("Text in the area:" + rect);
            System.out.println(stripper.getGraphicsState().getStrokingColor());// "class1" ) );
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
