import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by makri on 2/07/2017.
 */
public class KPDFCompareTest {


    KPDFCompare kpdfCompare;


    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void compareTwoImages() throws Exception {

    }
    @Test
    public void setThresholdStartXStartYToZero() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";


        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf");
        kpdfCompare.generateFilters(jsonStringWithPartlyReplacementExceptContactName);
        kpdfCompare.findAllDifferencesInTexts().setThresholdWidth(0.00f).setThresholdHeight(0.00f).setThresholdStartX(0.00f).setThresholdStartY(0.00f)
                .processDifferencesInTextContentsWithExpectations()
                .processDifferencesInCoordinateWithTolerance()
                .findDifferencesInColorRenderingMode()
                .processDifferencesInDeviationWithTolerance()
        ;

        assertEquals(0, kpdfCompare.getTextDifferencesInColorRenderingMode().size());
        assertEquals(0, kpdfCompare.getTextDifferencesInCoordinateAlignment().size());
        assertEquals(15, kpdfCompare.getTextDifferencesInCoordinateDeviation().size());

    }
    @Test
    public void setThresholdStartXStartYTo3percent() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";


        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf");
        kpdfCompare.generateFilters(jsonStringWithPartlyReplacementExceptContactName);
        kpdfCompare.findAllDifferencesInTexts().setThresholdWidth(0.03f).setThresholdHeight(0.01f).setThresholdStartX(0.01f).setThresholdStartY(0.03f)
                .processDifferencesInTextContentsWithExpectations()
                .processDifferencesInCoordinateWithTolerance()
                .findDifferencesInColorRenderingMode()
                .processDifferencesInDeviationWithTolerance()
        ;

        assertEquals(0.01f, kpdfCompare.getThresholdHeight(), 0.0f);
        assertEquals(0.01f, kpdfCompare.getThresholdStartX(),0.0f);
        assertEquals(0.03f, kpdfCompare.getThresholdWidth(),0.0f);
        assertEquals(0.03f, kpdfCompare.getThresholdStartY(),0.0f);
        assertEquals(0, kpdfCompare.getTextDifferencesInColorRenderingMode().size());
        assertEquals(0, kpdfCompare.getTextDifferencesInCoordinateAlignment().size());
        assertEquals(1, kpdfCompare.getTextDifferencesInCoordinateDeviation().size());

    }

    @Test
    public void test1() throws IOException {
        kpdfCompare = new KPDFCompare();
        kpdfCompare.setPdDocActual(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf");
        kpdfCompare.setPdDocExpected(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");

        assertNotNull(kpdfCompare.getPdDocExpected());
        assertNotNull(kpdfCompare.getPdDocActual());
        assertNull(kpdfCompare.getKpdfTextStripperActual());
        assertNull(kpdfCompare.getKpdfTextStripperExpected());

        kpdfCompare.findAllDifferencesInTexts();
        assertNotNull(kpdfCompare.getKpdfTextStripperActual());
        assertNotNull(kpdfCompare.getKpdfTextStripperExpected());

    }

    @Test
    public void comparePdfByTextContentWhen2FilesAreIdentical() throws Exception {
        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf");

        assertEquals(0, kpdfCompare.findAllDifferencesInTexts().getTextDifferencesBeforeProcessed().size());//, null, false, 0, 0, 0, 0));

        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{ \"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Tuesday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";

        assertEquals(0, kpdfCompare.findAllDifferencesInTexts().withTextDifferencesIgnored(jsonString).getTextDifferencesBeforeProcessed().size());//(kpdfTextStripper, kpdfTextStripper1, kpdfCompare.generateFilters(jsonString), false, 0, 0, 0, 0));


    }

    @Test
    public void comparePdfByTextWhenIdenticalFilesWithFilter() throws Exception {


        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf");
        assertEquals(0, kpdfCompare.withTextDifferencesIgnored(jsonString).findAllDifferencesInTexts().getTextDifferencesBeforeProcessed().size());//(kpdfTexpdfTextStripper, kpdfTextStripper1, kpdfCompare.generateFilters
    }

    @Test
    public void comparePdfByTextWhenTwoFilesHave17Differences() throws Exception {

        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");
        assertEquals(4, kpdfCompare.withTextDifferencesIgnored(jsonString).findAllDifferencesInTexts().getTextDifferencesBeforeProcessed().size());//(kpdfTexpdfTextStripper, kpdfTextStripper1, kpdfCompare.generateFilters
    }

    @Test
    public void checkIfAlignedInCooridnate() throws Exception {


        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";

        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");
        assertEquals(0, kpdfCompare.withTextDifferencesIgnored(jsonString).findAllDifferencesInTexts().processDifferencesInCoordinateWithTolerance("X").getTextDifferencesInCoordinateAlignment().size());//
        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");
        assertEquals(0, kpdfCompare.withTextDifferencesIgnored(jsonString).findAllDifferencesInTexts().processDifferencesInCoordinateWithTolerance("X").getTextDifferencesInCoordinateAlignment().size());//


    }

    @Test
    public void compareTextContentsWhen4DifferencesNoFilters() throws Exception {


        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");
        assertEquals(0, kpdfCompare.withTextDifferencesIgnored(jsonString).findAllDifferencesInTexts().processDifferencesInTextContentsWithExpectations().getTextDifferencesInTextContent().size());//

        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");
        assertEquals(1, kpdfCompare.withTextDifferencesIgnored(null).findAllDifferencesInTexts().processDifferencesInTextContentsWithExpectations().getTextDifferencesInTextContent().size());//


    }

    @Test
    public void compareTextContentsWhen32DifferencesWithFiltersNOReplacement() throws Exception {


        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";


        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");
        assertEquals(0, kpdfCompare.withTextDifferencesIgnored(jsonString).findAllDifferencesInTexts().processDifferencesInTextContentsWithExpectations().getTextDifferencesInTextContent().size());//


    }

    @Test
    public void compareTextContentsWhen32DifferencesWithFiltersWithCorrectReplacements() throws Exception {

        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu3X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";


        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");
        assertEquals(0, kpdfCompare.withTextDifferencesIgnored(jsonStringWithPartlyReplacementExceptContactName).findAllDifferencesInTexts().processDifferencesInTextContentsWithExpectations().getTextDifferencesInTextContent().size());//


    }

    @Test
    public void compareTextContentsWhen32DifferencesWithFiltersWithIncorrectReplacements() throws Exception {

        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";

        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");
        assertEquals(1, kpdfCompare.withTextDifferencesIgnored(jsonStringWithPartlyReplacementExceptContactName).findAllDifferencesInTexts().processDifferencesInTextContentsWithExpectations().getTextDifferencesInTextContent().size());//

    }

    @Test
    public void compareTextContentsWhe2DifferencesWithFiltersNoFilterOnDifference() throws Exception {

        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
       /* List<TextDifference> textDifferences = kpdfCompare.findAllDifferencesInTexts(kpdfTextStripper, kpdfTextStripper1);
        Integer countNOToleranceDifference = textDifferences.size();//
        textDifferences = kpdfCompare.processDifferencesInTextContentsWithExpectations(textDifferences, kpdfCompare.generateFilters(jsonStringWithPartlyReplacementExceptContactName));

        assertEquals(1, textDifferences.size() - countNOToleranceDifference);*/


        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");
        assertEquals(1, kpdfCompare.withTextDifferencesIgnored(jsonStringWithPartlyReplacementExceptContactName).findAllDifferencesInTexts().processDifferencesInTextContentsWithExpectations().getTextDifferencesInTextContent().size());//


    }

    @Test
    public void compareTextContentsWhenNoDifferencesInStrokingColorRenderingMode() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";

/*        PDDocument pdDocument3 = kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf");
        PDDocument pdDocument4 = kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf");
        KPDFTextStripper kpdfTextStripper3 = new KPDFTextStripper();
        kpdfTextStripper3.getText(pdDocument3);
        KPDFTextStripper kpdfTextStripper4 = new KPDFTextStripper();
        kpdfTextStripper4.getText(pdDocument4);
       // kpdfTextStripper.getText(pdDocument);
        List<TextDifference> textDifferences = kpdfCompare.findDifferencesInColorRenderingMode(kpdfTextStripper3, kpdfTextStripper4);

        assertEquals(0, textDifferences.size());// - countNOToleranceDifference);*/

        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf");
        assertEquals(0, kpdfCompare.withTextDifferencesIgnored(jsonStringWithPartlyReplacementExceptContactName).findDifferencesInColorRenderingMode().getTextDifferencesInColorRenderingMode().size());//

    }

    @Test
    public void compareTextContentsWhen1DifferencesInStrokingColorRenderingMode() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
//        List<TextDifference> textDifferences = kpdfCompare.findDifferencesInColorRenderingMode(kpdfTextStripper, kpdfTextStripperColor);
//
//        assertEquals(1, textDifferences.size());// - countNOToleranceDifference);


        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");
        assertEquals(1, kpdfCompare.withTextDifferencesIgnored(jsonStringWithPartlyReplacementExceptContactName).findDifferencesInColorRenderingMode().getTextDifferencesInColorRenderingMode().size());//


    }

    @Test
    public void comparePDFs1() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";


        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf");
        kpdfCompare.generateFilters(jsonStringWithPartlyReplacementExceptContactName);
        kpdfCompare.findAllDifferencesInTexts()
                .processDifferencesInTextContentsWithExpectations()
                .processDifferencesInCoordinateWithTolerance()
                .findDifferencesInColorRenderingMode()
                .processDifferencesInDeviationWithTolerance()
        ;

        assertEquals(0, kpdfCompare.getTextDifferencesInColorRenderingMode().size());
        assertEquals(0, kpdfCompare.getTextDifferencesInCoordinateAlignment().size());
        assertEquals(0, kpdfCompare.getTextDifferencesInCoordinateDeviation().size());


    }

    @Test
    public void comparePDFsVisually() throws Exception {
        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf");

        assertTrue(kpdfCompare.findAllDifferencesInTexts().withTextDifferencesIgnored().findVisualDifferences());
    }

    @Test
    public void comparePDFsVisually1NOToleranceThenThereIsDifference() throws Exception {
        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf");

        assertFalse(kpdfCompare.findAllDifferencesInTexts().findVisualDifferences());
    }

    @Test
    public void pdfToImage() throws Exception {
        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf");
        assertEquals(kpdfCompare.pdfToImages(kpdfCompare.getPdDocActual(), ConfigHelper.getCurrentWorkingDir() + File.separator + "getPDDocActual").size(),2);
      //  File file = new File(ConfigHelper.getCurrentWorkingDir() + File.separator + "getPDDocActual");
    }


    @Test
    public void generateFiltersTest() throws Exception {


        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf");

        String jsonString = "{\"filter\":[{ \"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Tuesday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KPDFTextInfo> KPDFTextInfos = kpdfCompare.generateFilters(jsonString);
        assertNull(KPDFTextInfos.get(0).getValue());
        assertNull(KPDFTextInfos.get(0).getFont());
        assertNull(KPDFTextInfos.get(0).getReplacement());
        assertTrue(KPDFTextInfos.get(0).getWidth() < 0);
        assertTrue(KPDFTextInfos.get(0).getHeight() < 0);
        assertTrue(KPDFTextInfos.get(0).getOffset() < 0);
        assertTrue(KPDFTextInfos.get(0).getStartX() < 0);
        assertTrue(KPDFTextInfos.get(0).getStartY() < 0);
        assertTrue(KPDFTextInfos.get(12).getIndex() < 0);
        assertEquals(15.0, KPDFTextInfos.get(0).getFontSize(), 0.00);
        assertEquals(KPDFTextInfos.get(1).getReplacement(), "Itinerary issued: Tuesday 27 June 2017");
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void wrongRunOders() throws Exception {

        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "testColor.pdf");

        kpdfCompare.processDifferencesInDeviationWithTolerance();
    }

    @Test
    public void wrongRunOders2() throws Exception {

        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "testColor.pdf");

        kpdfCompare.getFiltersForTextComparison();
    }

    @Test
    public void wrongRunOders3() throws Exception {

        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "testColor.pdf");

        kpdfCompare.findVisualDifferences();
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void wrongRunOders4() throws Exception {

        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "testColor.pdf");

        kpdfCompare.withTextDifferencesIgnored();
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void wrongRunOders_processDifferencesInCoordinateWithTolerance() throws Exception {

        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "testColor.pdf");

        kpdfCompare.processDifferencesInCoordinateWithTolerance();
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void wrongRunOders_processDifferencesInDeviationWithTolerance() throws Exception {

        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "testColor.pdf");

        kpdfCompare.processDifferencesInDeviationWithTolerance();
    }


    @Test
    public void comparedTextByColorRenderingModeTestWithTotallyDifferentColors() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";

        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "testColor.pdf");

        assertEquals(1, kpdfCompare.findDifferencesInColorRenderingMode().getTextDifferencesInColorRenderingMode().size());// - countNOToleranceDifference);
    }

    @Test
    public void comparedTextByColorRenderingModeTestWithSameColorsCountButStillDifferent() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");

        assertEquals(1, kpdfCompare.findDifferencesInColorRenderingMode().getTextDifferencesInColorRenderingMode().size());// - countNOToleranceDifference);
    }

    @Ignore
    @Test
    public void getPDDocumentTest() throws Exception {
        kpdfCompare = new KPDFCompare(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf");

        kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf");
        //  assertEquals((kpdfCompare.getPDDocument()).getClass(), PDDocument.class);
        assertNotNull(kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf"));
        assertNull(kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test400.pdf"));
    }

}