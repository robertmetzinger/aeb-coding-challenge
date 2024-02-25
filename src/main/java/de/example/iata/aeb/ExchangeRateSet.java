package de.example.iata.aeb;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRateSet {

    private ArrayList<ExchangeRate> exchangeRates;

    public ExchangeRateSet() {
        exchangeRates = new ArrayList<>();
    }

    public boolean addExchangeRate(ExchangeRate exchangeRate) {
        boolean success = false;
        exchangeRates.add(exchangeRate);
        return success;
    }

    public void print() {
        for (ExchangeRate exchangeRate: exchangeRates) {
            exchangeRate.print();
            System.out.println();
        }
    }
}
