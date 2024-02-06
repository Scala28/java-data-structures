package org.sample;

/**
 * Map realize a biunivocal mapping function between the key set and the value set.
 */
public interface Map extends Container{
    /**
     * Get the value associated to a given key.
     * @param key The key associated to the researched value.
     * @return The value associated to the give key.
     * @throws IllegalArgumentException if the given key is null.
     */
    Object get(Object key);

    /**
     * Remove the pair associated to the given key.
     * @param key The key of the pair to remove.
     * @return The value of the removed pair; null if no pair with the given key was found.
     * @throws IllegalArgumentException if the given key is null.
     * @throws EmptyMapException If the map does not contain any association.
     */
    Object remove(Object key);

    /**
     * Add a new association key-value to the map.
     * @param key
     * @param value
     * @return The old value associated to the given key; null no association was found to the given key.
     * @throws IllegalArgumentException it the given key or the given value are null.
     */
    Object put(Object key, Object value);

    /**
     * @return All the keys found in the map.
     */
    Object[] keys();
}

/**
 * Inherits from Map all the properties. Objects are kept sorted.
 */
interface SortedMap extends Map{
    /**
     * @return All the keys found in the map, sorted in ascending order.
     */
    Comparable[] sortedKeys();
}

/**
 * Implementation using simple array with dynamic dimension.
 * @author Scala28
 */
class ArrayMap implements Map{
    private Pair[] p;
    private int pSize;
    private static final int INITIAL_CAPACITY = 1;

    public ArrayMap(){
        p = new Pair[INITIAL_CAPACITY];
        makeEmpty();
    }
    public void makeEmpty(){
        pSize = 0;
    }
    public boolean isEmpty(){return pSize == 0;}
    //O(n)
    public Object get(Object key){
        if(key == null)
            throw new IllegalArgumentException();
        for(int i=0; i<pSize; i++)
            if(p[i].getKey().equals(key))
                return p[i].getValue();
        return null;
    }
    //O(n)
    public Object remove(Object key){
        if(isEmpty())
            throw new EmptyMapException();
        if(key == null)
            throw new IllegalArgumentException();
        for(int i=0; i<pSize; i++)
            if(p[i].getKey().equals(key)){
                Object obj = p[i].getValue();
                p[i] = p[pSize-1];
                pSize--;
                return obj;
            }
        return null;
    }
    //O(n)
    public Object put(Object key, Object value){
        if(value == null)
            throw new IllegalArgumentException();
        Object old = null;
        try{
            old = remove(key);
        }catch(EmptyMapException ex){}
        if(pSize == p.length)
            p = ArrayUtil.convertArray(ArrayUtil.resize(p, p.length*2), Pair.class);
        p[pSize++] = new Pair(key, value);
        return old;
    }
    public Object[] keys(){
        Object[] keys = new Object[pSize];
        for(int i=0; i<pSize; i++)
            keys[i] = p[i].getKey();
        return keys;
    }
    public class Pair{
        private Object key;
        private Object value;
        public Pair(Object k, Object v){
            key = k;
            value = v;
        }
        public Object getKey(){return key;}
        public Object getValue(){return value;}
        public void setKey(Object k){key = k;}
        public void setValue(Object v){value = v;}
    }
}

/**
 * Implementation of SortedMap using simple array with dynamic dimension.
 * @author Scala28
 */
class ArraySortedMap implements SortedMap{

    private Pair[] p;
    private int pSize;
    private static final int INITIAL_CAPACITY = 1;
    public ArraySortedMap(){
        p = new Pair[INITIAL_CAPACITY];
        makeEmpty();
    }
    public void makeEmpty(){pSize=0;}
    public boolean isEmpty(){return pSize==0;}
    //O(logn)
    public Object get(Object key) throws IllegalArgumentException{
        if(!(key instanceof Comparable))
            throw new IllegalArgumentException();
        int pos = binarySearch(0, pSize-1, (Comparable)key);
        if(pos >= 0)
            return p[pos].getValue();
        else
            return null;
    }
    //O(n)
    public Object remove(Object key) throws IllegalArgumentException{
        if(!(key instanceof Comparable))
            throw new IllegalArgumentException();
        int pos = binarySearch(0, pSize-1, (Comparable)key);
        if(pos >= 0){
            Object obj = p[pos].getValue();
            for(int i=pos; i<pSize-1; i++)
                p[i] = p[i+1];
            pSize--;
            return obj;
        }else
            return null;
    }
    //O(n)
    public Object put(Object key, Object value) throws IllegalArgumentException{
        if(key == null || value == null || !(key instanceof Comparable))
            throw new IllegalArgumentException();
        int pos = binarySearch(0, pSize-1,(Comparable)key);
        if(pos >= 0){
            Object old = p[pos].getValue();
            p[pos].setValue(value);
            return old;
        }
        if(pSize == p.length)
            p = ArrayUtil.convertArray(ArrayUtil.resize(p, p.length*2), Pair.class);
        int i = pSize-1;
        while(i >= 0 && p[i].getKey().compareTo(key) > 0){
            p[i+1] = p[i];
            i--;
        }
        p[i+1] = new Pair((Comparable)key, value);
        pSize++;
        return null;
    }
    public Comparable[] sortedKeys(){
        Comparable[] keys = new Comparable[pSize];
        for(int i=0; i<pSize; i++)
            keys[i] = p[i].getKey();
        return keys;
    }
    public Object[] keys(){
        return sortedKeys();
    }
    private int binarySearch(int low, int high, Comparable key){
        if(low > high)
            return -1;
        int mid = low +(high-low)/2;
        if(p[mid].getKey().compareTo(key) == 0)
            return mid;
        else if(p[mid].getKey().compareTo(key) > 0)
            return binarySearch(low, mid-1, key);
        else
            return binarySearch(mid+1, high, key);
    }
    public class Pair{
        private Comparable key;
        private Object value;
        public Pair(Comparable k, Object v){
            key = k;
            value = v;
        }
        public Comparable getKey(){return key;}
        public Object getValue(){return value;}
        public void setKey(Comparable k){key = k;}
        public void setValue(Object v){value = v;}
    }
}
