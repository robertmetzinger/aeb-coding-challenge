package de.example.iata.aeb;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ExchangeRateService {

    private ExchangeRateSet exchangeRateSet;

    public ExchangeRateService() {
        exchangeRateSet = new ExchangeRateSet();
    }

    public void parseExchangeRates(List<String[]> parsedData) {

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
                    boolean added = exchangeRateSet.addExchangeRate(exchangeRate);
                    if (!added) {
                        throw new Exception();
                    }
                }
            } catch (Exception e) {
                // ignore line if it has incomplete or invalid data
                // System.out.println("Couldn't parse exchange rate " + Arrays.toString(parsedLine));
            }
        }
        //exchangeRateSet.print();
    }

    public ExchangeRate getExchangeRateByCurrencyIsoCodeAndDate(String currencyIsoCode, LocalDate date) throws ExchangeRateNotFoundException {
        return exchangeRateSet.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, date);
    }

    public boolean enterExchangeRate(String currencyIsoCode, LocalDate startDate, LocalDate endDate, float exchangeRateValue) {
        ExchangeRateSet backup = (ExchangeRateSet) exchangeRateSet.clone();

        ExchangeRate exchangeRate = new ExchangeRate(currencyIsoCode, exchangeRateValue, startDate, endDate);
        exchangeRateSet.adjustOverlappingExchangeRates(exchangeRate);
        boolean added = exchangeRateSet.addExchangeRate(exchangeRate);
        if (added) {
            /*System.out.print("added ");
            exchangeRate.print();
            System.out.println();*/
        } else {
            exchangeRateSet = backup;
        }
        return added;
    }
}
