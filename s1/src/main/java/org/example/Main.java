package org.example;

import javax.xml.crypto.Data;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Шаг 1: Создание базы данных
        Database.createDatabase();

        // Шаг 2: Загрузка бизнесменов из CSV-файла
        List<Businessman> businessmen = CSVReaderUtil.readBusinessmenFromFile("Forbes (2).csv");


        // Шаг 3: Сохранение в базу данных
        Database.saveToDatabase(businessmen);

        Database.getTopNetworthInEnergy();
        // Шаг 6: Запрос для нахождения бизнесменов по стране
        Database.getBusinessmenByCountry("United States");

        // Шаг 7: Запрос для нахождения среднего возраста по отрасли
        Database.getAverageAgeByIndustry("Energy");

        List<Businessman> topBusinessmen = Database.getTopBusinessmenInEnergyFromUSA();

        // Строим график с полученными данными
        Graph.createChart(topBusinessmen);

    }
}
