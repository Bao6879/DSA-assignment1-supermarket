package com.example.dsaassignment1.linkedList;

public class BaoNode <E> {
    private BaoNode <E> next;
    private int depth;
    private E content;
    public BaoNode(E content) {
        setNext(null);
        setContent(content);
    }
    public BaoNode <E> getNext() {
        return next;
    }
    public void setNext(BaoNode <E> next) {
        this.next = next;
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
