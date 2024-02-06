package org.sample;

/**
 * Given a table with a fixed dimension, keys outside the key variability range are
 * mapped to a valid index for the table using a Hash function.
 * Different keys could be mapped to the same index, therefore object are associated to their original key.
 */
public interface HashTable extends Container {
    /**
     * Add a new association key-value to the table.
     * @param key Key associated to the value. It is used to calculate the table index used to insert the value.
     * @param value The value to add.
     * @throws IllegalArgumentException If the key param is null.
     */
    void insert(Object key, Object value);

    /**
     * Remove the association given the key.
     * @param key
     * @throws IllegalArgumentException If the key param is null.
     */
    void remove(Object key);

    /**
     * Get the value associated to the given key.
     * @param key
     * @return The value associated to the given key. Null if no association with the given key is found.
     * @throws IllegalArgumentException If the key param is null.
     */
    Object find(Object key);
}
/**
 * Implementation using simple array with fixed dimension referencing LinkedLists.
 * @author Scala28
 */
class ArrayHashTable implements HashTable{
    private LinkedList[] table;
    private int count;

    public ArrayHashTable(int dim){
        if(dim <= 0)
            throw new IllegalArgumentException();
        table = new LinkedList[dim];
        makeEmpty();
    }
    public void makeEmpty(){
        count = 0;
        for(int i=0; i<table.length; i++)
            table[i] = null;
    }
    public boolean isEmpty(){
        return count == 0;
    }
    public int hashMap(Object key) throws IllegalArgumentException{
        if(key == null)
            throw new IllegalArgumentException();
        int hash = key.hashCode() % table.length;
        if(hash < 0)
            hash = -hash;
        return hash;
    }
    public void insert(Object key, Object value) throws IllegalArgumentException{
        int index = hashMap(key);
        System.out.println(index);
        //If no list with such index is found
        if(table[index] == null){
            LinkedList list = new LinkedList();
            list.addLast(new Pair(key, value));
            table[index] = list;
            count++;
            return;
        }
        //If element with such index already exists
        ListIterator iter = table[index].getIterator();
        Object temp = null;
        while(iter.hasNext()){
            temp = iter.next();
            if(((Pair)temp).getKey().equals(key))
                break;
        }
        //If a value associated to that specific key is found
        if(temp != null && key.equals(((Pair)temp).getKey())){
            //substitute the old value with the new one
            ((Pair) temp).setValue(value);
            return;
        }
        //add a new association to the List at position index
        table[index].addLast(new Pair(key, value));
    }
    public void remove(Object key) throws IllegalArgumentException{
        int index = hashMap(key);
        if(table[index] != null){
            ListIterator iter = table[index].getIterator();
            Object temp = null;
            while(iter.hasNext()){
                temp = iter.next();
                if(((Pair) temp).getKey().equals(key))
                    break;
            }
            //If a Value associated to the given key is found
            if(temp != null && key.equals(((Pair) temp).getKey())){
                iter.remove();
                if(table[index].isEmpty())
                    table[index] = null;
            }

        }
    }
    public Object find(Object key) throws IllegalArgumentException{
        int index = hashMap(key);
        if(table[index] != null){
            ListIterator iter = table[index].getIterator();
            Object temp = null;
            while(iter.hasNext()){
                temp = iter.next();
                if(((Pair) temp).getKey().equals(key))
                    break;
            }
            if(temp != null && key.equals(((Pair) temp).getKey()))
                return ((Pair) temp).getValue();
        }
        return null;
    }
    public float loadFactor() {return (count + 0.0f) / table.length;}
    private class Pair{
        private Object key;
        private Object value;
        public Pair(){
            key = null;
            value = null;
        }
        public Pair(Object key, Object value){
            this.key = key;
            this.value = value;
        }
        public Object getKey(){return key;}
        public Object getValue(){return value;}
        public void setKey(Object key){
            this.key = key;
        }
        public void setValue(Object value){
            this.value = value;
        }
    }
}
