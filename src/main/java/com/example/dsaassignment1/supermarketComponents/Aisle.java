package com.example.dsaassignment1.supermarketComponents;

public class Aisle {
    private String name;
    private double length, width, temperature;
    public Aisle(String name, double length, double width, double temperature) {
        setName(name);
        setLength(length);
        setWidth(width);
        setTemperature(temperature);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
