package org.sample;

public interface Deque extends Container {
    void addFirst(Object obj);
    void addLast(Object obj);
    Object removeFirst();
    Object removeLast();
    Object getFirst();
    Object getLast();
}
class FixedDeque implements Deque{
    protected Object[] v;
    protected int front, back;
    public FixedDeque(){
        v = new Object[100];
        makeEmpty();
    }
    public FixedDeque(int dim) throws IllegalArgumentException{
        if(dim <= 0)
            throw new IllegalArgumentException();
        v = new Object[dim];
        makeEmpty();
    }
    public void makeEmpty(){
        front = back = 0;
    }
    public boolean isEmpty(){
        return front == back;
    }
    public void addLast(Object obj) throws FullDequeException{
        if(increment(back) == front)
            throw new FullDequeException();
        v[back] = obj;
        back = increment(back);
    }
    public Object getFirst() throws EmptyDequeException{
        if(isEmpty())
            throw new EmptyDequeException();
        return v[front];
    }
    public Object removeFirst() throws EmptyDequeException{
        Object obj = getFirst();
        front = increment(front);
        return obj;
    }
    protected int increment(int index){
        return (index+1)%v.length;
    }
    public void addFirst(Object obj) throws FullDequeException{
        if(decrement(front) == back)
            throw new FullDequeException();
        front = decrement(front);
        v[front] = obj;
    }
    public Object getLast() throws EmptyDequeException{
        if(isEmpty())
            throw new EmptyDequeException();
        return v[decrement(back)];
    }
    public Object removeLast() throws EmptyDequeException{
        Object obj = getLast();
        back = decrement(back);
        return obj;
    }
    protected int decrement(int index){
        if(index > 0)
            return index-1;
        else
            return v.length-1;
    }
}
class DynamicDeque extends FixedDeque{
    public DynamicDeque(){}
    public DynamicDeque(int dim) throws IllegalArgumentException{
        super(dim);
    }
    @Override
    public void addLast(Object obj){
        if(increment(back) == front){
            v = ArrayUtil.resize(v, v.length*2);
            if(back < front){
                System.arraycopy(v, 0, v, v.length/2, back);
                back += v.length/2;
            }
        }
        super.addLast(obj);
    }
    @Override
    public void addFirst(Object obj){
        if(decrement(front) == back){
            v = ArrayUtil.resize(v, v.length*2);
            if(back < front){
                System.arraycopy(v, 0, v, v.length/2, back);
                back += v.length/2;
            }
        }
        super.addFirst(obj);
    }
}
