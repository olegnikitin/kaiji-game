package com.softserveinc.ita.kaiji.model.util;

import org.apache.log4j.Logger;

import java.util.Set;

/**
 * Universal factory which can create anything by using <code>Creator</code>
 * @see com.softserveinc.ita.kaiji.model.util.Creator
 * @param <I> information for factory to chose what object must be created
 * @param <T> type of cheating objects
 * @param <C> type of creators with will used for creating objects
 *
 * @author Paziy Evgeniy
 * @version 1.2
 * @since 30.03.14
 */
public class Factory<I, T, C extends Creator<I, T>> {

    private static final Logger LOG = Logger.getLogger(Factory.class);

    private Set<C> creators;

    public Factory(Set<C> creators) {
        this.creators = creators;
    }

    public T create(I info) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating object...");
        }

        if (info == null) {
            NullPointerException ex = new NullPointerException("Info can't be null");
            LOG.error("Info is null", ex);
            throw ex;
        }

        for (C creator : creators) {
            if (creator.isValid(info)) {
                T object = creator.create(info);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Created : " + object.getClass() + "; creator: " + creator.getClass());
                }
                return object;
            }
        }

        IllegalArgumentException ex = new IllegalArgumentException("No object can be created for this info");
        LOG.error("Can't create object", ex);
        throw ex;
    }
}
