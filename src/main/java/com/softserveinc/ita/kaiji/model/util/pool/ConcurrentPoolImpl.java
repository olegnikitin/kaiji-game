package com.softserveinc.ita.kaiji.model.util.pool;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;

import com.softserveinc.ita.kaiji.model.util.pool.Poolable.*;
import org.apache.log4j.Logger;

/**
 * @author Ievgen Sukhov
 * @version 2.0
 * @since 30.03.14
 * <br/>
 * Usage example :
 * We need some class {@code Class} to impement {@link Poolable} interface and override
 * {@link Poolable#getPoolKey()} , for example for {@link Integer} type of key
 * So after that we can create pool of {@code Class} objects like this:
 * {@code ConcurrentPoolImpl<Class, Integer> pool = new ConcurrentPoolImpl<>()}
 */
public class ConcurrentPoolImpl<V extends Poolable<K>, K> implements ConcurrentPool<V, K> {

    private class State {
        ObjectStatus status;
        Semaphore lock;

        public State(Integer permits) {
            status = ObjectStatus.IDLE;
            lock = new Semaphore(permits);
        }

        public void setStatus(ObjectStatus s) {
            status = s;
        }

        public boolean isIdle() {
            return (status == ObjectStatus.IDLE) ? true : false;
        }
    }


    private final ConcurrentMap<K, V> pool;
    private final Map<K, State> status;

    private static final Logger LOG = Logger.getLogger(ConcurrentPoolImpl.class);

    public ConcurrentPoolImpl() {
        pool = new ConcurrentHashMap<>();
        status = new Hashtable<>();
    }


    @Override
    public void put(V object) {
        if (!validate(object.getPoolKey())) {
            pool.put(object.getPoolKey(), object);
            status.put(object.getPoolKey(), new State(1));
           if (LOG.isDebugEnabled()) {
               LOG.debug(object + " with id "
                       + object.getPoolKey() + " was placed in pool");
           }
        }
    }

    @Override
    public void put(V obj, Integer permits) {
        put(obj);
        status.put(obj.getPoolKey(), new State(permits));
    }

    @Override
    public void put(Set<V> objectsSet) {
        for (V o : objectsSet) {
            put(o);

        }
    }

    @Override
    public V allocate(K key) {
        if (validate(key)) {
            status.get(key).lock.acquireUninterruptibly();
            if (status.get(key).isIdle()) {
                V res = pool.get(key);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Fetching object " + res
                            + " with id " + key +" from pool");
                }
                if (status.get(key).lock.availablePermits() == 0) {
                    status.get(key).setStatus(ObjectStatus.BORROWED);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("No free slots left for "+res);
                    }
                }
                return res;
            } else {
                return allocate(key);
            }
        }
        return null;
    }

    @Override
    public void release(K key) {
        if (validate(key)) {
            status.get(key).setStatus(ObjectStatus.IDLE);
            status.get(key).lock.release();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Releasing object with id " + key);
            }
        }
    }

    @Override
    public void remove(K key) {
        if (validate(key) && status.get(key).isIdle()) {
                status.get(key).lock.acquireUninterruptibly();
                status.get(key).setStatus(ObjectStatus.BORROWED);
                pool.remove(key);
                status.get(key).lock.release();
                status.remove(key);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Removed object with id " + key);
                }
        }
    }

    @Override
    public boolean validate(V object) {
        return pool.containsValue(object);
    }

    @Override
    public boolean validate(K key) {
        return pool.containsKey(key);
    }

    @Override
    public Iterator<V> iterator() {
        return pool.values().iterator();
    }

    public Integer size() {
        return pool.size();
    }

    @Override
    public String toString() {
        return pool.toString();
    }

}
