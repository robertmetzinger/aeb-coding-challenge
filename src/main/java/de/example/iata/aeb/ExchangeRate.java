package de.example.iata.aeb;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ExchangeRate {

    private String currencyIsoCode;
    private float exchangeRateValue;
    private LocalDate startValidDate;
    private LocalDate endValidDate;

    public ExchangeRate(String currencyIsoCode, float exchangeRateValue, LocalDate startValidDate, LocalDate endValidDate) {
        this.currencyIsoCode = currencyIsoCode;
        this.exchangeRateValue = exchangeRateValue;
        this.startValidDate = startValidDate;
        this.endValidDate = endValidDate;
    }

    public String getCurrencyIsoCode() {
        return currencyIsoCode;
    }

    public float getExchangeRateValue() {
        return exchangeRateValue;
    }

    public LocalDate getStartValidDate() {
        return startValidDate;
    }

    public LocalDate getEndValidDate() {
        return endValidDate;
    }

    public void setStartValidDate(LocalDate startValidDate) {
        this.startValidDate = startValidDate;
    }

    public void setEndValidDate(LocalDate endValidDate) {
        this.endValidDate = endValidDate;
    }

    public void print() {
        System.out.printf("ExchangeRate[ISO Code: %s | Exchange Rate: %s | start valid date: %s | end valid date: %s]",
                getCurrencyIsoCode(),
                getExchangeRateValue(),
                getStartValidDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                getEndValidDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    @Override
    public Object clone() {
        return new ExchangeRate(this.currencyIsoCode, this.exchangeRateValue, this.startValidDate, this.endValidDate);
    }
}
