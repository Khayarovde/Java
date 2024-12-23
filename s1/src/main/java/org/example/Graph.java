package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;

public class Graph {

    // Метод для создания графика
    public static void createChart(List<Businessman> businessmen) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Добавляем данные в набор
        businessmen.forEach(b -> dataset.addValue(b.getNetworth(), b.getName(), b.getName()));

        // Создание графика
        JFreeChart barChart = ChartFactory.createBarChart(
                "Top 5 Energy Businessmen from USA",  // Заголовок
                "Businessman", // Ось X
                "Net Worth (in billions)", // Ось Y
                dataset,  // Данные
                PlotOrientation.VERTICAL,  // Вертикальная ориентация
                true,  // Легенда
                true,  // Информация по наведению
                false  // URL-обработчики
        );

        // Создание панели для отображения графика
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));  // Устанавливаем размер панели
        JFrame frame = new JFrame("Businessmen Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Закрытие по кнопке
        frame.getContentPane().add(chartPanel);  // Добавляем панель в окно
        frame.pack();
        frame.setVisible(true);  // Отображаем окно
    }
}
