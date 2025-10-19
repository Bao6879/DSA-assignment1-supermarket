package main.linkedList;

import main.supermarketComponents.Components;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class BaoList <E> implements Iterable <E>{
    private BaoNode <E> head;
    private int size;
    public BaoList() {

    }
    public void addNode(BaoNode <E> node1) {  //ADD AN E
        if (node1 == null)
            return;
        size++;
        BaoNode <E> node=new BaoNode<>(node1.getContent());
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
    public void addNode(E entity) {  //ADD AN E
        if (entity == null)
            return;
        size++;
        BaoNode <E> node=new BaoNode<>(entity);
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
    public void removeNode(BaoNode <E> node) { //ADD AN E
        BaoNode <E> delNode = searchNode(node);
        if (delNode == null) {
            return;
        }
        if (delNode.getNext() != null) {
            delNode.getNext().setPrev(delNode.getPrev());
            if (delNode ==head) {
                head = delNode.getNext();
            }
        }
        if (delNode.getPrev() != null)
            delNode.getPrev().setNext(delNode.getNext());
        if (delNode.getNext() == null && delNode.getPrev() == null && head == delNode) {
            head = null;
        }
        delNode.setNext(null);
        delNode.setPrev(null);
        size--;
    }
    public void removeNode(E node) { //ADD AN E
        BaoNode <E> delNode =searchNode(new BaoNode(node));
        if (delNode == null) {
            return;
        }
        if (delNode.getNext() != null) {
            delNode.getNext().setPrev(delNode.getPrev());
            if (delNode ==head) {
                head = delNode.getNext();
            }
        }
        if (delNode.getPrev() != null)
            delNode.getPrev().setNext(delNode.getNext());
        if (delNode.getNext() == null && delNode.getPrev() == null && head == delNode) {
            head = null;
        }
        delNode.setNext(null);
        delNode.setPrev(null);
        size--;
    }
    public BaoList <E> subList(int end) {
        BaoList <E> subList = new BaoList();
        BaoNode <E> current=head;
        if (end>=getSize() || end<0)
            return null;
        int index=0;
        while (index <= end) {
            subList.addNode(current);
            current = current.getNext();
            index++;
        }
        return subList;
    }
    public void clear()
    {
        size=0;
        head = null;
    }
    public int getSize() {
        return size;
    }
    public BaoNode <E> searchNode(BaoNode <E> node) {
        if (node==null)
            return null;
        BaoNode <E> current = head;
        while (current != null) {
            if (node.getContent().equals(current.getContent()))
                return current;
            current = current.getNext();
        }
        return null;
    }
    public BaoNode <E> searchNode(E entity) {
        if (entity==null)
            return null;
        BaoNode <E> current = head, node=new BaoNode<>(entity);
        while (current != null) {
            if (node.getContent().equals(current.getContent()))
                return current;
            current = current.getNext();
        }
        return null;
    }
    public boolean contains(BaoNode <E> node) {
        if (node==null)
            return false;
        return searchNode(node) != null;
    }
    public boolean contains(E entity) {
        if (entity==null)
            return false;
        return searchNode(new BaoNode<>(entity)) != null;
    }
    public BaoNode <E> similarNode(BaoNode <E> node) {
        BaoNode <E> current=head, most=head;
        double minSimilarScore=Double.MAX_VALUE;
        while (current != null) {
            if (((Components)node.getContent()).similarScore((Components) current.getContent())<minSimilarScore) {
                most=current;
                minSimilarScore=((Components)node.getContent()).similarScore((Components) current.getContent());
            }
            current = current.getNext();
        }
        return most;
    }
    public BaoNode <E> getNode(int index) {
        BaoNode <E> current = head;
        int num=0;
        if (index<0)
            return null;
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
