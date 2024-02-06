package org.sample;

/**
 * Generic object container.
 * @author Scala28
 */
public interface Container {
    /**
     * @return True if the container is empty; false if the container contains at least 1 object.
     */
    boolean isEmpty();

    /**
     * Remove all the elements from the container.
     */
    void makeEmpty();
}
