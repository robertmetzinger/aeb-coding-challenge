package de.example.iata.aeb;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class IataExchangeRateApplication {

    private ExchangeRateService exchangeRateService;

    public void run() throws Exception {

        exchangeRateService = new ExchangeRateService();

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
        CSVFileReader reader = new CSVFileReader();
        List<String[]> exchangeRatesData = reader.readFile("src/main/resources/KursExport.csv", ";", 6);
        exchangeRateService.parseExchangeRates(exchangeRatesData);
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
            ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCurrencyIsoCodeAndDate(currencyIsoCode, date);

            System.out.printf("Der Währungskurs für %s zum Datum %s beträgt %s.",
                    currencyIsoCode.toUpperCase(),
                    date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                    exchangeRate.getExchangeRateValue());
        } catch (ExchangeRateNotFoundException e) {
            System.out.println("Der Währungskurs konnte nicht ermittelt werden.");
        } catch (Exception e) {
            System.out.println("Eingabe konnte nicht verarbeitet werden.");
        }

    }

    private void enterIataExchangeRate() {
        try {
            String currencyIsoCode = getUserInputForStringField("W�hrung");
            LocalDate from = getUserInputForDateField("Von");
            LocalDate to = getUserInputForDateField("Bis");
            float exchangeRate = getUserInputForFloatField("Euro-Kurs f�r 1 " + currencyIsoCode);

            boolean success = exchangeRateService.enterExchangeRate(currencyIsoCode, from, to, exchangeRate);
            if (success) {
                System.out.println("Neuer Währungskurs erfolgreich hinzugefügt.");
            } else {
                System.out.println("Neuer Währungskurs konnte nicht hinzugefügt werden.");
            }

        } catch (Exception e) {
            System.out.println("Eingabe konnte nicht verarbeitet werden.");
        }
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
