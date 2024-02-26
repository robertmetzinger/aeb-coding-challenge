package de.example.iata.aeb;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ExchangeRateService {

    private CSVFileReader reader;
    private ExchangeRateSet exchangeRateSet;

    public ExchangeRateService() {
        reader = new CSVFileReader();
        exchangeRateSet = new ExchangeRateSet();
    }

    public void parseExchangeRates() {
        List<String[]> parsedData = reader.readFile("src/main/resources/KursExport.csv", ";", 6);

        float exchangeRateValue;
        String isoCountryCode;
        LocalDate startValidDate;
        LocalDate endValidDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for (String[] parsedLine : parsedData) {
            // ignore country name parsedLine[0]
            try {
                exchangeRateValue = NumberFormat.getNumberInstance(Locale.GERMAN).parse(parsedLine[1]).floatValue();
                isoCountryCode = parsedLine[2].toUpperCase();
                startValidDate = LocalDate.parse(parsedLine[3], formatter);
                endValidDate = LocalDate.parse(parsedLine[4], formatter);

                if (parsedLine[5].isEmpty()) { // ignore if line has comments
                    ExchangeRate exchangeRate = new ExchangeRate(isoCountryCode, exchangeRateValue, startValidDate, endValidDate);
                    exchangeRateSet.addExchangeRate(exchangeRate);
                }
            } catch (Exception e) {
                // ignore line if it has incomplete or invalid data
                System.out.println("Couldn't parse exchange rate " + Arrays.toString(parsedLine));
            }
        }
        exchangeRateSet.print();
    }

    public void displayExchangeRateByCurrencyIsoCodeAndDate(String currencyIsoCode, LocalDate date) throws ExchangeRateNotFoundException {
        try {
            ExchangeRate exchangeRate = exchangeRateSet.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, date);
            System.out.printf("Der Währungskurs für %s zum Datum %s beträgt %s.",
                    currencyIsoCode.toUpperCase(),
                    date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    exchangeRate.getExchangeRateValue());
        } catch (ExchangeRateNotFoundException e) {
            System.out.println("Der Währungskurs konnte nicht ermittelt werden.");
        }
    }
}
