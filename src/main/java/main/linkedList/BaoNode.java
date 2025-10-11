package main.linkedList;

import main.supermarketComponents.Aisle;
import main.supermarketComponents.FloorArea;
import main.supermarketComponents.Shelf;

public class BaoNode <E> {
    private BaoNode <E> next, prev;
    private int depth;
    private E content;
    public BaoNode(E content) {
        setPrev(null);
        setNext(null);
        setContent(content);
        if (content != null) {
            if (content instanceof FloorArea)
                setDepth(0);
            else if (content instanceof Aisle)
                setDepth(1);
            else if (content instanceof Shelf)
                setDepth(2);
            else
                setDepth(3);
        }
    }
    public BaoNode <E> getNext() {
        return next;
    }
    public void setNext(BaoNode <E> next) {
        this.next = next;
    }
    public BaoNode <E> getPrev() {
        return prev;
    }
    public void setPrev(BaoNode <E> prev) {
        this.prev = prev;
    }
    public E getContent() {
        return content;
    }
    public void setContent(E content) {
        this.content = content;
    }
    public int getDepth() {
        return depth;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }
}
