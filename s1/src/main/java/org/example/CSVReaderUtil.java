package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReaderUtil {

    public static List<Businessman> readBusinessmenFromFile(String filePath) {
        List<Businessman> businessmen = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;

            // Пропускаем первую строку (заголовки)
            reader.readNext();

            // Чтение всех строк данных
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length == 7) {  // Убедитесь, что строка содержит 7 столбцов
                    int rank = Integer.parseInt(nextLine[0].trim());
                    String name = nextLine[1].trim();
                    double networth = Double.parseDouble(nextLine[2].trim());
                    int age = Integer.parseInt(nextLine[3].trim());
                    String country = nextLine[4].trim();
                    String source = nextLine[5].trim();
                    String industry = nextLine[6].trim();

                    businessmen.add(new Businessman(rank, name, networth, age, country, source, industry));
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return businessmen;
    }
}
