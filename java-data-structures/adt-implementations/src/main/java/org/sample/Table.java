package org.sample;

/**
 * It's a mapping between values and numeric keys.
 * The key variability range must be known a priori.
 */
public interface Table extends Container{
    /**
     * Insert a value at the given key position.
     * @param key Position in the table.
     * @param value The value to insert.
     * @throws InvalidKeyTableException If the given key does not belong to the key variability range.
     */
    void insert(int key, Object value);

    /**
     * Remove a value from the given key position.
     * @param key The position of the value to remove.
     * @throws InvalidKeyTableException If the given key does not belong to the key variability range.
     */
    void remove(int key);

    /**
     * Get the value associated to the given key.
     * @param key The key of the element to get.
     * @return The element associated to the given key. Null if no association with the given key is found.
     * @throws InvalidKeyTableException If the given key does not belong to the key variability range.
     */
    Object find(int key);
}

/**
 * Implementation using simple array with fixed dimension.
 * @author Scala28
 */
class ArrayTable implements Table{
    private Object[] v;
    private int count;

    public ArrayTable(int dim){
        if(dim <= 0)
            throw new IllegalArgumentException();
        v = new Object[dim];
        makeEmpty();
    }
    public void makeEmpty(){
        count = 0;
        for(int i=0; i<v.length; i++)
            v[i] = null;
    }
    public boolean isEmpty(){
        return count == 0;
    }
    private void checkKey(int key) throws InvalidKeyTableException{
        if(key < 0 || key >= v.length)
            throw new InvalidKeyTableException();
    }
    public void insert(int key, Object value) throws InvalidKeyTableException{
        checkKey(key);
        if(v[key] == null)
            count++;
        v[key] = value;
    }
    public void remove(int key) throws InvalidKeyTableException{
        checkKey(key);
        if(v[key] != null){
            count --;
            v[key] = null;
        }
    }
    public Object find(int key) throws InvalidKeyTableException{
        checkKey(key);
        return v[key];
    }
    public float loadFactor(){
        return (count + 0.0f)/v.length;
    }
}
