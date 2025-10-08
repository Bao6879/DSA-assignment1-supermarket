package com.example.dsaassignment1.supermarketComponents;

import com.example.dsaassignment1.linkedList.BaoList;

public class Aisle extends Components{
    private String name;
    private double length, width, temperature;
    private BaoList <Shelf> shelves;
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

    public BaoList <Shelf> getShelves() {
        return shelves;
    }

    @Override
    public int getTotalSize() {
        int totalSize = 0;
        for (Shelf shelf : shelves) {
            totalSize+= shelf.getTotalSize();
        }
        return totalSize;
    }

    @Override
    public String toString() {
        return "";
    }
}
