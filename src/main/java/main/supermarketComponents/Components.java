package main.supermarketComponents;

import main.linkedList.BaoList;

public abstract class Components {
    public abstract int getTotalSize();
    public abstract String toString();
    public abstract BaoList getInnerList();
    public abstract void setInnerList(BaoList <?> innerList);
}
