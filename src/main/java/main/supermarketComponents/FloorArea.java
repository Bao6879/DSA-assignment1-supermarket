package main.supermarketComponents;

import main.Utilities;
import main.linkedList.BaoList;
import main.linkedList.BaoNode;

import java.util.Objects;

public class FloorArea extends Components {
    private String name;
    private int level;
    private BaoList <Aisle> aisles;
    public FloorArea(String name, int level) {
        setName(name);
        setLevel(level);
        setAttributes();
        aisles = new BaoList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public BaoList <Aisle> getInnerList() {
        return aisles;
    }

    @Override
    public void setInnerList(BaoList <?> aisles) {
        if (aisles!=null && aisles.getNode(0)!=null && aisles.getNode(0).getContent() instanceof Aisle)
            this.aisles = Utilities.copyList(aisles);
    }

    @Override
    public double similarScore(Components other) {
        return 0;
    }

    @Override
    public double getValue() {
        double value=0.0;
        for (Aisle aisle : aisles)
            value+=aisle.getValue();
        return value;
    }

    public Aisle findAisle(Aisle aisle) {
        for (Aisle a : aisles) {
            if (a == aisle)
                return a;
        }
        return null;
    }

    @Override
    public int getTotalSize() {
        int totalSize = 0;
        for (Aisle aisle : aisles) {
            totalSize+= aisle.getTotalSize();
        }
        return totalSize;
    }

    @Override
    public String toString() {
        return "Floor Area: "+ name +"; Level: "+level;
    }

    @Override
    public String toRawString() {
        return name +", "+level;
    }

    @Override
    public void setAttributes() {
        attributes = new BaoList();
        attributes.addNode(new BaoNode<>(Utilities.defaultString));
        attributes.addNode(new BaoNode<>(Utilities.defaultInteger));
    }

    @Override
    public BaoList getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloorArea floorArea = (FloorArea) o;
        return Objects.equals(name, floorArea.name);
    }
}
