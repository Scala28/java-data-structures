package org.sample;

/**
 * Manage data based on First In - First Out.
 * The only accessible data is the first data to dequeue.
 */
public interface Queue extends Container{

    /**
     * Add an element in the last position of the queue.
     * @param obj The element to add.
     */
    void enqueue(Object obj);

    /**
     * Remove the first element from the queue.
     * @return The removed element.
     * @throws EmptyQueueException If the queue does not contain element.
     */
    Object dequeue();

    /**
     * Get the first element from the queue.
     * @return The first element.
     * @throws EmptyQueueException If the queue does not contain element.
     */
    Object getFront();
}

/**
 * Implementation using circular array with fixed dimension.
 * @author Scala28
 */
class FixedArrayQueue implements Queue{
    //Instance fields
    protected Object[] v;
    protected int front, back;

    //Constructors
    public FixedArrayQueue(){
        v = new Object[100];
        makeEmpty();
    }
    public FixedArrayQueue(int dim) throws IllegalArgumentException{
        if(dim <= 0)
            throw new IllegalArgumentException();
        v = new Object[dim];
        makeEmpty();
    }
    //Methods implementations
    public void makeEmpty(){
        front = back = 0;
    }
    public boolean isEmpty(){
        return front == back;
    }
    //O(1)
    public void enqueue(Object obj) throws FullQueueException{
        if(increment(back) == front)
            throw new FullQueueException();
        v[back] = obj;
        back = increment(back);
    }
    protected int increment(int index){
        return (index+1)%v.length;
    }
    //O(1)
    public Object dequeue() throws EmptyQueueException{
        Object obj = getFront();
        front = increment(front);
        return obj;
    }
    //O(1)
    public Object getFront() throws EmptyQueueException{
        if(isEmpty())
            throw new EmptyQueueException();
        return v[front];
    }
}
/**
 * Implementation using circular array with fixed dimension.
 * @author Scala28
 */
class DynamicArrayQueue extends FixedArrayQueue {
    public DynamicArrayQueue(){}
    public DynamicArrayQueue(int dim) throws IllegalArgumentException{
        super(dim);
    }
    //O(1) - amortized analysis
    @Override
    public void enqueue(Object obj){
        if(increment(back) == front){
            v = ArrayUtil.resize(v, v.length*2);
            if(back < front){
                System.arraycopy(v, 0, v, v.length/2, back);
                back += v.length/2;
            }
        }
        super.enqueue(obj);
    }
}

/**
 * Implementation using linked list --> dynamic dimension.
 * @author Scala28
 */
class LinkedListQueue implements Queue{
    private LinkedList list;
    public LinkedListQueue(){
        list = new LinkedList();
        makeEmpty();
    }
    public void makeEmpty(){list.makeEmpty();}
    public boolean isEmpty(){
        return list.isEmpty();
    }
    //O(1)
    public void enqueue(Object obj){
        list.addLast(obj);
    }
    //O(1)
    public Object dequeue() throws EmptyQueueException{
        try{
            return list.removeFirst();
        }catch(EmptyLinkedListException e){
            throw new EmptyQueueException();
        }
    }
    //O(1)
    public Object getFront() throws EmptyQueueException{
        try{
            return list.getFirst();
        }catch(EmptyLinkedListException e){
            throw new EmptyQueueException();
        }
    }
}