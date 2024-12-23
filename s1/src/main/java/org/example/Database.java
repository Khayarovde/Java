package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:businessmen.db"; // Путь к базе данных

    // Создание базы данных и таблиц
    public static void createDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS Businessman (
                    Rank INTEGER PRIMARY KEY,
                    Name TEXT,
                    Networth REAL,
                    Age INTEGER,
                    Country TEXT,
                    Source TEXT,
                    Industry TEXT
                );
            """;
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableSQL);
                System.out.println("Таблица Businessman успешно создана или уже существует.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при создании базы данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Сохранение списка бизнесменов в базу данных
    public static void saveToDatabase(List<Businessman> businessmen) {
        String insertSQL = """
        INSERT OR REPLACE INTO Businessman (Rank, Name, Networth, Age, Country, Source, Industry)
        VALUES (?, ?, ?, ?, ?, ?, ?);
    """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            connection.setAutoCommit(false); // Выключаем автокоммит

            for (Businessman b : businessmen) {
                pstmt.setInt(1, b.getRank());
                pstmt.setString(2, b.getName().trim());  // Очищаем пробелы
                pstmt.setDouble(3, b.getNetworth());
                pstmt.setInt(4, b.getAge());
                pstmt.setString(5, b.getCountry().trim());  // Очищаем пробелы
                pstmt.setString(6, b.getSource().trim());  // Очищаем пробелы
                pstmt.setString(7, b.getIndustry().trim());  // Очищаем пробелы
                pstmt.addBatch(); // Добавляем в пакет
            }
            pstmt.executeBatch(); // Выполняем пакет
            connection.commit(); // Подтверждаем транзакцию

            System.out.println("Данные бизнесменов успешно сохранены в базу данных.");
        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении данных в базу данных: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Метод для отображения всех данных из таблицы Businessman
    public static void displayBusinessmen() {
        String selectSQL = "SELECT * FROM Businessman"; // SQL запрос для получения всех данных

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            // Выводим заголовок таблицы
            System.out.println("Rank | Name | Networth | Age | Country | Source | Industry");

            // Читаем и выводим каждую строку
            while (rs.next()) {
                System.out.println(rs.getInt("Rank") + " | " +
                        rs.getString("Name") + " | " +
                        rs.getDouble("Networth") + " | " +
                        rs.getInt("Age") + " | " +
                        rs.getString("Country") + " | " +
                        rs.getString("Source") + " | " +
                        rs.getString("Industry"));
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при отображении данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Запрос: Получение топ-бизнесмена в энергетике из США по капиталу
    public static void getTopNetworthInEnergy()
    {
        String query = """
        SELECT Name, Networth, Industry, Country
        FROM Businessman
        WHERE LOWER(TRIM(Industry)) = 'energy' AND LOWER(TRIM(Country)) = 'united states';
        LIMIT 1;
        """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                // Извлекаем данные и очищаем от пробелов
                String name = rs.getString("Name").trim();
                String industry = rs.getString("Industry").trim();
                String country = rs.getString("Country").trim();
                double networth = rs.getDouble("Networth");

                // Проверяем на случай некорректных данных
                if (industry.equalsIgnoreCase("energy") && country.equalsIgnoreCase("united states")) {
                    System.out.println("Топ-бизнесмен в энергетике из США: " + name + " с капиталом: " + networth);
                } else {
                    System.out.println("Некорректные данные: " + name + " не относится к энергетике из США.");
                }
            } else {
                System.out.println("Нет данных для указанного запроса.");
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса для топ-бизнесмена в энергетике: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public static void getBusinessmenByCountry(String country) {
        // Очищаем страну от пробелов и приводим к нижнему регистру
        String cleanedCountry = country.trim();

        // Если страна пуста после обрезки, выводим ошибку
        if (cleanedCountry.isEmpty()) {
            System.out.println("Не указана страна для поиска.");
            return;
        }

        String query = """
        SELECT Name, Rank, Networth, Age, Industry, Country
        FROM Businessman
        WHERE Country = ? AND LOWER(TRIM(Industry)) = 'energy'
    """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, cleanedCountry);  // Устанавливаем страну в запрос

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println(rs.getInt("Rank") + " | " +
                            rs.getString("Name") + " | " +
                            rs.getDouble("Networth") + " | " +
                            rs.getInt("Age") + " | " +
                            rs.getString("Industry"));
                }
                if (!found) {
                    System.out.println("Нет бизнесменов в стране: " + country);
                }

            }

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса для бизнесменов по стране: " + e.getMessage());
            e.printStackTrace();
        }
    }



    // Запрос: Получение среднего возраста бизнесменов по отрасли
    public static void getAverageAgeByIndustry(String industry) {
        String query = """
            SELECT AVG(Age) AS AvgAge
            FROM Businessman
            WHERE Industry = ?;
        """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, industry);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Средний возраст бизнесменов в отрасли " + industry + ": " + rs.getDouble("AvgAge"));
                } else {
                    System.out.println("Нет данных для указанной отрасли.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса для среднего возраста по отрасли: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Получение топ-5 бизнесменов в энергетике из США
    public static List<Businessman> getTopBusinessmenInEnergyFromUSA() {
        List<Businessman> topBusinessmen = new ArrayList<>();
        String query = """
            SELECT Rank, Name, Networth, Age, Country, Source, Industry
            FROM Businessman
            WHERE LOWER(TRIM(Industry)) = 'energy' AND LOWER(TRIM(Country)) = 'united states'
            ORDER BY Networth DESC
            LIMIT 5;
        """;

        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Businessman businessman = new Businessman(
                        rs.getInt("Rank"),
                        rs.getString("Name").trim(),
                        rs.getDouble("Networth"),
                        rs.getInt("Age"),
                        rs.getString("Country").trim(),
                        rs.getString("Source").trim(),
                        rs.getString("Industry").trim()
                );
                topBusinessmen.add(businessman);
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса для топ-бизнесменов: " + e.getMessage());
            e.printStackTrace();
        }
        return topBusinessmen;
    }
}
