package com.example.dsaassignment1.supermarketComponents;

import com.example.dsaassignment1.linkedList.BaoList;

public class FloorArea extends Components {
    private String title;
    private int level;
    private BaoList <Aisle> aisles;
    public FloorArea(String title, int level) {
        setTitle(title);
        setLevel(level);
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
}
