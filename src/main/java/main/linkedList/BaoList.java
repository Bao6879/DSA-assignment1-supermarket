package main.linkedList;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

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
        size++;
        if (head == null) {
            head = node;
            return;
        }
        BaoNode <E> newNode = head;
        while (newNode.getNext() != null) {
            newNode = newNode.getNext();
        }
        newNode.setNext(node);
        node.setPrev(newNode);
    }
    public BaoNode <E> removeNode(BaoNode <E> node) {
        BaoNode <E> delNode = searchNode(node);
        if (delNode == null) {
            return null;
        }
        if (delNode.getNext() != null)
            delNode.getNext().setPrev(delNode.getPrev());
        if (delNode.getPrev() != null)
            delNode.getPrev().setNext(delNode.getNext());
        size--;
        return delNode;
    }
    public void clear()
    {
        size=0;
        head = null;
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
            if (node.getContent().equals(current.getContent()))
                return current;
            current = current.getNext();
        }
        return null;
    }
    public BaoNode <E> getNode(int index) {
        BaoNode <E> current = head;
        int num=0;
        while (current != null && num<index) {
            current = current.getNext();
            num++;
        }
        if (index>num)
            return null;
        return current;
    }
    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new BaoIterator <E> (head);
    }
}
