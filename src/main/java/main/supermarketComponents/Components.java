package main.supermarketComponents;

import main.linkedList.BaoList;

public abstract class Components {
    protected static BaoList attributes;
    public abstract String getName();
    public abstract int getTotalSize();
    public abstract String toString();
    public abstract String toRawString();
    public abstract void setAttributes();
    public abstract BaoList getAttributes();
    public abstract BaoList getInnerList();
    public abstract void setInnerList(BaoList <?> innerList);
    public abstract double similarScore(Components other);
    public abstract double getValue();
}
