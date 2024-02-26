package de.example.iata.aeb;

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

    private List<ExchangeRate> getExchangeRatesByCurrencyIsoCode(String currencyIsoCode) {
        List<ExchangeRate> exchangeRatesWithIsoCode = new ArrayList<>();
        for (ExchangeRate exchangeRate : exchangeRates) {
            if (exchangeRate.getCurrencyIsoCode().equals(currencyIsoCode)) {
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
