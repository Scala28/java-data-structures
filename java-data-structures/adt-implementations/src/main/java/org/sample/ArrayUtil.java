package org.sample;

import java.lang.reflect.Array;

public class ArrayUtil {
    public static Object[] resize(Object[] a, int newDim){
        if(newDim < a.length)
            throw new IllegalArgumentException();
        Object[] newArray = new Object[newDim];
        System.arraycopy(a, 0, newArray, 0, a.length);
        return newArray;
    }
    @SuppressWarnings("unchecked")
    public static <T>T[] convertArray(Object[] a, Class<T> targetType)throws IllegalArgumentException {
        T[] ret = (T[])Array.newInstance(targetType, a.length);
        for(int i=0; i<ret.length; i++)
            ret[i] = (T)a[i];
        return ret;
    }
    public static void swap(Object[] a, int i, int j){
        Object temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
