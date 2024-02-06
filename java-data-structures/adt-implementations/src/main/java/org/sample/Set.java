package org.sample;

/**
 * Container of distinct objects.
 */
public interface Set extends Container {
    /**
     * Add an object to the set. Fail quietly if the set already contains the object.
     * @param obj The object to add.
     */
    void add(Object obj);

    /**
     * Determines if an element is contained in the set.
     * @param obj The object to check.
     * @return True if the element is contained; false if not.
     */
    boolean contains(Object obj);

    /**
     * Get all set's elements.
     * @return An array containing all the set's element.
     */
    Object[] toArray();
}

/**
 * Inherits from Set all the properties. Objects are kept sorted.
 */
interface SortedSet extends Set{
    /**
     * Add an element to the set. Fail quietly if the set already contains the object.
     * @param obj The element to add.
     * @throws IllegalArgumentException If the given obj is not instance of a class that implements Comparable.
     */
    void add(Comparable obj);

    /**
     * Get all set's elements kept sorted.
     * @return An array containing all the set's element kept sorted.
     */
    Comparable[] toSortedArray();
}

/**
 * Implementation using simple array with dynamic dimension.
 * @author Scala28
 */
class ArraySet implements Set{
    private static final int INITIAL_CAPACITY = 1;
    private Object[] v;
    private int vSize;

    public ArraySet(){
        v = new Object[INITIAL_CAPACITY];
        makeEmpty();
    }
    public void makeEmpty(){
        vSize = 0;
    }
    public boolean isEmpty(){
        return vSize == 0;
    }
    //O(n)
    public void add(Object obj){
        if(contains(obj))
            return;
        if(vSize == v.length)
            v = ArrayUtil.resize(v, v.length*2);
        v[vSize++] = obj;
    }
    //O(n)
    public boolean contains(Object obj){
        for(int i=0; i<vSize; i++){
            if(v[i].equals(obj))
                return true;
        }
        return false;
    }
    //O(n)
    public Object[] toArray(){
        Object[] x = new Object[vSize];
        System.arraycopy(v, 0, x, 0, vSize);
        return x;
    }
    //O(n^2)
    public static Set union(Set s1, Set s2){
        Set x = new ArraySet();
        Object[] v = s1.toArray();
        for(int i=0; i<v.length; i++)
            x.add(v[i]);

        v = s1.toArray();
        for(int i=0; i<v.length; i++)
            x.add(v[i]);
        return x;
    }
    //O(n^2)
    public static Set intersection(Set s1, Set s2){
        Set x = new ArraySet();
        Object[] v = s1.toArray();
        for(int i=0; i<v.length; i++)
            if(s2.contains(v[i]))
                x.add(v[i]);
        return x;
    }
    //O(n^2)
    public static Set subtraction(Set s1, Set s2){
        Set x = new ArraySet();
        Object[] v = s1.toArray();

        for(int i=0; i<v.length; i++)
            if(!s2.contains(v[i]))
                x.add(v[i]);
        return x;
    }
}

/**
 * Implementation of SortedSet using simple array with dynamic dimension.
 * @author Scala28
 */
class ArraySortedSet implements SortedSet{
    private static final int INITIAL_CAPACITY = 1;
    private Comparable[] v;
    private int vSize;

    public ArraySortedSet(){
        v = new Comparable[INITIAL_CAPACITY];
        makeEmpty();
    }
    public void makeEmpty(){
        vSize = 0;
    }
    public boolean isEmpty(){
        return vSize == 0;
    }

    public void add(Object obj){throw new IllegalArgumentException();}
    //O(n)
    public void add(Comparable obj){
        if(contains(obj))
            return;
        if(vSize == v.length)
            v = ArrayUtil.convertArray(ArrayUtil.resize(v, v.length*2), Comparable.class);

        v[vSize++] = obj;
        for(int i=vSize-1; i > 0; i--){
            if(v[i].compareTo(v[i-1]) < 0)
                ArrayUtil.swap(v, i, i-1);
            else
                break;
        }
    }
    //O(logn)
    public boolean contains(Object obj){
        int low = 0;
        int high = vSize-1;
        int pos = binarySearch(low, high, obj);
        return pos >= 0;
    }
    //O(n)
    public Comparable[] toSortedArray(){
        Comparable[] x = new Comparable[vSize];
        System.arraycopy(v, 0, x, 0, vSize);
        return x;
    }
    public Object[] toArray(){
        return toSortedArray();
    }
    private int binarySearch(int low, int high, Object target){
        if(low > high)
            return -1;
        int mid = low +(high-low)/2;
        if(v[mid].compareTo(target) == 0)
            return mid;
        else if(v[mid].compareTo(target) > 0)
            return binarySearch(low, mid-1, target);
        else
            return binarySearch(mid+1, high, target);
    }
    //O(nlogn)
    public static SortedSet union(SortedSet s1, SortedSet s2){
        SortedSet x = new ArraySortedSet();
        Comparable[] v1 = s1.toSortedArray();
        Comparable[] v2 = s2.toSortedArray();
        int i=0, j=0;
        while(i<v1.length && j<v2.length){
            if(v1[i].compareTo(v2[j]) < 0)
                x.add(v1[i++]);
            else if(v1[i].compareTo(v2[j]) > 0)
                x.add(v2[j++]);
            else{
                x.add(v1[i++]);
                j++;
            }
        }
        while(i < v1.length)
            x.add(v1[i++]);
        while(j < v2.length)
            x.add(v2[j++]);
        return x;

    }
    //O(nlogn)
    public static SortedSet intersection(SortedSet s1, SortedSet s2){
        SortedSet x = new ArraySortedSet();
        Comparable[] v1 = s1.toSortedArray();
        Comparable[] v2 = s2.toSortedArray();

        for(int i=0, j=0; i<v1.length; i++){
            while(j< v2.length && v1[i].compareTo(v2[j]) > 0)
                j++;
            if(j == v2.length)
                break;
            if(v1[i].compareTo(v2[j])==0){
                x.add(v1[i]);
                j++;
            }
        }
        return x;
    }
    //O(nlogn)
    public static SortedSet subtraction(SortedSet s1, SortedSet s2){
        SortedSet x = new ArraySortedSet();
        Comparable[] v1 = s1.toSortedArray();
        Comparable[] v2 = s2.toSortedArray();

        int i, j;
        for(i=0, j=0; i<v1.length; i++){
            while(j<v2.length && v1[i].compareTo(v2[j])>0)
                j++;
            if(j == v2.length)
                break;
            if(v1[i].compareTo(v2[j]) != 0)
                x.add(v1[i]);
        }
        while(i < v1.length)
            x.add(v1[i++]);
        return x;
    }
}
