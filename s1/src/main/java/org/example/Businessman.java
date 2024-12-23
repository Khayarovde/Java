package org.example;

import lombok.Data;

@Data
public class Businessman {
    private int rank;
    private String name;
    private double networth;
    private int age;
    private String country;
    private String source;
    private String industry;

    // Конструктор
    public Businessman(int rank, String name, double networth, int age, String country, String source, String industry) {
        this.rank = rank;
        this.name = name;
        this.networth = networth;
        this.age = age;
        this.country = country;
        this.source = source;
        this.industry = industry;
    }

    @Override
    public String toString() {
        return "Businessman{" +
                "rank=" + rank +
                ", name='" + name + '\'' +
                ", networth=" + networth +
                ", age=" + age +
                ", country='" + country + '\'' +
                ", source='" + source + '\'' +
                ", industry='" + industry + '\'' +
                '}';
    }
}
