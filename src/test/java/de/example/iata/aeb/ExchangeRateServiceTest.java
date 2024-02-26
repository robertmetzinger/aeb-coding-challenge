package de.example.iata.aeb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateServiceTest {

    private static ExchangeRateService exchangeRateService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @BeforeEach
    public void setupTest() {
        exchangeRateService = new ExchangeRateService();

        List<String[]> data = new ArrayList<>();

        String[] afn1 = {"Afghanistan", "57,41", "AFN", "10.12.2010", "09.01.2011", ""};
        String[] afn2 = {"Afghanistan", "60,2535", "AFN", "10.01.2011", "09.02.2011", ""};
        String[] afn3 = {"Afghanistan", "62,08715", "AFN", "10.02.2011", "09.03.2011", ""};
        String[] afn4 = {"Afghanistan", "62,4547", "AFN", "10.03.2011", "09.04.2011", ""};
        String[] afn5 = {"Afghanistan", "64,1672", "AFN", "10.04.2011", "09.05.2011", ""};
        String[] egp1 = {"Ägypten", "7,71163", "EGP", "10.12.2010", "09.01.2011", ""};
        String[] egp2 = {"Ägypten", "7,6652", "EGP", "10.01.2011", "09.02.2011", ""};
        String[] egp3 = {"Ägypten", "7,9893", "EGP", "10.02.2011", "09.03.2011", ""};
        String[] egp4 = {"Ägypten", "8,1207", "EGP", "10.03.2011", "09.04.2011", ""};
        String[] egp5 = {"Ägypten", "8,4216", "EGP", "10.04.2011", "09.05.2011", ""};
        String[] alb1 = {"Albanien", "1,3353", "USD", "10.12.2010", "09.01.2011", ""};
        String[] alb2 = {"Albanien", "1,31995", "USD", "10.01.2011", "09.02.2011", ""};
        String[] alb3 = {"Albanien", "1,36965", "USD", "10.02.2011", "09.03.2011", ""};
        String[] alb4 = {"Albanien", "1,37925", "USD", "10.03.2011", "09.04.2011", ""};
        String[] alb5 = {"Albanien", "1,41465", "USD", "10.04.2011", "09.05.2011", ""};
        String[] dzd1 = {"Algerien", "99,50665", "DZD", "10.12.2010", "09.01.2011", ""};
        String[] dzd2 = {"Algerien", "98,2775", "DZD", "10.01.2011", "09.02.2011", ""};
        String[] dzd3 = {"Algerien", "99,81115", "DZD", "10.02.2011", "09.03.2011", ""};
        String[] dzd4 = {"Algerien", "100,09665", "DZD", "10.03.2011", "09.04.2011", ""};
        String[] dzd5 = {"Algerien", "101,7285", "DZD", "10.04.2011", "09.05.2011", ""};
        String[] ang1 = {"Angola", "1,3353", "USD", "10.12.2010", "09.01.2011", ""};
        String[] ang2 = {"Angola", "1,31995", "USD", "10.01.2011", "09.02.2011", ""};
        String[] ang3 = {"Angola", "1,36965", "USD", "10.02.2011", "09.03.2011", ""};
        String[] ang4 = {"Angola", "1,37925", "USD", "10.03.2011", "09.04.2011", ""};
        String[] ang5 = {"Angola", "1,41465", "USD", "10.04.2011", "09.05.2011", ""};
        String[] xaf1 = {"Äquatorialguinea", "657,105", "XAF", "10.12.2010", "09.01.2011", ""};
        String[] xaf2 = {"Äquatorialguinea", "657,10745", "XAF", "10.01.2011", "09.02.2011", ""};
        String[] xaf3 = {"Äquatorialguinea", "657,1212", "XAF", "10.02.2011", "09.03.2011", ""};
        String[] xaf4 = {"Äquatorialguinea", "657,43455", "XAF", "10.03.2011", "09.04.2011", ""};
        String[] xaf5 = {"Äquatorialguinea", "656,19445", "XAF", "10.04.2011", "09.05.2011", ""};
        String[] gnf1 = {"Guinea", "9246,965", "GNF", "10.12.2010", "09.01.2011", ""};
        String[] gnf2 = {"Guinea", "9,33794", "GNF", "10.01.2011", "09.02.2011", "Umrechnungskurs für 1000 WE (Währungseinheiten)"};
        String[] gnf3 = {"Guinea", "10,20405", "GNF", "10.02.2011", "09.03.2011", "Umrechnungskurs für 1000 WE (Währungseinheiten)"};
        String[] gnf4 = {"Guinea", "10,29235", "GNF", "10.03.2011", "09.04.2011", "Umrechnungskurs für 1000 WE (Währungseinheiten)"};
        String[] gnf5 = {"Guinea", "9,37186", "GNF", "10.04.2011", "09.05.2011", "Umrechnungskurs für 1000 WE (Währungseinheiten)"};

        data.add(afn1);
        data.add(afn2);
        data.add(afn3);
        data.add(afn4);
        data.add(afn5);
        data.add(egp1);
        data.add(egp2);
        data.add(egp3);
        data.add(egp4);
        data.add(egp5);
        data.add(alb1);
        data.add(alb2);
        data.add(alb3);
        data.add(alb4);
        data.add(alb5);
        data.add(dzd1);
        data.add(dzd2);
        data.add(dzd3);
        data.add(dzd4);
        data.add(dzd5);
        data.add(ang1);
        data.add(ang2);
        data.add(ang3);
        data.add(ang4);
        data.add(ang5);
        data.add(xaf1);
        data.add(xaf2);
        data.add(xaf3);
        data.add(xaf4);
        data.add(xaf5);
        data.add(gnf1);
        data.add(gnf2);
        data.add(gnf3);
        data.add(gnf4);
        data.add(gnf5);

        exchangeRateService.parseExchangeRates(data);
    }

    @Test
    void displayExchangeRateTest() {
        LocalDate date = LocalDate.parse("10.12.2010", formatter);
        try {
            ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate("AFN", date);
            assertEquals("57.41", String.valueOf(exchangeRate.getExchangeRateValue()));
        } catch (ExchangeRateNotFoundException e) {
            fail();
        }
    }

    @Test
    void displayExchangeRateTestNonExistingCurrency() {
        LocalDate date = LocalDate.parse("10.12.2010", formatter);
        assertThrows(ExchangeRateNotFoundException.class, () ->
                exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate("XYZ", date)
        );
    }

    @Test
    void displayExchangeRateTestWrongDate() {
        LocalDate date = LocalDate.parse("09.12.2010", formatter);
        assertThrows(ExchangeRateNotFoundException.class, () ->
                exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate("AFN", date)
        );
    }

    @Test
    void displayExchangeRateTestIgnoreLineWithComment() {
        LocalDate date = LocalDate.parse("10.01.2011", formatter);
        assertThrows(ExchangeRateNotFoundException.class, () ->
                exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate("GNF", date)
        );
    }
}
