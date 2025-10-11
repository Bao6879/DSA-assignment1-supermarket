package main.supermarketComponents;

import main.linkedList.BaoList;

import java.util.Objects;

public class Shelf extends Components {
    private int number;
    private BaoList <Goods> goods;
    public Shelf(int number) {
        setNumber(number);
        goods = new BaoList<>();
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
    public void setGoods(BaoList <Goods> goods) {
        this.goods = goods;
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
        return "Shelf: " + number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return number == shelf.number;
    }
}
