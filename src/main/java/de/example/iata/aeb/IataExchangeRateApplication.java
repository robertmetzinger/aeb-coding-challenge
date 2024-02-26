package de.example.iata.aeb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class IataExchangeRateApplication {

    private ExchangeRateService exchangeRateService;

    public void run() throws Exception {
        readIataExchangeRates();

        displayMenu();

        boolean exitRequested = false;

        while (!exitRequested) {
            String userInput = getUserInput();

            exitRequested = processUserInputAndCheckForExitRequest(userInput);
        }

        System.out.println("Auf Wiedersehen!");
    }

    private void readIataExchangeRates() {
        exchangeRateService = new ExchangeRateService();
        exchangeRateService.parseExchangeRates();
    }

    private void displayMenu() {
        System.out.println("IATA W�hrungskurs-Beispiel");
        System.out.println();

        System.out.println("W�hlen Sie eine Funktion durch Auswahl der Zifferntaste und Dr�cken von 'Return'");
        System.out.println("[1] W�hrungskurs anzeigen");
        System.out.println("[2] Neuen W�hrungskurs eingeben");
        System.out.println();

        System.out.println("[0] Beenden");
    }

    private String getUserInput() throws Exception {
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

        return consoleInput.readLine();
    }

    //Returns true when the user wants to exit the application
    private boolean processUserInputAndCheckForExitRequest(String userInput) throws Exception {
        if (userInput.equals("0")) {
            return true;
        }

        if (userInput.equals("1")) {
            displayIataExchangeRate();
        } else if (userInput.equals("2")) {
            enterIataExchangeRate();
        } else {
            System.out.println("Falsche Eingabe. Versuchen Sie es bitte erneut.");
        }

        return false;
    }

    private void displayIataExchangeRate() throws Exception {
        String currencyIsoCode = getUserInputForStringField("W�hrung");
        try {
            LocalDate date = getUserInputForDateField("Datum");
            exchangeRateService.displayExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, date);
        } catch (Exception e) {
            System.out.println("Datumseingabe konnte nicht verarbeitet werden");
        }

    }

    private void enterIataExchangeRate() throws Exception {
        String currencyIsoCode = getUserInputForStringField("W�hrung");
        LocalDate from = getUserInputForDateField("Von");
        LocalDate to = getUserInputForDateField("Bis");
        float exchangeRate = getUserInputForFloatField("Euro-Kurs f�r 1 " + currencyIsoCode);

        exchangeRateService.enterExchangeRate(currencyIsoCode, from, to, exchangeRate);
    }

    private String getUserInputForStringField(String fieldName) throws Exception {
        System.out.print(fieldName + ": ");
        return getUserInput();
    }

    private LocalDate getUserInputForDateField(String fieldName) throws Exception {
        System.out.print(fieldName + " (tt.mm.jjjj): ");
        String dateString = getUserInput();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(dateString, formatter);
    }

    private Double getUserInputForDoubleField(String fieldName) throws Exception {
        String doubleString = getUserInputForStringField(fieldName);
        return Double.valueOf(doubleString);
    }

    private float getUserInputForFloatField(String fieldName) throws Exception {
        String floatString = getUserInputForStringField(fieldName);
        return Float.parseFloat(floatString);
    }
}
