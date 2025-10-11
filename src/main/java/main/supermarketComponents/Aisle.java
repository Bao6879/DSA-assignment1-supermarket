package main.supermarketComponents;

import main.linkedList.BaoList;

import java.util.Objects;

public class Aisle extends Components{
    private String name;
    private double length, width, temperature;
    private BaoList <Shelf> shelves;
    public Aisle(String name, double length, double width, double temperature) {
        setName(name);
        setLength(length);
        setWidth(width);
        setTemperature(temperature);
        shelves = new BaoList<>();
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

    public void setShelves(BaoList <Shelf> shelves) {
        this.shelves = shelves;
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
        return "Aisle: "+name+"; length: "+length+"; width: "+width+"; temperature: "+temperature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aisle aisle = (Aisle) o;
        return Double.compare(length, aisle.length) == 0 && Double.compare(width, aisle.width) == 0 && Double.compare(temperature, aisle.temperature) == 0 && Objects.equals(name, aisle.name);
    }
}
