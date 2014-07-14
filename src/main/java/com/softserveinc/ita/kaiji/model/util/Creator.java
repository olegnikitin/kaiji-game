package com.softserveinc.ita.kaiji.model.util;

/**
 * Creator creates some instance <code>T</code> by information <code>I</code>
 * @param <I> type of creation info
 * @param <T> type of created object
 * @see com.softserveinc.ita.kaiji.model.game.creator.GameCreator
 * @see com.softserveinc.ita.kaiji.model.game.GameFactory
 *
 * @author Paziy Evgeniy
 * @version 2.0
 * @since 28.03.14
 */
public interface Creator<I, T> {

    /**
     * Returns true if creatorInfo valid for creating
     * @param creatorInfo instructions for creator
     * @return true if creatorInfo valid for creating
     */
    boolean isValid(I creatorInfo);

    /**
     * Returns instance of creating object <code>T</code> from object <code>I</code>
     * @param creatorInfo instructions for creator
     * @return instance of creating object
     */
    T create(I creatorInfo);
}