package com.softserveinc.ita.kaiji.model.util;

/**
 * Some object which have Id
 * @author Paziy Evgeniy
 * @version 1.1
 * @since 17.03.14
 */
public interface Identifiable {
    /**
     * Sets Id to object. Null can be set.
     * It means there is no Id for object.
     * @param id for object
     */
    void setId(Integer id);

    /**
     * Returns object id. Can be null if not set or set null.
     * @return object id
     */
    Integer getId();
}
