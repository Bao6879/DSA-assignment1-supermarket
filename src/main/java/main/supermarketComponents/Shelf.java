package main.supermarketComponents;

import main.Utilities;
import main.linkedList.BaoList;
import main.linkedList.BaoNode;

public class Shelf extends Components {
    private int number;
    private BaoList <Goods> goods;
    public Shelf(int number) {
        setNumber(number);
        setAttributes();
        goods = new BaoList<>();
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    @Override
    public BaoList <Goods> getInnerList() {
        return goods;
    }
    @Override
    public void setInnerList(BaoList <?> goods) {
        if (goods!=null && goods.getNode(0)!=null && goods.getNode(0).getContent() instanceof Goods)
            this.goods = Utilities.copyList(goods);
    }

    @Override
    public double getValue() {
        double value=0.0;
        for (Goods item : goods)
            value+=item.getValue();
        return value;
    }

    @Override
    public double similarScore(Components other) {
        if (other==null)
            return Double.MAX_VALUE-1;
        Shelf otherShelf = (Shelf) other;
        return otherShelf.getNumber() - this.number;
    }

    @Override
    public String getName() {
        return getNumber()+"";
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
    public String toRawString() {
        return number + "";
    }

    @Override
    public void setAttributes() {
        attributes = new BaoList();
        attributes.addNode(new BaoNode(Utilities.defaultInteger));
    }

    @Override
    public BaoList getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return number == shelf.number;
    }
}
