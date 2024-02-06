package org.sample;

/**
 * Manage data based on Last In - First-Out.
 * The only accessible data is the first one to pop.
 */
public interface Stack extends Container {
    /**
     * Add an element to the top of the stack.
     * @param obj The element to add.
     */
    void push(Object obj);

    /**
     * Remove the top element from the stack.
     * @return The removed element.
     * @throws EmptyStackException If the stack does not contain element.
     */
    Object pop();

    /**
     * Get the top element from the stack.
     * @return The top element.
     * @throws EmptyStackException If the stack does not contain element.
     */
    Object top();
}

/**
 * Implementation using simple array with fixed dimension.
 * @author Scala28
 */
class FixedArrayStack implements Stack{
    //Instance fields
    protected Object[] v;
    protected int vSize;
    //Constructors
    public FixedArrayStack(){
        v = new Object[100];
        makeEmpty();
    }
    public FixedArrayStack(int dim) throws IllegalArgumentException{
        if(dim <= 0)
            throw new IllegalArgumentException();
        v = new Object[dim];
        makeEmpty();
    }
    //Methods implementations
    public boolean isEmpty(){
        return vSize == 0;
    }
    public void makeEmpty(){
        vSize = 0;
    }
    //O(1)
    public void push(Object obj) throws FullStackException{
        if(vSize == v.length)
            throw new FullStackException();
        v[vSize++] = obj;
    }
    //O(1)
    public Object pop() throws EmptyStackException{
        Object obj = top();
        vSize--;
        return obj;
    }
    //O(1)
    public Object top() throws EmptyStackException{
        if(isEmpty())
            throw new EmptyStackException();
        return v[vSize-1];
    }
}

/**
 * Implementation using simple array with dynamic dimension.
 * @author Scala28
 */
class DynamicArrayStack extends FixedArrayStack {
    public DynamicArrayStack(){}
    public DynamicArrayStack(int dim) throws IllegalArgumentException{
        super(dim);
    }
    //O(1) - amortized analysis
    @Override
    public void push(Object obj){
        if(vSize == v.length)
            v = ArrayUtil.resize(v, v.length*2);
        super.push(obj);
    }
}

/**
 * DynamicArrayQueue extended class that accepts only String type elements for push
 * @author Scala28
 */
class StringArrayStack extends DynamicArrayStack {
    public StringArrayStack(){}
    public StringArrayStack(int dim)throws IllegalArgumentException{
        super(dim);
    }
    @Override
    public void push(Object obj)throws InvalidTypeException{
        if(!(obj instanceof String))
            throw new InvalidTypeException();
        super.push(obj);
    }
}

/**
 * Implementation using linked list --> dynamic dimension.
 * @author Scala28
 */
class LinkedListStack implements Stack{
    private LinkedList list;
    public LinkedListStack(){
        list = new LinkedList();
        makeEmpty();
    }
    public void makeEmpty(){list.makeEmpty();}
    public boolean isEmpty(){
        return list.isEmpty();
    }
    //O(1)
    public void push(Object obj){
        list.addFirst(obj);
    }
    //O(1)
    public Object pop() throws EmptyStackException{
        try{
            return list.removeFirst();
        }catch(EmptyLinkedListException e){
            throw new EmptyStackException();
        }
    }
    //O(1)
    public Object top() throws EmptyStackException{
        try{
            return list.getFirst();
        }catch(EmptyLinkedListException e){
            throw new EmptyStackException();
        }
    }
}

