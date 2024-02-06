package org.sample;

/**
 * Dictionary realize a mapping function between the key set and the value set.
 */
public interface Dictionary extends Container {
    /**
     * Add a new association key-value to the dictionary.
     * @param key
     * @param value
     * @throws IllegalArgumentException If the given key of the given value are null.
     */
    void insert(Object key, Object value);

    /**
     * Remove the first association found for the given key.
     * @param key
     * @return The value of the removed association.
     * @throws IllegalArgumentException If the given key is null.
     * @throws EmptyDictionaryException If the dictionary does not contain any association.
     */
    Object remove(Object key);
    /**
     * Remove all the association found for the given key.
     * @param key
     * @return All the values associated to the given key.
     * @throws IllegalArgumentException If the given key is null.
     * @throws EmptyDictionaryException If the dictionary does not contain any association.
     */
    Object[] removeAll(Object key);

    /**
     * Find the first value associated to the given key.
     * @param key
     * @return The value of the fisrt association found.
     * @throws IllegalArgumentException If the given key is null.
     */
    Object find(Object key);
    /**
     * Find all the associated values for the given key.
     * @param key
     * @return All the value associated to the given key.
     * @throws IllegalArgumentException If the given key is null.
     */
    Object[] findAll(Object key);

    /**
     * @return All the keys contained in the dictionary, without repeating keys.
     */
    Object[] keys();
}

/**
 * Inherits from Dictionary all the properties. Objects are kept in order.
 */
interface SortedDictionary extends Dictionary{
    /**
     * Add a new association key-value to the dictionary.
     * @param key
     * @param value
     * @throws IllegalArgumentException If the given key or the given value are null,
     *          or if the given key isn't an instance of a class that implements Comparable.
     */
    void insert(Comparable key, Object value);

    /**
     * @return An array containing all the keys() kept sorted, without repeating keys.
     */
    Comparable[] sortedKeys();
}

/**
 * Implementation using simple array with dynamic dimension.
 * @author Scala28
 */
class ArrayDictionary implements Dictionary{
    private Pair[] p;
    private int pSize;
    private static final int INITIAL_CAPACITY = 1;
    public ArrayDictionary(){
        p = new Pair[INITIAL_CAPACITY];
        makeEmpty();
    }
    public void makeEmpty(){pSize = 0;}
    public boolean isEmpty(){return pSize==0;}
    //O(1)
    public void insert(Object key, Object value){
        if(value == null)
            throw new IllegalArgumentException();
        if(find(key) != null){
            for(int i=0; i<pSize; i++){
                if(p[i].getKey().equals(key)){
                    p[i].addValue(value);
                    break;
                }
            }
        }else{
            if(pSize == p.length)
                p = ArrayUtil.convertArray(ArrayUtil.resize(p, p.length*2), Pair.class);
            Pair pair = new Pair(key);
            pair.addValue(value);
            p[pSize++] = pair;
        }
    }
    //O(n)
    public Object remove(Object key){
        if(isEmpty())
            throw new EmptyDictionaryException();
        if(find(key) != null){
            for(int i=0; i<pSize; i++){
                if(p[i].getKey().equals(key)){
                    Object ret = p[i].remove();
                    if(p[i].vSize == 0){
                        p[i] = p[pSize-1];
                        pSize--;
                    }
                    return ret;
                }
            }
        }
        return null;
    }
    //O(n)
    public Object[] removeAll(Object key){
        if(isEmpty())
            throw new EmptyDictionaryException();
        if(find(key) != null){
            for(int i=0; i<pSize; i++){
                if(p[i].getKey().equals(key)){
                    Object[] ret = p[i].getValues();
                    p[i] = p[pSize-1];
                    pSize--;
                    return ret;
                }
            }
        }
        return null;
    }
    //O(n)
    public Object find(Object key){
        if(key == null)
            throw new IllegalArgumentException();
        for(int i=0; i<pSize; i++){
            if(p[i].getKey().equals(key))
                return p[i].getValues()[0];
        }
        return null;
    }
    //O(n)
    public Object[] findAll(Object key){
        if(key == null)
            throw new IllegalArgumentException();
        for(int i=0; i<pSize; i++)
            if(p[i].getKey().equals(key))
                return p[i].getValues();
        return new Object[]{};
    }
    public Object[] keys(){
        Object[] keys = new Object[pSize];
        for(int i=0; i<pSize; i++){
            keys[i] = p[i].getKey();
        }
        return keys;
    }
    public class Pair{
        private Object[] values;
        private int vSize;
        private Object key;
        private static final int INITIAL_CAPACITY = 1;
        public Pair(Object key){
            values = new Object[INITIAL_CAPACITY];
            vSize = 0;
            this.key = key;
        }
        public Object getKey(){return key;}
        public Object[] getValues(){
            Object[] ret = new Object[vSize];
            System.arraycopy(values, 0, ret, 0, vSize);
            return ret;
        }
        public void addValue(Object value){
            if(vSize == values.length)
                values = ArrayUtil.resize(values, values.length*2);
            values[vSize++] = value;
        }
        public Object remove(){
            Object ret = values[0];
            values[0] = values[vSize-1];
            vSize--;
            return ret;
        }
    }
}

/**
 * Implementation of SortedDictionary using simple array with dynamic dimension.
 * @author Scala28
 */
class ArraySortedDictionary implements SortedDictionary{
    private Pair[] p;
    private int pSize;
    private static final int INITIAL_CAPACITY = 1;
    public ArraySortedDictionary(){
        p = new Pair[INITIAL_CAPACITY];
        makeEmpty();
    }
    public void makeEmpty(){pSize=0;}
    public boolean isEmpty(){return pSize==0;}
    public void insert(Object key, Object value) throws IllegalArgumentException{
        throw new IllegalArgumentException();
    }
    //O(n)
    public void insert(Comparable key, Object value){
        if(value == null)
            throw new IllegalArgumentException();
        if(find(key) != null){
            int pos = binarySearch(0, pSize-1, key);
            p[pos].addValue(value);
        }else{
            if(pSize == p.length)
                p = ArrayUtil.convertArray(ArrayUtil.resize(p, p.length*2), Pair.class);
            int i = pSize-1;
            while(i>=0 && p[i].getKey().compareTo(key) > 0){
                p[i+1] = p[i];
                i--;
            }
            Pair pair = new Pair(key);
            pair.addValue(value);
            p[i+1] = pair;
            pSize++;
        }
    }
    //O(n)
    public Object remove(Object key){
        if(isEmpty())
            throw new EmptyDictionaryException();
        if(find(key) != null)
        {
            int pos = binarySearch(0, pSize, (Comparable)key);
            Object ret = p[pos].remove();
            if(p[pos].vSize == 0){
                for(int i=pos; i<pSize-1; i++){
                    p[i] = p[i+1];
                }
                pSize--;
            }
            return ret;
        }
        return null;
    }
    //O(n)
    public Object[] removeAll(Object key){
        if(isEmpty())
            throw new EmptyDictionaryException();
        if(find(key) != null){
            int pos = binarySearch(0, pSize-1, (Comparable)key);
            Object[] ret = new Object[p[pos].vSize];
            System.arraycopy(p[pos].getValues(), 0, ret, 0, ret.length);
            for(int i=pos; i<pSize-1; i++){
                p[i] = p[i+1];
            }
            pSize--;
            return ret;
        }
        return null;
    }
    //O(logn)
    public Object find(Object key){
        if(key == null || !(key instanceof Comparable))
            throw new IllegalArgumentException();
        int pos = binarySearch(0, pSize-1, (Comparable)key);
        if(pos >= 0)
            return p[pos].getValues()[0];
        else return null;
    }
    //O(logn)
    public Object[] findAll(Object key){
        if(key == null || !(key instanceof Comparable))
            throw new IllegalArgumentException();
        int pos = binarySearch(0, pSize-1, (Comparable)key);
        if(pos >= 0)
            return p[pos].getValues();
        else return new Object[]{};
    }
    public Comparable[] sortedKeys(){
        Comparable[] keys = new Comparable[pSize];
        for(int i=0; i<pSize; i++){
            keys[i] = p[i].getKey();
        }
        return keys;
    }
    public Object[] keys(){return sortedKeys();}
    private int binarySearch(int low, int high, Comparable target){
        if(low > high)
            return -1;
        int mid = low + (high-low)/2;
        if(p[mid].getKey().compareTo(target) == 0)
            return mid;
        else if(p[mid].getKey().compareTo(target) > 0)
            return binarySearch(low, mid-1, target);
        else
            return binarySearch(mid+1, high, target);
    }
    public class Pair{
        private Comparable key;
        private Object[] values;
        private int vSize;
        private static final int INITIAL_CAPACITY = 1;
        public Pair(Comparable key){
            this.key = key;
            values = new Object[INITIAL_CAPACITY];
        }
        public Comparable getKey(){return key;}
        public Object[] getValues(){
            Object[] ret = new Object[vSize];
            System.arraycopy(values, 0, ret, 0, vSize);
            return ret;
        }
        public void addValue(Object value){
            if(vSize == values.length)
                values = ArrayUtil.resize(values, values.length*2);
            values[vSize++] = value;
        }
        public Object remove(){
            Object ret = values[0];
            values[0] = values[vSize-1];
            vSize--;
            return ret;
        }
    }
}
