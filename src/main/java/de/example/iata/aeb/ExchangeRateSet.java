package de.example.iata.aeb;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateSet {

    private ArrayList<ExchangeRate> exchangeRates;

    public ExchangeRateSet() {
        exchangeRates = new ArrayList<>();
    }

    public boolean addExchangeRate(ExchangeRate exchangeRate) {
        if (isDuplicate(exchangeRate)) {
            return false;
        }
        exchangeRates.add(exchangeRate);
        return true;
    }

    public ExchangeRate getExchangeRateByCurrencyIsoCodeAndDate(String currencyIsoCode, LocalDate date) throws ExchangeRateNotFoundException {
        List<ExchangeRate> exchangeRatesWithIsoCode = getExchangeRatesByCurrencyIsoCode(currencyIsoCode);
        for (ExchangeRate exchangeRate : exchangeRatesWithIsoCode) {
            if (!date.isBefore(exchangeRate.getStartValidDate())) {
                // = date is equal or after currently checked exchange rate
                if (!date.isAfter(exchangeRate.getEndValidDate())) {
                    // = date is equal or before currently checked exchange rate
                    // means that date is in range, so we found the exchange rate
                    return exchangeRate;
                }
            }
        }
        throw new ExchangeRateNotFoundException();
    }

    private List<ExchangeRate> getExchangeRatesByCurrencyIsoCode(String currencyIsoCode) {
        List<ExchangeRate> exchangeRatesWithIsoCode = new ArrayList<>();
        for (ExchangeRate exchangeRate : exchangeRates) {
            if (exchangeRate.getCurrencyIsoCode().equals(currencyIsoCode.toUpperCase())) {
                exchangeRatesWithIsoCode.add(exchangeRate);
            }
        }
        return exchangeRatesWithIsoCode;
    }

    private boolean isDuplicate(ExchangeRate exchangeRate) {
        List<ExchangeRate> exchangeRatesWithIsoCode = getExchangeRatesByCurrencyIsoCode(exchangeRate.getCurrencyIsoCode());
        for (ExchangeRate exchangeRateOfIteration : exchangeRatesWithIsoCode) {
            if (exchangeRateOfIteration.getStartValidDate().isEqual(exchangeRate.getStartValidDate())) {
                if (exchangeRateOfIteration.getEndValidDate().isEqual(exchangeRate.getEndValidDate())) {
                    System.out.print("Duplicate ignored ");
                    exchangeRate.print();
                    System.out.println();
                    return true;
                }
            }
        }
        return false;
    }

    public void print() {
        for (ExchangeRate exchangeRate : exchangeRates) {
            exchangeRate.print();
            System.out.println();
        }
    }
}
