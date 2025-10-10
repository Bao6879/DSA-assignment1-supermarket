package com.example.dsaassignment1.supermarketComponents;

import com.example.dsaassignment1.linkedList.BaoList;

import java.util.Objects;

public class FloorArea extends Components {
    private String title;
    private int level;
    private BaoList <Aisle> aisles;
    public FloorArea(String title, int level) {
        setTitle(title);
        setLevel(level);
        aisles = new BaoList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public BaoList <Aisle> getAisles() {
        return aisles;
    }

    public void setAisles(BaoList <Aisle> aisles) {
        this.aisles = aisles;
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
        return "Floor Area: "+title+"; Level: "+level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloorArea floorArea = (FloorArea) o;
        return level == floorArea.level && Objects.equals(title, floorArea.title);
    }
}
