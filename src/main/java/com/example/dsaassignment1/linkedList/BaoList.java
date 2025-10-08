package com.example.dsaassignment1.linkedList;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class BaoList <E> implements Iterable <E>{
    private BaoNode <E> head;
    private int size;
    public BaoList() {

    }
    public BaoNode <E> getHead() {
        return head;
    }
    public void setHead(BaoNode <E> head) {
        this.head = head;
    }
    public void addNode(BaoNode <E> node) {
        if (head == null) {
            head = node;
            return;
        }
        BaoNode <E> newNode = head;
        while (newNode.getNext() != null) {
            newNode = newNode.getNext();
        }
        newNode.setNext(node);
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public BaoNode <E> searchNode(BaoNode <E> node) {
        BaoNode <E> current = head;
        while (current != null) {
            if (node.equals(current))
                return current;
            current = current.getNext();
        }
        return null;
    }
    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new BaoIterator <E> (head);
    }
}
