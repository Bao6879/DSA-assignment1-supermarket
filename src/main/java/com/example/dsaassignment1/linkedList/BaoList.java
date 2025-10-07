package com.example.dsaassignment1.linkedList;

public class BaoList {
    private BaoNode head;
    public BaoList() {

    }
    public BaoNode getHead() {
        return head;
    }
    public void setHead(BaoNode head) {
        this.head = head;
    }

    public void addNode(BaoNode node) {
        if (head == null) {
            head = node;
            return;
        }
        BaoNode newNode = head;
        while (newNode.getNext() != null) {
            newNode = newNode.getNext();
        }
        newNode.setNext(node);
    }

    public int size() {
        int size = 0;
        BaoNode current = head;
        while (current != null) {
            size++;
            size+=current.getBaoList().size();
            current = current.getNext();
        }
        return size;
    }
}
