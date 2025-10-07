package com.example.dsaassignment1.linkedList;

public class BaoList {
    private BaoNode head;
    public BaoList(BaoNode head) {
        setHead(head);
    }
    public BaoNode getHead() {
        return head;
    }
    public void setHead(BaoNode head) {
        this.head = head;
    }

    public void addNode(BaoNode node) {
        BaoNode newNode = head;
        while (newNode.getNext() != null) {
            newNode = newNode.getNext();
        }
        newNode.setNext(node);
    }
}
