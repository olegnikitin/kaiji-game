package com.softserveinc.ita.kaiji.dao;

import com.softserveinc.ita.kaiji.model.util.Identifiable;

import java.util.List;

/**
 * Provide base CRUD methods for working with repository.
 * Some DAO interface must extend this <code>GenericDAO</code> to provide base methods.
 * @see com.softserveinc.ita.kaiji.model.util.Identifiable
 * @param <T> type of objects
 * @author Paziy Evgeniy
 * @version 1.2
 * @since 09.04.14
 */
public interface GenericDAO<T extends Identifiable> {

    /**
     * Save object to repository
     * @param object obj for saving
     * @return id of saved object
     */
    Integer save(T object);

    /**
     * Returns object by chosen id
     * @param id object id
     * @return object by chosen id
     */
    T get(Integer id);

    /**
     * Returns count of saved instances
     * @return count of saved instances
     */
    int count();

    /**
     * Returns all objects
     * @return all objects
     */
    List<T> getAll();

    /**
     * Returns all objects
     * @param from start position
     * @param limit max result count
     * @return all objects
     */
    List<T> getAll(int from, int limit);

    /**
     * Updates object in repository
     * @param object for updating
     */
    void update(T object);

    /**
     * Remove object from repository
     * @param object for removing
     */
    void delete(T object);
}
