package com.softserveinc.ita.kaiji.model.util.pool;

import java.util.Iterator;
import java.util.Set;

/**
 * Basic interface for synchronized pool that can store objects
 * @author Ievgen Sukhov
 * @version 1.3
 * @since 30.03.14
 *
 * P.S.  This is sort of cache, not pool, sorry
 */
public interface ConcurrentPool <V extends Poolable<K>, K> extends Iterable<V> {

    /**
     * Adds object of type {@code V} to pool if it isn`t present there
     * @param object - object that will be added
     */
    void put(V object);

    /**
     * Adds object of type {@code V} to pool if it isn`t present there
     * Allows multiple threads to access given object in pool
     * Only objects that are not contained in pool can be added
     * @param object - object that will be added
     * @param  permits - specify how many threads can access this object
     */

    void put(V object, Integer permits);

    /**
     * Adds {@link Set} of objects to pool
     * Only objects that are not contained in pool are added
     * @param objectsSet - not empty {@link Set}  of objects to add
     */

    void put(Set<V> objectsSet);


    /**
     * Locks object available for provided key and changes it`s {@link Poolable.ObjectStatus} to BORROWED
     * If object is not available pool will wait until it frees and then will provide object
     * @param key - Value of type {@code K} to search for object
     * @return - Object {@code V} if can get it or {@code null} if there is no such object in pool
     */
    V allocate(K key);

    /**
     * Must be called when object with specified key is no longer needed
     * Releases lock on object and makes it available for other threads/methods
     * Status is changed to IDLE
     *  @param key - Value of type {@code K} to search for object
     */
    void release(K key);


    /**
     * Removes specified object from pool if it is not busy
     * @param key - Value of type {@code K} to search for object
     */
    void remove(K key);

    /**
     * Checks if provided object is present in pool
     * @param object - object to compare with
     * @return {@code boolean} true if it is available
     * and false if there is no such object in pool
     *
     */
    boolean validate(V object);

    /**
     * Checks if provided object is present in pool by key
     * @param key - key of object to check
     * @return {@code boolean} true if it is available
     * and false if there is no such object in pool
     *
     */
    boolean validate(K key);


    /**
     * Used to iterate through values of given pool
     * @return {@link Iterator} for a collection of values with type {@code V}
     */
    Iterator<V> iterator();

    /**
     * To get current size of the collection
     * @return Integer size value
     */
    Integer size();
}
