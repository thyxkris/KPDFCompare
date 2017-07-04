import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by makri on 2/07/2017.
 */
public class KPDFCompareTest {
    PDDocument pdDocument;
    PDDocument pdDocument1;
    KPDFCompare kpdfCompare;
    KPDFTextStripper kpdfTextStripper;
    KPDFTextStripper kpdfTextStripper1;
    PDDocument pdDocumentColor;
    KPDFTextStripper kpdfTextStripperColor;

    @Before
    public void setUp() throws Exception {
        kpdfCompare = new KPDFCompare();
        pdDocument = kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf");
        pdDocument1 = kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test1.pdf");
        pdDocumentColor = kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "testColor.pdf");

        kpdfTextStripper = new KPDFTextStripper();
        kpdfTextStripper.getText(pdDocument);
        kpdfTextStripper1 = new KPDFTextStripper();
        kpdfTextStripper1.getText(pdDocument1);
        kpdfTextStripperColor = new KPDFTextStripper();
        kpdfTextStripperColor.getText(pdDocumentColor);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void compareTwoImages() throws Exception {

    }

    @Test
    public void comparePdfByTextContentWhen2FilesAreIdentical() throws Exception {

        KPDFTextStripper kpdfTextStripper1 = new KPDFTextStripper();
        kpdfTextStripper1.getText(pdDocument);

        List<KPDFCompare.ResultTextCompare> resultTextCompares = new ArrayList<KPDFCompare.ResultTextCompare>();
        assertEquals(resultTextCompares, kpdfCompare.comparePdfByText(kpdfTextStripper, kpdfTextStripper1, null, false, 0, 0, 0, 0));

        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{ \"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Tuesday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        assertEquals(resultTextCompares, kpdfCompare.comparePdfByText(kpdfTextStripper, kpdfTextStripper1, kpdfCompare.generateFilters(jsonString), false, 0, 0, 0, 0));


    }

    @Test
    public void comparePdfByTextWhenIdenticalFilesWithFilter() throws Exception {

        KPDFTextStripper kpdfTextStripper1 = new KPDFTextStripper();
        kpdfTextStripper1.getText(pdDocument);
        List<KPDFCompare.ResultTextCompare> resultTextCompares = new ArrayList<KPDFCompare.ResultTextCompare>();

        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        resultTextCompares = kpdfCompare.comparePdfByText(kpdfTextStripper, kpdfTextStripper1, kpdfCompare.generateFilters(jsonString), false, 0, 0, 0, 0);
        assertEquals(resultTextCompares.size(), 0);
    }

    @Test
    public void comparePdfByTextWhenTwoFilesHave17Differences() throws Exception {


        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.compareTextNoTolerance(kpdfTextStripper, kpdfTextStripper1);//, kpdfCompare.generateFilters(jsonString), false, 0, 0, 0, 0);
        assertEquals(4, resultTextCompares.size());
        assertEquals(4, kpdfCompare.comparePdfByText(kpdfTextStripper, kpdfTextStripper1, kpdfCompare.generateFilters(jsonString), false, 0, 0, 0, 0).size());
    }

    @Test
    public void checkIfAlignedInCooridnate() throws Exception {


        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.compareTextNoTolerance(kpdfTextStripper, kpdfTextStripper1);
        Integer countNOToleranceDifference = resultTextCompares.size();// /, kpdfCompare.generateFilters(jsonString), false, 0, 0, 0, 0);
        System.out.println(resultTextCompares.size());
        resultTextCompares = kpdfCompare.checkIfAlignedInCooridnate(resultTextCompares, "x");
        assertEquals(0, resultTextCompares.size() - countNOToleranceDifference);

        countNOToleranceDifference = resultTextCompares.size();// /, kpdfCompare.generateFilters(jsonString), false, 0, 0, 0, 0);
        resultTextCompares = kpdfCompare.checkIfAlignedInCooridnate(resultTextCompares, "y");
        assertEquals(0, resultTextCompares.size() - countNOToleranceDifference);


    }

    @Test
    public void compareTextContentsWhen4DifferencesNoFilters() throws Exception {


        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.compareTextNoTolerance(kpdfTextStripper, kpdfTextStripper1);
        Integer countNOToleranceDifference = resultTextCompares.size();//
        System.out.println(resultTextCompares.size());
        resultTextCompares = kpdfCompare.compareTextContents(resultTextCompares, null);
        assertEquals(2, resultTextCompares.size() - countNOToleranceDifference);

        countNOToleranceDifference = resultTextCompares.size();// /, kpdfCompare.generateFilters(jsonString), false, 0, 0, 0, 0);
        resultTextCompares = kpdfCompare.compareTextContents(resultTextCompares, kpdfCompare.generateFilters(jsonString));
        assertEquals(0, resultTextCompares.size() - countNOToleranceDifference);


    }

    @Test
    public void compareTextContentsWhen32DifferencesWithFiltersNOReplacement() throws Exception {


        String jsonString = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.compareTextNoTolerance(kpdfTextStripper, kpdfTextStripper1);
        Integer countNOToleranceDifference = resultTextCompares.size();//
        System.out.println(countNOToleranceDifference);
        resultTextCompares = kpdfCompare.compareTextContents(resultTextCompares, kpdfCompare.generateFilters(jsonString));
        assertEquals(0, resultTextCompares.size() - countNOToleranceDifference);


    }

    @Test
    public void compareTextContentsWhen32DifferencesWithFiltersWithCorrectReplacements() throws Exception {

        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu3X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.compareTextNoTolerance(kpdfTextStripper, kpdfTextStripper1);
        Integer countNOToleranceDifference = resultTextCompares.size();//
        resultTextCompares = kpdfCompare.compareTextContents(resultTextCompares, kpdfCompare.generateFilters(jsonStringWithPartlyReplacementExceptContactName));
        assertEquals(0, resultTextCompares.size() - countNOToleranceDifference);


    }

    @Test
    public void compareTextContentsWhen32DifferencesWithFiltersWithIncorrectReplacements() throws Exception {

        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.compareTextNoTolerance(kpdfTextStripper, kpdfTextStripper1);
        Integer countNOToleranceDifference = resultTextCompares.size();//
        resultTextCompares = kpdfCompare.compareTextContents(resultTextCompares, kpdfCompare.generateFilters(jsonStringWithPartlyReplacementExceptContactName));

        assertEquals(1, resultTextCompares.size() - countNOToleranceDifference);


    }

    @Test
    public void compareTextContentsWhe2DifferencesWithFiltersNoFilterOnDifference() throws Exception {

        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.compareTextNoTolerance(kpdfTextStripper, kpdfTextStripper1);
        Integer countNOToleranceDifference = resultTextCompares.size();//
        resultTextCompares = kpdfCompare.compareTextContents(resultTextCompares, kpdfCompare.generateFilters(jsonStringWithPartlyReplacementExceptContactName));

        assertEquals(2, resultTextCompares.size() - countNOToleranceDifference);


    }

    @Test
    public void compareTextContentsWhenNoDifferencesInStrokingColorRenderingMode() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";

        PDDocument pdDocument3 = kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf");
        PDDocument pdDocument4 = kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf");
        KPDFTextStripper kpdfTextStripper3 = new KPDFTextStripper();
        kpdfTextStripper3.getText(pdDocument3);
        KPDFTextStripper kpdfTextStripper4 = new KPDFTextStripper();
        kpdfTextStripper4.getText(pdDocument4);
       // kpdfTextStripper.getText(pdDocument);
        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.compareTextNoTolerance(kpdfTextStripper3, kpdfTextStripper4);
        Integer countNOToleranceDifference = resultTextCompares.size();//
        resultTextCompares = kpdfCompare.comparedTextByColorRenderingMode(kpdfTextStripper3, kpdfTextStripper4, kpdfCompare.generateFilters(jsonStringWithPartlyReplacementExceptContactName), resultTextCompares);

        assertEquals(0, resultTextCompares.size() - countNOToleranceDifference);

    }

    @Test
    public void compareTextContentsWhen1DifferencesInStrokingColorRenderingMode() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.compareTextNoTolerance(kpdfTextStripper, kpdfTextStripperColor);
        Integer countNOToleranceDifference = resultTextCompares.size();//
        resultTextCompares = kpdfCompare.comparedTextByColorRenderingMode(kpdfTextStripper, kpdfTextStripperColor, kpdfCompare.generateFilters(jsonStringWithPartlyReplacementExceptContactName), resultTextCompares);

        assertEquals(1, resultTextCompares.size() - countNOToleranceDifference);

    }

    @Test
    public void comparePDFs1() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";


        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.comparePDFs(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test3.pdf", ConfigHelper.getTestResourcesFolderPath() + File.separator + "test4.pdf",jsonStringWithPartlyReplacementExceptContactName);

        System.out.println(resultTextCompares.get(resultTextCompares.size()-1).reason);
    }

    @Test
    public void comparePDFsVisually() throws Exception {

        assertFalse(kpdfCompare.comparePDFsVisually(pdDocument,pdDocument1));
    }

    @Test
    public void pdfToImage() throws Exception {

    }

    @Test
    public void testPDFBoxExtractImages() throws Exception {

    }

    @Test
    public void generateFiltersTest() throws Exception {


        String jsonString = "{\"filter\":[{ \"FontSize\":\"15.0\",\"name\":\"bookingNumber\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Tuesday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\"},{\"value\":\"Fare Class: Red e Deal\",\"name\":\"fareClass\"},{\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KFilter> kFilters = kpdfCompare.generateFilters(jsonString);
        assertNull(kFilters.get(0).getValue());
        assertNull(kFilters.get(0).getFont());
        assertNull(kFilters.get(0).getReplacement());
        assertNull(kFilters.get(0).getWidth());
        assertNull(kFilters.get(0).getHeight());
        assertNull(kFilters.get(0).getOffset());
        assertNull(kFilters.get(0).getStartX());
        assertNull(kFilters.get(0).getStartY());
        assertEquals(kFilters.get(12).getIndex(), null);
        assertEquals(kFilters.get(0).getFontSize(), "15.0");
        assertEquals(kFilters.get(1).getReplacement(), "Itinerary issued: Tuesday 27 June 2017");
    }

    @Test
    public void generatePDFOnRectangles() throws Exception {

    }


    @Test
    public void comparedTextByColorRenderingModeTestWithTotallyDifferentColors() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.compareTextNoTolerance(kpdfTextStripper, kpdfTextStripperColor);

        Integer countNOToleranceDifference = resultTextCompares.size();//
        resultTextCompares = kpdfCompare.comparedTextByColorRenderingMode(kpdfTextStripper, kpdfTextStripperColor, kpdfCompare.generateFilters(jsonStringWithPartlyReplacementExceptContactName), resultTextCompares);
        assertEquals(1, resultTextCompares.size() - countNOToleranceDifference);
    }
    @Test
    public void comparedTextByColorRenderingModeTestWithSameColorsCountButStillDifferent() throws Exception {
        String jsonStringWithPartlyReplacementExceptContactName = "{\"filter\":[{\"value\":\"9104024\",\"FontSize\":\"15.0\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Itinerary issued: Monday 26 June 2017\",\"name\":\"issuedDate\",\"replacement\":\"Itinerary issued: Monday 27 June 2017\"},{\"value\":\"Airline Reference: VU3X9V\",\"Font\":\"PDType1Font Helvetica-Bold\",\"name\":\"airlineReference\",\"replacement\":\"Airline Reference: Vu4X9V\"},{\"value\":\"Agent Reference: 9XM07A\",\"Font\":\"PDType1Font Helvetica\",\"name\":\"agentReference\",\"replacement\":\"Agent Reference: 9xM07A\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"232.46\",\"name\":\"departDate\",\"replacement\":\"Monday 3 July 2017\"},{\"value\":\"Monday 3 July 2017\",\"startX\":\"359.29\",\"name\":\"arrivalDate\",\"replacement\":\"Monday 4 July 2017\"},{\"value\":\"9104024\",\"FontSize\":\"19.5\",\"name\":\"bookingNumber\",\"replacement\":\"9104025\"},{\"value\":\"Flight No: QF501\",\"name\":\"flightNo\",\"replacement\":\"Flight No: QF502\"},{\"value\":\"6:00AM\",\"name\":\"departTime\"},{\"value\":\"7:35AM\",\"name\":\"arrivalTime\",\"replacement\":\"7:45AM\"}, {\"value\":\"Qantas\",\"name\":\"flightBrand\"},{\"value\":\"DOMESTIC - Terminal 3\",\"name\":\"arrivalTerminal\"}]}";
        List<KPDFCompare.ResultTextCompare> resultTextCompares = kpdfCompare.compareTextNoTolerance(kpdfTextStripper, kpdfTextStripper1);
        Integer countNOToleranceDifference = resultTextCompares.size();//
        resultTextCompares = kpdfCompare.comparedTextByColorRenderingMode(kpdfTextStripper, kpdfTextStripper1, kpdfCompare.generateFilters(jsonStringWithPartlyReplacementExceptContactName), resultTextCompares);
        assertEquals(1, resultTextCompares.size() - countNOToleranceDifference);
    }

    @Test
    public void getPDDocumentTest() throws Exception {
        assertEquals((kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf")).getClass(), PDDocument.class);
        assertNotNull(kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test.pdf"));
        assertNull(kpdfCompare.getPDDocument(ConfigHelper.getTestResourcesFolderPath() + File.separator + "test400.pdf"));
    }

}