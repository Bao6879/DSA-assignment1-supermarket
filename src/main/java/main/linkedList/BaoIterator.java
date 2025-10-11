package main.linkedList;

import java.util.Iterator;

public class BaoIterator <E> implements Iterator<E> {
    private BaoNode<E> pos;
    public BaoIterator(BaoNode<E> pos) {
        this.pos = pos;
    }

    @Override
    public boolean hasNext() {
        return pos != null;
    }

    @Override
    public E next() {
        BaoNode<E> tmp = pos;
        pos=pos.getNext();
        return tmp.getContent();
    }
}
