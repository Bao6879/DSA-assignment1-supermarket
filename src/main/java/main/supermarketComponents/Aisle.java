package main.supermarketComponents;

import main.Utilities;
import main.linkedList.BaoList;
import main.linkedList.BaoNode;

import java.util.Objects;

public class Aisle extends Components{
    private String name, temperature;
    private double length, width;
    private BaoList <Shelf> shelves;
    public Aisle(String name, double length, double width, String temperature) {
        setName(name);
        setLength(length);
        setWidth(width);
        setTemperature(temperature);
        setAttributes();
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String  temperature) {
        this.temperature = temperature;
    }

    @Override
    public BaoList <Shelf> getInnerList() {
        return shelves;
    }

    @Override
    public void setInnerList(BaoList <?> shelves) {
        if (shelves!=null && shelves.getNode(0)!=null && shelves.getNode(0).getContent() instanceof Shelf)
            this.shelves = Utilities.copyList(shelves);
    }

    @Override
    public double similarScore(Components other) {
        if (other==null)
            return Double.MAX_VALUE-1;
        Aisle otherAisle = (Aisle) other;
        double difference=Math.abs(getLength()-otherAisle.getLength())+Math.abs(getWidth()-otherAisle.getWidth())+Math.abs(getTemperature().length()-otherAisle.getTemperature().length());
        difference+=Math.abs(otherAisle.getName().length()-getName().length());
        for (int i=0; i<Math.min(getName().length(), otherAisle.getName().length()); i++)
            if (getName().charAt(i)!=otherAisle.getName().charAt(i))
                difference++;
        return difference;
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
    public String toRawString() {
        return name+", "+length+", "+width+", "+temperature;
    }

    @Override
    public void setAttributes() {
        attributes = new BaoList();
        attributes.addNode(new BaoNode(Utilities.defaultString));
        attributes.addNode(new BaoNode(Utilities.defaultDouble));
        attributes.addNode(new BaoNode(Utilities.defaultDouble));
        attributes.addNode(new BaoNode(Utilities.defaultDouble));
    }

    @Override
    public BaoList getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aisle aisle = (Aisle) o;
        return Objects.equals(name, aisle.name);
    }
}
