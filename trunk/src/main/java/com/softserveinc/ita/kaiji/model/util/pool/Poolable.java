package com.softserveinc.ita.kaiji.model.util.pool;

/**
 * Needed to be implemented by all objects that will be used in {@code ConcurrentPool}
 * @author Ievgen Sukhov
 * @version 1.1
 * @since 30.03.14
 *
 * @see ConcurrentPool
 */
public interface Poolable<K> {


    /**
     * Object states in pool
     */
    enum ObjectStatus {
        /**
         * For future optimizations
         */
        NOT_IN_POOL,
        /**
         * Object available and not used anywhere
         */
        IDLE,
        /**
         * Object is already in use
         */
        BORROWED;
    }

    /**
     * Must be implemented so pool knows what kind of key to use
     * Could be any unique class field or generated value
     * Can be parametrised with any type {@code K} , but immutable types recomended
     * @return key value to be stored in pool
     */
    K getPoolKey();


}
