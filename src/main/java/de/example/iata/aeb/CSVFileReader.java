package de.example.iata.aeb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSVFileReader {

    public List<String[]> readFile(String filePath, String delimiter, int columnCount) {

        List<String[]> parsedData = new ArrayList<>();

        try (Scanner lineScanner = new Scanner(new BufferedReader(new FileReader(filePath)))) {
            // scan file line by line
            while (lineScanner.hasNextLine()) {
                // scan line token by token
                Scanner tokenScanner = new Scanner(lineScanner.nextLine());
                tokenScanner.useDelimiter(delimiter);
                String[] parsedLine = new String[columnCount];

                for (int i = 0; i < columnCount; i++) {
                    if (tokenScanner.hasNext()) {
                        String token = tokenScanner.next();
                        parsedLine[i] = token;
                    }
                }
                parsedData.add(parsedLine);
                tokenScanner.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return parsedData;
    }
}
