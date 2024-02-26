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
        if (violatesTimeConstraints(exchangeRate)) {
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
                    /*System.out.print("Duplicate ignored ");
                    exchangeRate.print();
                    System.out.println();*/
                    return true;
                }
            }
        }
        return false;
    }

    private boolean violatesTimeConstraints(ExchangeRate exchangeRate) {
        if (exchangeRate.getStartValidDate().isAfter(exchangeRate.getEndValidDate())) {
            /*System.out.print("Violates time constraints ");
            exchangeRate.print();
            System.out.println();*/
            return true;
        }
        return false;
    }

    public void adjustOverlappingExchangeRates(ExchangeRate inputExchangeRate) {
        String currencyIsoCode = inputExchangeRate.getCurrencyIsoCode();
        LocalDate startDateInput = inputExchangeRate.getStartValidDate();
        LocalDate endDateInput = inputExchangeRate.getEndValidDate();

        List<ExchangeRate> exchangeRatesWithIsoCode = getExchangeRatesByCurrencyIsoCode(currencyIsoCode);
        for (ExchangeRate exchangeRate : exchangeRatesWithIsoCode) {

            LocalDate startDateOld = exchangeRate.getStartValidDate();
            LocalDate endDateOld = exchangeRate.getEndValidDate();

            // check if existing exchange rates are within date range of new exchange rate
            if (startDateInput.isBefore(startDateOld) ||
                    startDateInput.isEqual(startDateOld)) {
                // -> new start date <= existing exchange rate start date
                if (endDateInput.isAfter(endDateOld) ||
                        endDateInput.isEqual(endDateOld)) {
                    // -> new end date >= existing exchange rate end date

                    // remove existing exchange rate, because new exchange rate covers date range
                    exchangeRates.remove(exchangeRate);
                    /*System.out.print("removed ");
                    exchangeRate.print();
                    System.out.println();*/
                    continue;
                }
            }

            // check if new exchange rate is within existing exchange rate date range
            if (startDateInput.isAfter(startDateOld) ||
                    startDateInput.isEqual(startDateOld)) {
                // -> new start date >= existing exchange rate start date
                if (endDateInput.isBefore(endDateOld) ||
                        endDateInput.isEqual(endDateOld)) {
                    // -> new end date <= existing exchange rate end date

                    // split existing exchange rate into one before and one after new exchange rate

                    if (startDateInput.isEqual(endDateOld)) {
                        // remove if start dates are the same
                        exchangeRates.remove(exchangeRate);
                        /*System.out.print("removed ");
                        exchangeRate.print();
                        System.out.println();*/
                    } else {
                        // adjust existing exchange rate to be before new one
                        LocalDate adjustedEndDate = startDateInput.minusDays(1);
                        exchangeRate.setEndValidDate(adjustedEndDate);
                        /*System.out.print("adjusted ");
                        exchangeRate.print();
                        System.out.println();*/
                    }

                    if (endDateInput.isBefore(endDateOld)) {
                        // add exchange rate after new one
                        LocalDate adjustedStartDate = endDateInput.plusDays(1);
                        ExchangeRate afterExchangeRate = new ExchangeRate(
                                currencyIsoCode,
                                exchangeRate.getExchangeRateValue(),
                                adjustedStartDate,
                                endDateOld);
                        exchangeRates.add(afterExchangeRate);
                        /*System.out.print("added ");
                        afterExchangeRate.print();
                        System.out.println();*/
                        continue;
                    }
                }
            }

            // check if start date overlaps with existing exchange rates
            if (startDateInput.isAfter(startDateOld)) {
                // -> new start date > existing exchange rate start date
                if (startDateInput.isBefore(endDateOld) ||
                        startDateInput.isEqual(endDateOld)) {
                    // -> new start date <= existing exchange rate end date
                    // start date <= new start date <= end date ==> overlap

                    // adjust end date of existing exchange rate to one day before start date of new exchange rate
                    LocalDate adjustedEndDate = startDateInput.minusDays(1);
                    exchangeRate.setEndValidDate(adjustedEndDate);
                    /*System.out.print("adjusted ");
                    exchangeRate.print();
                    System.out.println();*/
                    continue;
                }
            }

            // check if end date overlaps with existing exchange rates
            if (endDateInput.isAfter(startDateOld) ||
                    endDateInput.isEqual(startDateOld)) {
                // -> new end date >= existing exchange rate start date
                if (endDateInput.isBefore(endDateOld)) {
                    // -> new end date <= existing exchange rate end date
                    // start date <= new end date <= end date ==> overlap

                    // adjust start date of existing exchange rate to one day after end date of new exchange rate
                    LocalDate adjustedStartDate = endDateInput.plusDays(1);
                    exchangeRate.setStartValidDate(adjustedStartDate);
                    /*System.out.print("adjusted ");
                    exchangeRate.print();
                    System.out.println();*/
                }
            }
        }
    }

    public void print() {
        for (ExchangeRate exchangeRate : exchangeRates) {
            exchangeRate.print();
            System.out.println();
        }
    }

    @Override
    public Object clone() {
        ExchangeRateSet exchangeRateSetClone = new ExchangeRateSet();
        for (ExchangeRate exchangeRate : exchangeRates) {
            exchangeRateSetClone.addExchangeRate((ExchangeRate) exchangeRate.clone());
        }
        return exchangeRateSetClone;
    }
}
