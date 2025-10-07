package com.example.dsaassignment1.supermarketComponents;

public class FloorArea {
    private String title;
    private int level;
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
}
