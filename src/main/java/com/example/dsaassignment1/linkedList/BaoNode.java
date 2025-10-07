package com.example.dsaassignment1.linkedList;

public class BaoNode {
    private BaoNode next;
    private Object content;
    private BaoList baoList;
    public BaoNode(Object content) {
        setNext(null);
        setContent(content);
    }
    public BaoNode getNext() {
        return next;
    }
    public void setNext(BaoNode next) {
        this.next = next;
    }
    public Object getContent() {
        return content;
    }
    public void setContent(Object content) {
        this.content = content;
    }
    public BaoList getBaoList() {
        return baoList;
    }
    public void setBaoList(BaoList baoList) {
        this.baoList = baoList;
    }
}
