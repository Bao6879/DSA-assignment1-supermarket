package com.example.dsaassignment1.supermarketComponents;

import com.example.dsaassignment1.linkedList.BaoList;

public class Shelf extends Components {
    private int number;
    private BaoList <Goods> goods;
    public Shelf(int number) {
        setNumber(number);
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public BaoList <Goods> getGoods() {
        return goods;
    }
    @Override
    public int getTotalSize() {
        int totalSize = 0;
        for (Goods good : goods)
            totalSize++;
        return totalSize;
    }
    @Override
    public String toString() {
        return "Shelf Number: " + number;
    }

}
