package de.example.iata.aeb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateServiceTest {

    private static ExchangeRateService exchangeRateService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @BeforeEach
    public void setupTest() {
        exchangeRateService = ExchangeRateServiceTestSetup.setupExchangeRateService();
    }

    @Test
    public void displayExchangeRateTest() {
        LocalDate date = LocalDate.parse("10.12.2010", formatter);
        try {
            ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate("AFN", date);
            assertEquals("57.41", String.valueOf(exchangeRate.getExchangeRateValue()));
        } catch (ExchangeRateNotFoundException e) {
            fail();
        }
    }

    @Test
    public void displayExchangeRateTestNonExistingCurrency() {
        LocalDate date = LocalDate.parse("10.12.2010", formatter);
        assertThrows(ExchangeRateNotFoundException.class, () ->
                exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate("XYZ", date)
        );
    }

    @Test
    public void displayExchangeRateTestWrongDate() {
        LocalDate date = LocalDate.parse("09.12.2010", formatter);
        assertThrows(ExchangeRateNotFoundException.class, () ->
                exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate("AFN", date)
        );
    }

    @Test
    public void displayExchangeRateTestIgnoreLineWithComment() {
        LocalDate date = LocalDate.parse("10.01.2011", formatter);
        assertThrows(ExchangeRateNotFoundException.class, () ->
                exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate("GNF", date)
        );
    }

    @Test
    public void enterExchangeRateTest() {
        String currencyIsoCode = "ABC";
        LocalDate startDate = LocalDate.parse("01.01.2024", formatter);
        LocalDate endDate = LocalDate.parse("01.02.2024", formatter);
        float exchangeRateValue = Float.parseFloat("1.5");
        exchangeRateService.enterExchangeRate(currencyIsoCode, startDate, endDate, exchangeRateValue);


        LocalDate checkDate = LocalDate.parse("05.01.2024", formatter);
        try {
            ExchangeRate checkExchangeRate = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDate);
            assertEquals(exchangeRateValue, checkExchangeRate.getExchangeRateValue());
        } catch (ExchangeRateNotFoundException e) {
            fail();
        }
    }

    @Test
    public void enterExchangeRateWithinExistingTest() {
        // {"Afghanistan", "57,41", "AFN", "10.12.2010", "09.01.2011", ""}; checking with this exchange rate
        String currencyIsoCode = "AFN";
        LocalDate startDate = LocalDate.parse("15.12.2010", formatter);
        LocalDate endDate = LocalDate.parse("20.12.2010", formatter);
        float exchangeRateValueOfNew = Float.parseFloat("1.5");
        exchangeRateService.enterExchangeRate(currencyIsoCode, startDate, endDate, exchangeRateValueOfNew);


        LocalDate checkDateBefore = LocalDate.parse("12.12.2010", formatter);
        LocalDate checkDateAfter = LocalDate.parse("05.01.2011", formatter);
        LocalDate checkDateMiddle = LocalDate.parse("17.12.2010", formatter);
        LocalDate checkDateBoundaryStart = LocalDate.parse("15.12.2010", formatter);
        LocalDate checkDateBoundaryEnd = LocalDate.parse("20.12.2010", formatter);
        float exchangeRateValueOfExisting = Float.parseFloat("57.41");
        try {
            ExchangeRate checkExchangeRateBefore = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBefore);
            assertEquals(exchangeRateValueOfExisting, checkExchangeRateBefore.getExchangeRateValue());

            ExchangeRate checkExchangeRateAfter = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateAfter);
            assertEquals(exchangeRateValueOfExisting, checkExchangeRateAfter.getExchangeRateValue());

            ExchangeRate checkExchangeRateMiddle = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateMiddle);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateMiddle.getExchangeRateValue());

            ExchangeRate checkExchangeRateBoundaryStart = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBoundaryStart);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateBoundaryStart.getExchangeRateValue());

            ExchangeRate checkExchangeRateBoundaryEnd = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBoundaryEnd);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateBoundaryEnd.getExchangeRateValue());
        } catch (ExchangeRateNotFoundException e) {
            fail();
        }
    }

    @Test
    public void enterExchangeRateIncludingExistingTest() {
        // afn1 = {"Afghanistan", "57,41", "AFN", "10.12.2010", "09.01.2011", ""}; // overlap this
        // afn2 = {"Afghanistan", "60,2535", "AFN", "10.01.2011", "09.02.2011", ""}; // fully include this
        // afn3 = {"Afghanistan", "62,08715", "AFN", "10.02.2011", "09.03.2011", ""}; // overlap this

        String currencyIsoCode = "AFN";
        LocalDate startDate = LocalDate.parse("05.01.2011", formatter);
        LocalDate endDate = LocalDate.parse("12.02.2011", formatter);
        float exchangeRateValueOfNew = Float.parseFloat("1.5");
        exchangeRateService.enterExchangeRate(currencyIsoCode, startDate, endDate, exchangeRateValueOfNew);


        LocalDate checkDateBefore = LocalDate.parse("04.01.2011", formatter);
        LocalDate checkDateAfter = LocalDate.parse("15.02.2011", formatter);
        LocalDate checkDateMiddle = LocalDate.parse("17.01.2011", formatter);
        LocalDate checkDateBoundaryStart = LocalDate.parse("05.01.2011", formatter);
        LocalDate checkDateBoundaryEnd = LocalDate.parse("12.02.2011", formatter);
        float exchangeRateValueOfExistingBefore = Float.parseFloat("57.41");
        float exchangeRateValueOfExistingAfter = Float.parseFloat("62.08715");
        try {
            ExchangeRate checkExchangeRateBefore = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBefore);
            assertEquals(exchangeRateValueOfExistingBefore, checkExchangeRateBefore.getExchangeRateValue());

            ExchangeRate checkExchangeRateAfter = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateAfter);
            assertEquals(exchangeRateValueOfExistingAfter, checkExchangeRateAfter.getExchangeRateValue());

            ExchangeRate checkExchangeRateMiddle = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateMiddle);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateMiddle.getExchangeRateValue());

            ExchangeRate checkExchangeRateBoundaryStart = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBoundaryStart);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateBoundaryStart.getExchangeRateValue());

            ExchangeRate checkExchangeRateBoundaryEnd = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBoundaryEnd);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateBoundaryEnd.getExchangeRateValue());
        } catch (ExchangeRateNotFoundException e) {
            fail();
        }
    }

    @Test
    public void enterExchangeRateOverlapExistingTest() {
        // afn1 = {"Afghanistan", "57,41", "AFN", "10.12.2010", "09.01.2011", ""}; // overlap this
        // afn2 = {"Afghanistan", "60,2535", "AFN", "10.01.2011", "09.02.2011", ""}; // overlap this

        String currencyIsoCode = "AFN";
        LocalDate startDate = LocalDate.parse("05.01.2011", formatter);
        LocalDate endDate = LocalDate.parse("05.02.2011", formatter);
        float exchangeRateValueOfNew = Float.parseFloat("1.5");
        exchangeRateService.enterExchangeRate(currencyIsoCode, startDate, endDate, exchangeRateValueOfNew);


        LocalDate checkDateBefore = LocalDate.parse("04.01.2011", formatter);
        LocalDate checkDateAfter = LocalDate.parse("06.02.2011", formatter);
        LocalDate checkDateMiddle = LocalDate.parse("17.01.2011", formatter);
        LocalDate checkDateBoundaryStart = LocalDate.parse("05.01.2011", formatter);
        LocalDate checkDateBoundaryEnd = LocalDate.parse("05.02.2011", formatter);
        float exchangeRateValueOfExistingBefore = Float.parseFloat("57.41");
        float exchangeRateValueOfExistingAfter = Float.parseFloat("60.2535");
        try {
            ExchangeRate checkExchangeRateBefore = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBefore);
            assertEquals(exchangeRateValueOfExistingBefore, checkExchangeRateBefore.getExchangeRateValue());

            ExchangeRate checkExchangeRateAfter = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateAfter);
            assertEquals(exchangeRateValueOfExistingAfter, checkExchangeRateAfter.getExchangeRateValue());

            ExchangeRate checkExchangeRateMiddle = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateMiddle);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateMiddle.getExchangeRateValue());

            ExchangeRate checkExchangeRateBoundaryStart = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBoundaryStart);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateBoundaryStart.getExchangeRateValue());

            ExchangeRate checkExchangeRateBoundaryEnd = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBoundaryEnd);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateBoundaryEnd.getExchangeRateValue());
        } catch (ExchangeRateNotFoundException e) {
            fail();
        }
    }

    @Test
    public void enterExchangeRateEqualExistingTest() {
        // afn1 = {"Afghanistan", "57,41", "AFN", "10.12.2010", "09.01.2011", ""}; // overlap this
        // afn2 = {"Afghanistan", "60,2535", "AFN", "10.01.2011", "09.02.2011", ""}; // replace this
        // afn3 = {"Afghanistan", "62,08715", "AFN", "10.02.2011", "09.03.2011", ""}; // overlap this

        String currencyIsoCode = "AFN";
        LocalDate startDate = LocalDate.parse("10.01.2011", formatter);
        LocalDate endDate = LocalDate.parse("09.02.2011", formatter);
        float exchangeRateValueOfNew = Float.parseFloat("1.5");
        exchangeRateService.enterExchangeRate(currencyIsoCode, startDate, endDate, exchangeRateValueOfNew);


        LocalDate checkDateBefore = LocalDate.parse("04.01.2011", formatter);
        LocalDate checkDateAfter = LocalDate.parse("10.02.2011", formatter);
        LocalDate checkDateMiddle = LocalDate.parse("17.01.2011", formatter);
        LocalDate checkDateBoundaryStart = LocalDate.parse("10.01.2011", formatter);
        LocalDate checkDateBoundaryEnd = LocalDate.parse("09.02.2011", formatter);
        float exchangeRateValueOfExistingBefore = Float.parseFloat("57.41");
        float exchangeRateValueOfExistingAfter = Float.parseFloat("62.08715");
        try {
            ExchangeRate checkExchangeRateBefore = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBefore);
            assertEquals(exchangeRateValueOfExistingBefore, checkExchangeRateBefore.getExchangeRateValue());

            ExchangeRate checkExchangeRateAfter = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateAfter);
            assertEquals(exchangeRateValueOfExistingAfter, checkExchangeRateAfter.getExchangeRateValue());

            ExchangeRate checkExchangeRateMiddle = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateMiddle);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateMiddle.getExchangeRateValue());

            ExchangeRate checkExchangeRateBoundaryStart = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBoundaryStart);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateBoundaryStart.getExchangeRateValue());

            ExchangeRate checkExchangeRateBoundaryEnd = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, checkDateBoundaryEnd);
            assertEquals(exchangeRateValueOfNew, checkExchangeRateBoundaryEnd.getExchangeRateValue());
        } catch (ExchangeRateNotFoundException e) {
            fail();
        }
    }
}
