package org.sample;

import java.util.NoSuchElementException;

interface ListIterator{
    Object next();
    boolean hasNext();
    void add(Object obj);
    void remove();
}
public class LinkedList implements Container{
    private ListNode header, tail;
    public LinkedList(){
        makeEmpty();
    }
    public void makeEmpty(){
        header = tail = new ListNode();
    }
    public boolean isEmpty(){
        return header==tail;
    }
    public Object getFirst(){
        if(isEmpty())
            throw new EmptyLinkedListException();
        return header.getNext().getValue();
    }
    public Object getLast(){
        if(isEmpty())
            throw new EmptyLinkedListException();
        return tail.getValue();
    }
    public void addFirst(Object value){
        header.setValue(value);
        ListNode node = new ListNode();
        node.setNext(header);
        header = node;
    }
    public Object removeFirst(){
        Object e = getFirst();
        header = header.getNext();
        header.setValue(null);
        return  e;
    }
    public void addLast(Object value){
        tail.setNext(new ListNode(value));
        tail = tail.getNext();
    }
    public Object removeLast(){
        Object ret = getLast();

        ListNode temp = header;
        while(temp.getNext() != tail)
            temp = temp.getNext();
        tail = temp;
        tail.setNext(null);
        return ret;
    }
    public ListIterator getIterator(){
        return new LinkedListIterator(header);
    }
    private class LinkedListIterator implements ListIterator{
        private ListNode current, previous;
        public LinkedListIterator(ListNode head){
            current = head;
            previous = null;
        }
        public boolean hasNext(){
            return current.getNext() != null;
        }
        public Object next(){
            if(!hasNext())
                throw new NoSuchElementException();
            previous = current;
            current = current.getNext();
            return current.getValue();
        }
        public void add(Object obj){
            ListNode node = new ListNode(obj);
            current.setNext(node);
            previous = current;
            current = node;

            if(current.getNext() == null)
                tail = current;
        }
        public void remove(){
            if(previous == null)
                throw new IllegalStateException();
            previous.setNext(current.getNext());
            current = previous;

            if(current.getNext() == null)
                tail = current;
            previous = null;
        }
    }
    private class ListNode{
        private Object value;
        private ListNode next;

        public ListNode(Object value){
            this.value = value;
        }
        public ListNode(){
            value = null;
            next = null;
        }
        public Object getValue(){return value;}
        public ListNode getNext(){return next;}
        public void setValue(Object value){
            this.value = value;
        }
        public void setNext(ListNode next){
            this.next = next;
        }
    }
}
