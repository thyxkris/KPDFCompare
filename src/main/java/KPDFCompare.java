
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by makri on 15/06/2017.
 */
public class KPDFCompare {

    final private String DIFFERENCE_IN_COLOR_RENDERING_MODE = "Color and Rendering Mode difference caught";
    final private String DifferenceInTextContent = "unexpected Text content found";
    final private String DifferenceInTextTolerancePossible = "Text differ but potential adjustment and tolerance";
    final private String DifferenceInTextNOWayTolerance = "Texts differ too much, NO adjustment or tolerance potential";

    private List<TextDifference> TextDifferencesBeforeProcessed = null;
    private List<TextDifference> TextDifferencesInColorRenderingMode = null;
    private List<TextDifference> TextDifferencesInCoordinateAlignment = null;
    private List<TextDifference> TextDifferencesInCoordinateDeviation = null;
    private List<KPDFTextInfo> filtersForVisualComparison = null;
    private List<KPDFTextInfo> filtersForTextComparison = null;
    private KPDFTextStripper kpdfTextStripperExpected = null;
    private KPDFTextStripper kpdfTextStripperActual = null;
    private PDDocument pdDocExpected = null;
    private PDDocument pdDocActual = null;

    private boolean isTextTolerant = false;

    private float thresholdStartX = 0.03f;
    private float thresholdHeight = 0;
    private float thresholdStartY = 0;
    private float thresholdWidth = 0.2f;
    private String visualCompareMethod = "fuzzy";
    private String workingDirectory = ConfigHelper.getTestResourcesFolderPath();
    private Logger logger = LogManager.getLogger();
    private boolean visualComparisonWithFilters = true;
    private List<TextDifference> TextDifferencesInTextContent = new ArrayList<TextDifference>();


    public KPDFCompare(PDDocument pdDocExpected, PDDocument pdDocActual) throws IOException {

        kpdfTextStripperExpected = new KPDFTextStripper();
        kpdfTextStripperExpected.getText(pdDocActual);
        kpdfTextStripperActual = new KPDFTextStripper();
        kpdfTextStripperActual.getText(pdDocExpected);
    }

    public KPDFCompare(String pdfExpected, String pdfActual) throws IOException {

        this.pdDocExpected = this.getPDDocument(pdfExpected);
        this.pdDocActual = this.getPDDocument(pdfActual);

    }
    public KPDFCompare( ) throws IOException {

    }

    public List<TextDifference> getTextDifferencesInTextContent() {
        return TextDifferencesInTextContent;
    }

    public List<TextDifference> getTextDifferencesBeforeProcessed() {
        return TextDifferencesBeforeProcessed;
    }

    public List<TextDifference> getTextDifferencesInColorRenderingMode() {
        return TextDifferencesInColorRenderingMode;
    }

    public List<TextDifference> getTextDifferencesInCoordinateAlignment() {
        return TextDifferencesInCoordinateAlignment;
    }

    public List<TextDifference> getTextDifferencesInCoordinateDeviation() {
        return TextDifferencesInCoordinateDeviation;
    }


    public float getThresholdStartX() {
        return thresholdStartX;

    }

    public KPDFCompare setThresholdStartX(float thresholdStartX) {
        this.thresholdStartX = thresholdStartX;
        return this;
    }

    public float getThresholdHeight() {
        return thresholdHeight;
    }

    public KPDFCompare setThresholdHeight(float thresholdHeight) {
        this.thresholdHeight = thresholdHeight;
        return this;
    }

    public float getThresholdStartY() {
        return thresholdStartY;
    }

    public KPDFCompare setThresholdStartY(float thresholdStartY) {
        this.thresholdStartY = thresholdStartY;
        return this;
    }

    public float getThresholdWidth() {
        return thresholdWidth;
    }

    public KPDFCompare setThresholdWidth(float thresholdWidth) {
        this.thresholdWidth = thresholdWidth;
        return this;
    }

    public KPDFTextStripper getKpdfTextStripperExpected() {
        return kpdfTextStripperExpected;
    }

    public KPDFTextStripper getKpdfTextStripperActual() {
        return kpdfTextStripperActual;
    }


    public PDDocument getPdDocExpected() {
        return pdDocExpected;
    }

    public KPDFCompare setPdDocActual(String pdfFile) throws IOException {
        return setPdDocActual(getPDDocument(pdfFile));

    }
    public KPDFCompare setPdDocExpected(PDDocument pdDocExpected) {
        this.pdDocExpected = pdDocExpected;
        return this;
    }
    public KPDFCompare setPdDocExpected(String pdfFile) throws IOException {
        return setPdDocExpected(getPDDocument(pdfFile));

    }

    public PDDocument getPdDocActual() {
        return pdDocActual;
    }

    public KPDFCompare setPdDocActual(PDDocument pdDocActual) {
        this.pdDocActual = pdDocActual;
        return this;
    }

    public String getVisualCompareMethod() {
        return visualCompareMethod;
    }

    public KPDFCompare setVisualCompareMethod(String visualCompareMethod) {
        this.visualCompareMethod = visualCompareMethod;
        return this;
    }

    public KPDFCompare findAllDifferencesInTexts() throws IOException {
        //  textDifferences = new ArrayList<TextDifference>();
        kpdfTextStripperExpected = new KPDFTextStripper();
        kpdfTextStripperExpected.getText(pdDocExpected);
        kpdfTextStripperActual = new KPDFTextStripper();
        kpdfTextStripperActual.getText(pdDocActual);

        if ((kpdfTextStripperActual == null) || (kpdfTextStripperActual == null))
            throw new IllegalStateException("please choose files to compare");

        TextDifferencesBeforeProcessed = new ArrayList<TextDifference>();

        String HeightNotSame = "HeightNotSame";
        String WidthNotSame = "WidthNotSame";
        String StartXNotSame = "StartXNotSame";
        String StartYNotSame = "StartYNotSame";
        String FontNotSame = "FontNotSame";
        String FontSizeNotSame = "FontSizeNotSame";
        String ValueNotSame = "ValueNotSame";
        String ColorRenderingModeNotSame = "ColorRenderingModeNotSame";

        //if it's passed above, continue to compare other parts
        for (int i = 0; i < Math.min(kpdfTextStripperExpected.getPdfTextInfos().size(), kpdfTextStripperActual.getPdfTextInfos().size()); i++) {
            boolean isSame = true;
            StringBuilder differenceTypes = new StringBuilder();
            if (kpdfTextStripperExpected.getPdfTextInfos().get(i).getHeight() != kpdfTextStripperActual.getPdfTextInfos().get(i).getHeight()) {
                isSame = false;
                differenceTypes.append(" " + HeightNotSame);// = false;
            }
            if (kpdfTextStripperExpected.getPdfTextInfos().get(i).getWidth() != kpdfTextStripperActual.getPdfTextInfos().get(i).getWidth()) {
                isSame = false;
                differenceTypes.append(" " + WidthNotSame);// = false;++;//
            }
            if (kpdfTextStripperExpected.getPdfTextInfos().get(i).getStartX() != kpdfTextStripperActual.getPdfTextInfos().get(i).getStartX()) {
                if ((0.0 == kpdfTextStripperActual.getPdfTextInfos().get(i).getStartX()) && (kpdfTextStripperExpected.getPdfTextInfos().get(i).getWidth() == kpdfTextStripperActual.getPdfTextInfos().get(i).getWidth())) {
                    //it's due to the bug of pdf box libraries, it should be ignored
                    kpdfTextStripperActual.getPdfTextInfos().get(i).setStartX(kpdfTextStripperExpected.getPdfTextInfos().get(i).getStartX());
                } else {
                    isSame = false;
                    differenceTypes.append(" " + StartXNotSame);// = false; = false;
                }
            }
            if (kpdfTextStripperExpected.getPdfTextInfos().get(i).getStartY() != kpdfTextStripperActual.getPdfTextInfos().get(i).getStartY()) {
                isSame = false;
                differenceTypes.append(" " + StartYNotSame);// = false;lse;
            }
            if (!kpdfTextStripperExpected.getPdfTextInfos().get(i).getFont().equals(kpdfTextStripperActual.getPdfTextInfos().get(i).getFont())) {
                isSame = false;
                differenceTypes.append(" " + FontNotSame);// = false;lse;
            }
            if (kpdfTextStripperExpected.getPdfTextInfos().get(i).getFontSize() != kpdfTextStripperActual.getPdfTextInfos().get(i).getFontSize()) {
                isSame = false;
                differenceTypes.append(" " + FontSizeNotSame);// = false;lse;
            }
            if (!kpdfTextStripperExpected.getPdfTextInfos().get(i).getValue().equals(kpdfTextStripperActual.getPdfTextInfos().get(i).getValue())) {

                //font, fontsize, startX, startY, width, height are all the same
                int length = kpdfTextStripperActual.getPdfTextInfos().get(i).getValue().length();
                if (isSame && (kpdfTextStripperActual.getPdfTextInfos().get(i).getValue().substring(length - 1, length).equals(" "))) {
                    //it means the difference is caused by a " "'
                    kpdfTextStripperActual.getPdfTextInfos().get(i).setValue(kpdfTextStripperExpected.getPdfTextInfos().get(i).getValue());
                } else {

                    isSame = false;
                    differenceTypes.append(" " + ValueNotSame);// = false;lse;}
                }
            }
            if (!kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap().equals(kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap())) {
                isSame = false;
                differenceTypes.append(" " + ColorRenderingModeNotSame);// = false;lse;
            }

            if (!isSame) {

                String message = "line " + Integer.toString(i) + " differ due to " + differenceTypes.toString() + " : expected | actual";
                TextDifference textDifference = new TextDifference();
                textDifference.expected.setValue(kpdfTextStripperExpected.getPdfTextInfos().get(i).getValue());
                textDifference.expected.setStartX(String.valueOf(kpdfTextStripperExpected.getPdfTextInfos().get(i).getStartX()));
                textDifference.expected.setStartY(String.valueOf(kpdfTextStripperExpected.getPdfTextInfos().get(i).getStartY()));
                textDifference.expected.setWidth(String.valueOf(kpdfTextStripperExpected.getPdfTextInfos().get(i).getWidth()));
                textDifference.expected.setHeight(String.valueOf(kpdfTextStripperExpected.getPdfTextInfos().get(i).getHeight()));
                textDifference.expected.setFont(kpdfTextStripperExpected.getPdfTextInfos().get(i).getFont());
                textDifference.expected.setFontSize(String.valueOf(kpdfTextStripperExpected.getPdfTextInfos().get(i).getFontSize()));
                textDifference.expected.setIndex(String.valueOf(i));
                textDifference.expected.setGraphicsStateHashMap(kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap());
                textDifference.expected.setPageNumber(kpdfTextStripperExpected.getPdfTextInfos().get(i).getPageNumber());


                textDifference.actual.setIndex(String.valueOf(i));
                textDifference.actual.setValue(kpdfTextStripperActual.getPdfTextInfos().get(i).getValue());
                textDifference.actual.setStartX(String.valueOf(kpdfTextStripperActual.getPdfTextInfos().get(i).getStartX()));
                textDifference.actual.setStartY(String.valueOf(kpdfTextStripperActual.getPdfTextInfos().get(i).getStartY()));
                textDifference.actual.setWidth(String.valueOf(kpdfTextStripperActual.getPdfTextInfos().get(i).getWidth()));
                textDifference.actual.setHeight(String.valueOf(kpdfTextStripperActual.getPdfTextInfos().get(i).getHeight()));
                textDifference.actual.setFont(kpdfTextStripperActual.getPdfTextInfos().get(i).getFont());
                textDifference.actual.setFontSize(String.valueOf(kpdfTextStripperActual.getPdfTextInfos().get(i).getFontSize()));
                textDifference.actual.setGraphicsStateHashMap(kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap());
                textDifference.actual.setPageNumber(kpdfTextStripperActual.getPdfTextInfos().get(i).getPageNumber());

                String reason = DifferenceInTextTolerancePossible;
                TextDifferencesBeforeProcessed.add(textDifference);

                logDifference(message, TextDifferencesBeforeProcessed, null, reason);
            }
        }

        if (Math.abs(kpdfTextStripperExpected.getPdfTextInfos().size() - kpdfTextStripperActual.getPdfTextInfos().size()) > 0) {
            logger.info("comparison stops since the difference is too apparent");
            if (kpdfTextStripperExpected.getPdfTextInfos().size() > kpdfTextStripperActual.getPdfTextInfos().size()) {
                for (int i = Math.min(kpdfTextStripperExpected.getPdfTextInfos().size(), kpdfTextStripperActual.getPdfTextInfos().size()); i < kpdfTextStripperExpected.getPdfTextInfos().size(); i++) {
                    TextDifference textDifference = new TextDifference();

                    textDifference.expected.setValue(kpdfTextStripperExpected.getPdfTextInfos().get(i).getValue());
                    textDifference.expected.setStartX(String.valueOf(kpdfTextStripperExpected.getPdfTextInfos().get(i).getStartX()));
                    textDifference.expected.setStartY(String.valueOf(kpdfTextStripperExpected.getPdfTextInfos().get(i).getStartY()));
                    textDifference.expected.setWidth(String.valueOf(kpdfTextStripperExpected.getPdfTextInfos().get(i).getWidth()));
                    textDifference.expected.setHeight(String.valueOf(kpdfTextStripperExpected.getPdfTextInfos().get(i).getHeight()));
                    textDifference.expected.setFont(kpdfTextStripperExpected.getPdfTextInfos().get(i).getFont());
                    textDifference.expected.setFontSize(String.valueOf(kpdfTextStripperExpected.getPdfTextInfos().get(i).getFontSize()));
                    textDifference.expected.setIndex(String.valueOf(i));
                    textDifference.expected.setGraphicsStateHashMap(kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap());

                    textDifference.actual = null;
                    textDifference.reason = DifferenceInTextNOWayTolerance;
                    TextDifferencesBeforeProcessed.add(textDifference);
                }
            }

            if (kpdfTextStripperExpected.getPdfTextInfos().size() < kpdfTextStripperActual.getPdfTextInfos().size()) {
                for (int i = Math.min(kpdfTextStripperExpected.getPdfTextInfos().size(), kpdfTextStripperActual.getPdfTextInfos().size()); i < kpdfTextStripperActual.getPdfTextInfos().size(); i++) {

                    TextDifference textDifference = new TextDifference();
                    textDifference.actual.setIndex(String.valueOf(i));
                    textDifference.actual.setValue(kpdfTextStripperActual.getPdfTextInfos().get(i).getValue());
                    textDifference.actual.setStartX(String.valueOf(kpdfTextStripperActual.getPdfTextInfos().get(i).getStartX()));
                    textDifference.actual.setStartY(String.valueOf(kpdfTextStripperActual.getPdfTextInfos().get(i).getStartY()));
                    textDifference.actual.setWidth(String.valueOf(kpdfTextStripperActual.getPdfTextInfos().get(i).getWidth()));
                    textDifference.actual.setHeight(String.valueOf(kpdfTextStripperActual.getPdfTextInfos().get(i).getHeight()));
                    textDifference.actual.setFont(kpdfTextStripperActual.getPdfTextInfos().get(i).getFont());
                    textDifference.actual.setFontSize(String.valueOf(kpdfTextStripperActual.getPdfTextInfos().get(i).getFontSize()));
                    textDifference.actual.setGraphicsStateHashMap(kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap());

                    textDifference.expected = null;
                    textDifference.reason = DifferenceInTextNOWayTolerance;
                    TextDifferencesBeforeProcessed.add(textDifference);
                }
            }
        }
        logger.info(kpdfTextStripperExpected.getPdfTextInfos().size());
        logger.info(kpdfTextStripperActual.getPdfTextInfos().size());
        return this;

    }

    public KPDFCompare processDifferencesInCoordinateWithTolerance() throws IOException {
        return this.processDifferencesInCoordinateWithTolerance("x").processDifferencesInCoordinateWithTolerance("y");

    }

    public KPDFCompare processDifferencesInCoordinateWithTolerance(String coordinate) throws IOException {

        if (!coordinate.toLowerCase().equals("x") && !coordinate.toLowerCase().equals("y"))
            throw new IllegalArgumentException("coordinate can be only x or y");

        if (!checkProcessableWithTolarance())
            return this;

        if (TextDifferencesBeforeProcessed == null)
            throw new IllegalStateException("please run findAllDifferencesInTexts First");

        if (null == TextDifferencesInCoordinateAlignment) {
            TextDifferencesInCoordinateAlignment = new ArrayList<TextDifference>();
        }

        List<Float> coordinateListActual = new ArrayList<Float>();
        List<Float> cooridnateListExpected = new ArrayList<Float>();

        for (int i = 0; i < TextDifferencesBeforeProcessed.size(); i++) {
            coordinateListActual.add(TextDifferencesBeforeProcessed.get(i).expected.getCoordinateOrWitdhOrHeight(coordinate));
        }
        for (int i = 0; i < TextDifferencesBeforeProcessed.size(); i++) {
            cooridnateListExpected.add(TextDifferencesBeforeProcessed.get(i).actual.getCoordinateOrWitdhOrHeight(coordinate));
        }

        for (int i = 0; i < cooridnateListExpected.size(); i++) {
            if (coordinateListActual.get(i) != cooridnateListExpected.get(i)) {
                System.out.println(coordinateListActual.get(i));
                System.out.println(cooridnateListExpected.get(i));
                coordinateListActual.remove(i);//, (float) -1);
                cooridnateListExpected.remove(i);//, (float) -1);
            } else {
                boolean foundMatch = false;
                for (int j = i + 1; j < cooridnateListExpected.size(); j++) {
                    if (cooridnateListExpected.get(j).equals(cooridnateListExpected.get(i))) {
                        foundMatch = true;
                        //label it to avoid loop again
                        coordinateListActual.remove(j);//, (float) -1);
                        cooridnateListExpected.remove(j);//, (float) -1);

                        if (coordinateListActual.get(j).equals(coordinateListActual.get(i))) {
                            //the deviation in x alignment is ignored
                            logger.info("the difference in " + coordinate + " is ignored after adjustment of alignment of " + coordinate + " : " + cooridnateListExpected.get(j) + " | " + cooridnateListExpected.get(j));

                        } else {
//                        foundMatch = false;
//                        coordinateListActual.remove(j);//, (float) -1);
//                        cooridnateListExpected.remove(j);//, (f
                            String reason = coordinate + " cordinate is not aligned after ajustment";
                            String message = "there is one spot not aligned between expected pdf and actual pdf, even after adjustment , expected:" + cooridnateListExpected.get(j) + " != " + coordinateListActual.get(j);
                            logDifference(message, TextDifferencesBeforeProcessed, j, reason, TextDifferencesInCoordinateAlignment);
                        }
                    }
                    if (!foundMatch) {

                        coordinateListActual.remove(i);//, (float) -1);
                        cooridnateListExpected.remove(i);//, (float) -1);

                        //rule out the possiblity of differences in color and rendering mode

                        //so if one deviation in X cannot find at least one pair, then computer has no idea if it's right or wrong, in this case , will output wrong anyway
                        TextDifference textDifference = new TextDifference();
                        textDifference.expected = TextDifferencesBeforeProcessed.get(i).expected;
                        textDifference.actual = TextDifferencesBeforeProcessed.get(i).actual;
                        textDifference.reason = coordinate + " cordinate is not aligned after ajustment";
                        String message = "this deviation happens only once on the " + coordinate + " coordinate, so we are not sure if it's made this way on purpose";
                        logDifference(message, TextDifferencesInCoordinateAlignment, textDifference);

                    }

                }
            }


        }
        return this;
    }

    public KPDFCompare processDifferencesInDeviationWithTolerance() {
        return this.processDifferencesInDeviationWithTolerance("X", this.thresholdStartX)
                .processDifferencesInDeviationWithTolerance("Y", this.thresholdStartY)
                .processDifferencesInDeviationWithTolerance("height", this.thresholdHeight)
                .processDifferencesInDeviationWithTolerance("width", this.thresholdWidth);

    }

    public KPDFCompare processDifferencesInDeviationWithTolerance(String deviationType, Float threshold) {

        if (!checkProcessableWithTolarance())
            return this;
        if (TextDifferencesBeforeProcessed == null)
            throw new IllegalStateException("please run findAllDifferencesInTexts First");

        if (!deviationType.toLowerCase().equals("x") && !deviationType.toLowerCase().equals("y") && !deviationType.toLowerCase().equals("width") && !deviationType.toLowerCase().equals("height"))
            throw new IllegalArgumentException("coordinate can be only x or y or width or height");

        if (null == TextDifferencesInCoordinateDeviation)
            TextDifferencesInCoordinateDeviation = new ArrayList<TextDifference>();

        List<Float> coordinateListActual = new ArrayList<Float>();
        List<Float> cooridnateListExpected = new ArrayList<Float>();

        for (int i = 0; i < TextDifferencesBeforeProcessed.size(); i++) {
            coordinateListActual.add(TextDifferencesBeforeProcessed.get(i).expected.getCoordinateOrWitdhOrHeight(deviationType));
        }

        for (int i = 0; i < TextDifferencesBeforeProcessed.size(); i++) {
            cooridnateListExpected.add(TextDifferencesBeforeProcessed.get(i).actual.getCoordinateOrWitdhOrHeight(deviationType));
        }

        String message = null;
        Float deviation = Float.valueOf(0);
        for (int i = 0; i < cooridnateListExpected.size(); i++) {

            deviation = Math.abs(coordinateListActual.get(i) - cooridnateListExpected.get(i)) / Math.max(coordinateListActual.get(i), cooridnateListExpected.get(i));
            message = "the difference in " + deviationType.toUpperCase() + " is too big : " + cooridnateListExpected.get(i) + " | " + coordinateListActual.get(i);
            if (deviation != 0) {
                if (deviation > threshold) {
                    String reason = "deviation in " + deviationType.toUpperCase() + " coordinate is out of the tolerance region";
                    logDifference(message, TextDifferencesBeforeProcessed, i, reason, TextDifferencesInCoordinateDeviation);
                } else {
                }
            }


        }
        return this;
    }

    public boolean checkProcessableWithTolarance() {
        //too many differences, and impossible to be the same in a tolerantable way as there are difference lines in tow pdf

        if (null == TextDifferencesBeforeProcessed)
            throw new IllegalStateException("please run finAllTextDifferences method first");
        return ((TextDifferencesBeforeProcessed.get(TextDifferencesBeforeProcessed.size() - 1).actual != null) && (TextDifferencesBeforeProcessed.get(TextDifferencesBeforeProcessed.size() - 1).expected != null));
    }

    public KPDFCompare findDifferencesInColorRenderingMode() throws IOException {

        kpdfTextStripperExpected = new KPDFTextStripper();
        kpdfTextStripperExpected.getText(pdDocExpected);
        kpdfTextStripperActual = new KPDFTextStripper();
        kpdfTextStripperActual.getText(pdDocActual);

        //if(!checkProcessableWithTolarance())return this;
        if ((kpdfTextStripperExpected == null) || (kpdfTextStripperActual == null))
            throw new IllegalStateException("please choose files to compare First");

        TextDifferencesInColorRenderingMode = new ArrayList<TextDifference>();

        for (int i = 0; i < kpdfTextStripperExpected.getPdfTextInfos().size(); i++) {
            boolean sameColor = true;
            if (!kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap().equals(kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap())) {
                sameColor = false;
                logger.info(kpdfTextStripperExpected.getPdfTextInfos().get(i).getValue());
            }
            if (!sameColor) {
                TextDifference textDifference = new TextDifference();
                textDifference.expected.setGraphicsStateHashMap(kpdfTextStripperExpected.getPdfTextInfos().get(i).getGraphicsStateHashMap());
                textDifference.expected.setValue(kpdfTextStripperExpected.getPdfTextInfos().get(i).getValue());
                textDifference.expected.setValue(kpdfTextStripperExpected.getPdfTextInfos().get(i).getValue());

                textDifference.actual.setValue(kpdfTextStripperActual.getPdfTextInfos().get(i).getValue());
                textDifference.actual.setGraphicsStateHashMap(kpdfTextStripperActual.getPdfTextInfos().get(i).getGraphicsStateHashMap());
                textDifference.actual.setValue(kpdfTextStripperActual.getPdfTextInfos().get(i).getValue());

                textDifference.reason = DIFFERENCE_IN_COLOR_RENDERING_MODE;
                logDifference(textDifference.reason, TextDifferencesInColorRenderingMode, textDifference);
            }
        }
        return this;
    }

    public KPDFCompare processDifferencesInTextContentsWithExpectations() {

        if (!checkProcessableWithTolarance()) return this;
        if (TextDifferencesBeforeProcessed == null)
            throw new IllegalStateException("please run findAllDifferencesInTexts First");

        int size = TextDifferencesBeforeProcessed.size();
        TextDifferencesInTextContent = new ArrayList<TextDifference>();

        for (int i = 0; i < size; i++) {
            if (!TextDifferencesBeforeProcessed.get(i).expected.getValue().equals(TextDifferencesBeforeProcessed.get(i).actual.getValue())) {
                int index = findTextLinesByFilter(filtersForTextComparison, TextDifferencesBeforeProcessed.get(i));
                //if index > 0, it indicates the difference is an expected oone
                if (index >= 0) {
                    //it means this line should be ignored
                    if (filtersForTextComparison.get(index).getReplacement() == null) {
                        logger.info("due to no expected value, the difference in value is ignored : " + TextDifferencesBeforeProcessed.get(i).expected.getValue() + " | " + TextDifferencesBeforeProcessed.get(i).actual.getValue());
                    } else {
                        //it means this value of the line euals replacment
                        if (filtersForTextComparison.get(index).getReplacement().equals(TextDifferencesBeforeProcessed.get(i).actual.getValue())) {
                            //it means this line should be ignored; the replacement is identical to the actual value
                            logger.info("due to replacement value equals actual value, the difference in value is ignored : " + TextDifferencesBeforeProcessed.get(i).expected.getValue() + " | " + TextDifferencesBeforeProcessed.get(i).actual.getValue());
                        } else {
                            //it means this value of the line doesnot equal replacment
                            logDifference("there is one line not having the same text value as expected ", TextDifferencesBeforeProcessed, i, DifferenceInTextContent, TextDifferencesInTextContent);
                        }
                    }
                } else {
                    //it means , there is a difference in the two lines conent, however, the filter  doesnot expect this difference. thus ,it's a new bug!
                    logDifference("there is one line differences having not been mentioned in the template ", TextDifferencesBeforeProcessed, i, DifferenceInTextContent, TextDifferencesInTextContent);
                }
            }

        }

        return this;
    }

    //visual compare; text content compare; text color compare
    public KPDFCompare withTextDifferencesIgnored() throws Exception {

        if (null == TextDifferencesBeforeProcessed)
            throw new IllegalStateException("please run findAllTextDifferences first");

        isVisuallyTolerant = true;

        filtersForVisualComparison = new ArrayList<KPDFTextInfo>();

        if (this.TextDifferencesBeforeProcessed.size() > 0) {

            for (int i = 0; i < this.TextDifferencesBeforeProcessed.size(); i++) {
                KPDFTextInfo KPDFTextInfo = new KPDFTextInfo();
                float startX = Math.min(TextDifferencesBeforeProcessed.get(i).actual.getStartX(), TextDifferencesBeforeProcessed.get(i).expected.getStartX());
                KPDFTextInfo.setStartX(String.valueOf(startX));
                float startY = Math.min(TextDifferencesBeforeProcessed.get(i).actual.getStartY(), TextDifferencesBeforeProcessed.get(i).expected.getStartY());
                KPDFTextInfo.setStartY(String.valueOf(startY));
                float maxHeight = Math.max(TextDifferencesBeforeProcessed.get(i).actual.getHeight() + TextDifferencesBeforeProcessed.get(i).actual.getStartY(), TextDifferencesBeforeProcessed.get(i).expected.getHeight() + TextDifferencesBeforeProcessed.get(i).expected.getStartY()) - startY;
                KPDFTextInfo.setHeight(String.valueOf(maxHeight));
                float maxWidth = Math.max(TextDifferencesBeforeProcessed.get(i).actual.getWidth() + TextDifferencesBeforeProcessed.get(i).actual.getStartX(), TextDifferencesBeforeProcessed.get(i).expected.getWidth() + TextDifferencesBeforeProcessed.get(i).expected.getStartX()) - startX;
                KPDFTextInfo.setWidth(String.valueOf(maxWidth));
                KPDFTextInfo.setPageNumber(TextDifferencesBeforeProcessed.get(i).expected.getPageNumber());
                filtersForVisualComparison.add(KPDFTextInfo);
            }
        }

        return this;

    }

    public KPDFCompare withAreasIgnoredByJsonFilters(String jsonString) throws Exception {

        isVisuallyTolerant = true;
        filtersForVisualComparison = this.generateFilters(jsonString);

        return this;

    }

    private boolean isVisuallyTolerant = false;

    public boolean findVisualDifferences() throws Exception {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String expectedPdf = "expected" + dateFormat.format(new Date()) + ".pdf";
        String actualPdf = "actual" + dateFormat.format(new Date()) + ".pdf";
        File fileExpected = new File(workingDirectory + expectedPdf);
        File fileActual = new File(workingDirectory + actualPdf);
        String jpgPathNameToSaveExpected = workingDirectory + File.separator + "expected" + dateFormat.format(new Date());
        String jpgPathNameToSaveActual = workingDirectory + File.separator + "actual" + dateFormat.format(new Date());
        String jpagPathNameResult = workingDirectory + File.separator + "result" + dateFormat.format(new Date());


        if (this.isVisuallyTolerant) {
            addRectanglesToPDDocument(this.pdDocActual);//, withTextDifferencesIgnored(KPDFTextInfos));
            addRectanglesToPDDocument(this.pdDocExpected);//, withTextDifferencesIgnored(KPDFTextInfos));
        }

        List<String> jpgPathNameToSaveActuals = pdfToImages(pdDocActual, jpgPathNameToSaveActual);
        List<String> jpgPathNameToSaveExpecteds = pdfToImages(pdDocExpected, jpgPathNameToSaveExpected);

        //compare pixels
        boolean result = jpgPathNameToSaveActuals.size() == jpgPathNameToSaveExpecteds.size();

        int coutnNotSame = 0;
        for (int i = 0; i < jpgPathNameToSaveActuals.size(); i++) {

            if (this.visualCompareMethod.toLowerCase().contains("fuzzy")) {
                ImageComparison imageComparison = new ImageComparison(10, 10, 0.05);
                result = (imageComparison.fuzzyEqual(jpgPathNameToSaveExpecteds.get(i), jpgPathNameToSaveActuals.get(i), jpagPathNameResult + String.valueOf(i) + ".jpg"));
            }
            if (!result) coutnNotSame++;
            logger.info(jpgPathNameToSaveExpecteds.get(i) + " is the same as " + jpgPathNameToSaveActuals.get(i) + " : " + result);
        }
        if (coutnNotSame > 0) return false;
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

    public List<String> pdfToImages(PDDocument pdDocument, String jpgPathNameToSaveNoSuffix) throws IOException {

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

    public List<KPDFTextInfo> getFiltersForTextComparison() {
        return filtersForTextComparison;
    }


    public KPDFCompare withTextDifferencesIgnored(String configJson) throws IOException {
        this.isTextTolerant = true;
        this.filtersForTextComparison = this.generateFilters(configJson);
        return this;
    }

    //according to the input json String to generate a list of KPDFTextInfo objects
    public List<KPDFTextInfo> generateFilters(String configJson) throws IOException {

        List<KPDFTextInfo> filters = new ArrayList<KPDFTextInfo>();

        if (configJson == null)
            return filters;
        // List<KPDFTextInfo> filtersForTextComparison = new ArrayList<KPDFTextInfo>();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> map = mapper.readValue(configJson, Map.class);

        JsonNode rootNode = mapper.readTree(configJson);
        for (JsonNode jsonNode : rootNode.get("filter")) {
            KPDFTextInfo filter = new KPDFTextInfo();
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
            if ((jsonNode.get("pageNumber") != null) && !jsonNode.get("pageNumber").equals("")) {
                filter.setPageNumber(jsonNode.get("pageNumber").textValue());
            }

            filters.add(filter);

        }
        return filters;
    }

    private PDDocument addRectanglesToPDDocument(PDDocument whichPDDocumentToProcess) throws IOException {

        if (null == this.filtersForVisualComparison)
            return whichPDDocumentToProcess;

        for (int pageIndex = 0; pageIndex < whichPDDocumentToProcess.getNumberOfPages(); pageIndex++) {
            PDPage page = whichPDDocumentToProcess.getPage(pageIndex);
            PDPageContentStream contentStream = new PDPageContentStream(whichPDDocumentToProcess, page, true, true);

            //Setting the non stroking color
            contentStream.setNonStrokingColor(Color.white);

            for (KPDFTextInfo filter : filtersForVisualComparison) {
                if (filter.getPageNumber() == pageIndex + 1) {
                    int x = Math.round(Float.valueOf(filter.getStartX()));
                    int y = Math.round(Float.valueOf(filter.getStartY()));
                    int width = Math.round(Float.valueOf(filter.getWidth()));
                    int height = Math.round(Float.valueOf(filter.getHeight()));
                    Rectangle rect = new Rectangle(x - 4, y - 4, width + 5, height + 5);
                    System.out.println("Text in the area:" + rect);
                    contentStream.addRect(x - 4, y - 4, width + 5, height + 5);//9091335
                }
            }
            contentStream.fill();
            System.out.println("rectangle added");
            //Closing the ContentStream object
            contentStream.close();

        }
        return whichPDDocumentToProcess;
    }

    private int findTextLinesByFilter(List<KPDFTextInfo> filters, TextDifference textDifference) {

        if ((null != filters) && (filters.size() != 1)) {

            for (int i = 0; i < filters.size(); i++) {
                boolean found = true;
                if ((null != filters.get(i).getFont()) && !textDifference.expected.getFont().equals(filters.get(i).getFont())) {
                    found = false;
                }
                if ((filters.get(i).getFontSize() >= 0) && (textDifference.expected.getFontSize() != (filters.get(i).getFontSize()))) {
                    found = false;
                }
                if ((null != filters.get(i).getValue()) && !textDifference.expected.getValue().equals(filters.get(i).getValue())) {
                    found = false;
                }
                if ((filters.get(i).getStartX() >= 0) && (textDifference.expected.getStartX() != (filters.get(i).getStartX()))) {
                    found = false;
                }
                if ((filters.get(i).getStartY() >= 0) && (textDifference.expected.getStartY() != (filters.get(i).getStartY()))) {
                    found = false;
                }

                if (found) {
                    return i;
                }
            }
        }
        return -1;
    }

    private List<TextDifference> logDifference(String message, List<TextDifference> textDifferences, Integer index, String reason) {

        //log the futher-analysed result from already-kwown textDifferences to check if they are tolerated, and if not, will log the reason/resultTextCompare/Index
        //if index is not provided, will assuming an adding of resultTextCompare so the new textDifferences will be appended directly
        if (null == index) {
            //only print the log info
            index = textDifferences.size() - 1;

        } else {
            //otherwise, add
            TextDifference textDifference = new TextDifference();
            textDifference.expected = textDifferences.get(index).expected;
            textDifference.actual = textDifferences.get(index).actual;
            textDifference.reason = reason;
            textDifferences.add(textDifference);
        }

        logger.info(message);
        logger.info("value: " + "|" + textDifferences.get(index).expected.getValue() + "|" + textDifferences.get(index).actual.getValue() + "|");
        logger.info("x " + "|" + textDifferences.get(index).expected.getStartX() + " | " + textDifferences.get(index).actual.getStartX());
        logger.info("y " + "|" + textDifferences.get(index).expected.getStartY() + " | " + textDifferences.get(index).actual.getStartY());
        logger.info("width " + "|" + textDifferences.get(index).expected.getWidth() + " | " + textDifferences.get(index).actual.getWidth());
        logger.info("height " + "|" + textDifferences.get(index).expected.getHeight() + " | " + textDifferences.get(index).actual.getHeight());
        logger.info("font " + "|" + textDifferences.get(index).expected.getFont() + " | " + textDifferences.get(index).actual.getFont());
        logger.info("font size : " + "|" + textDifferences.get(index).expected.getFontSize() + " | " + textDifferences.get(index).actual.getFontSize());
        logger.info("getStrokingColor : " + "|" + textDifferences.get(index).expected.getStrokingColor() + " | " + textDifferences.get(index).actual.getStrokingColor());
        logger.info("getNonStrokingColor : " + "|" + textDifferences.get(index).expected.getNonStrokingColor() + " | " + textDifferences.get(index).actual.getNonStrokingColor());
        logger.info("getRenderingMode : " + "|" + textDifferences.get(index).expected.getRenderingMode() + " | " + textDifferences.get(index).actual.getRenderingMode());


        return textDifferences;
    }

    private List<TextDifference> logDifference(String message, List<TextDifference> textDifferencesSource, Integer index, String reason, List<TextDifference> textDifferencesDest) {

        //log the futher-analysed result from already-kwown textDifferences to check if they are tolerated, and if not, will log the reason/resultTextCompare/Index
        //if index is not provided, will assuming an adding of resultTextCompare so the new textDifferences will be appended directly
        if (null == index) {
            //only print the log info
            index = textDifferencesSource.size() - 1;

        } else {
            //otherwise, add
            TextDifference textDifference = new TextDifference();
            textDifference.expected = textDifferencesSource.get(index).expected;
            textDifference.actual = textDifferencesSource.get(index).actual;
            textDifference.reason = reason;
            textDifferencesDest.add(textDifference);
        }

        logger.info(message);
        logger.info("value: " + "|" + textDifferencesSource.get(index).expected.getValue() + "|" + textDifferencesSource.get(index).actual.getValue() + "|");
        logger.info("x " + "|" + textDifferencesSource.get(index).expected.getStartX() + " | " + textDifferencesSource.get(index).actual.getStartX());
        logger.info("y " + "|" + textDifferencesSource.get(index).expected.getStartY() + " | " + textDifferencesSource.get(index).actual.getStartY());
        logger.info("width " + "|" + textDifferencesSource.get(index).expected.getWidth() + " | " + textDifferencesSource.get(index).actual.getWidth());
        logger.info("height " + "|" + textDifferencesSource.get(index).expected.getHeight() + " | " + textDifferencesSource.get(index).actual.getHeight());
        logger.info("font " + "|" + textDifferencesSource.get(index).expected.getFont() + " | " + textDifferencesSource.get(index).actual.getFont());
        logger.info("font size : " + "|" + textDifferencesSource.get(index).expected.getFontSize() + " | " + textDifferencesSource.get(index).actual.getFontSize());
        logger.info("getStrokingColor : " + "|" + textDifferencesSource.get(index).expected.getStrokingColor() + " | " + textDifferencesSource.get(index).actual.getStrokingColor());
        logger.info("getNonStrokingColor : " + "|" + textDifferencesSource.get(index).expected.getNonStrokingColor() + " | " + textDifferencesSource.get(index).actual.getNonStrokingColor());
        logger.info("getRenderingMode : " + "|" + textDifferencesSource.get(index).expected.getRenderingMode() + " | " + textDifferencesSource.get(index).actual.getRenderingMode());


        return textDifferencesDest;
    }

    private List<TextDifference> logDifference(String message, List<TextDifference> textDifferences, TextDifference textDifference) throws IOException {


        textDifferences.add(textDifference);
        int index = textDifferences.size() - 1;


        logger.info(message);
        logger.info("value: " + "|" + textDifferences.get(index).expected.getValue() + "|" + textDifferences.get(index).actual.getValue() + "|");
        logger.info("x " + "|" + textDifferences.get(index).expected.getStartX() + " | " + textDifferences.get(index).actual.getStartX());
        logger.info("y " + "|" + textDifferences.get(index).expected.getStartY() + " | " + textDifferences.get(index).actual.getStartY());
        logger.info("width " + "|" + textDifferences.get(index).expected.getWidth() + " | " + textDifferences.get(index).actual.getWidth());
        logger.info("height " + "|" + textDifferences.get(index).expected.getHeight() + " | " + textDifferences.get(index).actual.getHeight());
        logger.info("font " + "|" + textDifferences.get(index).expected.getFont() + " | " + textDifferences.get(index).actual.getFont());
        logger.info("font size : " + "|" + textDifferences.get(index).expected.getFontSize() + " | " + textDifferences.get(index).actual.getFontSize());

        logger.info("expected: ");
        for (Map.Entry<Integer, GraphicsState> entry : textDifferences.get(index).expected.getGraphicsStateHashMap().entrySet()) {
            logger.info("till " + entry.getKey() + " :" + "getRenderingMode : " + entry.getValue().getRenderingMode() + " getNonStrokingColor: " + entry.getValue().getNonStrokingColor().toRGB() + " geStrokingColor: " + entry.getValue().getStrokingColor().toRGB());
        }
        logger.info("actual: ");
        for (Map.Entry<Integer, GraphicsState> entry : textDifferences.get(index).actual.getGraphicsStateHashMap().entrySet()) {
            logger.info("till " + entry.getKey() + " :" + "getRenderingMode : " + entry.getValue().getRenderingMode() + " getNonStrokingColor: " + entry.getValue().getNonStrokingColor().toRGB() + " geStrokingColor: " + entry.getValue().getStrokingColor().toRGB());
        }

        return textDifferences;
    }

}

