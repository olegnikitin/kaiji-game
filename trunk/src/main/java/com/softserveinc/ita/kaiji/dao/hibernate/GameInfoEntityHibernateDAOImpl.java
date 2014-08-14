package com.softserveinc.ita.kaiji.dao.hibernate;

import com.softserveinc.ita.kaiji.dao.GameInfoEntityDAO;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paziy Evgeniy
 * @version 1.2
 * @since 09.04.14
 */
@SuppressWarnings("JpaQlInspection")
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class GameInfoEntityHibernateDAOImpl implements GameInfoEntityDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Integer save(GameInfoEntity gameInfoEntity) {
        Session session = sessionFactory.getCurrentSession();
        return (Integer) session.save(gameInfoEntity);
    }

    @Override
    public GameInfoEntity get(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return (GameInfoEntity) session.get(GameInfoEntity.class, id);
    }

    @Override
    public int count() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select count(*) from GameInfoEntity");
        return (int) query.uniqueResult();
    }

    @Override
    public List<GameInfoEntity> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from GameInfoEntity");
        return query.list();
    }

    @Override
    public List<GameInfoEntity> getAll(int from, int limit) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from GameInfoEntity");
        query.setFirstResult(from);
        query.setMaxResults(limit);
        return query.list();
    }

    @Override
    public void update(GameInfoEntity gameInfoEntity) {
        Session session = sessionFactory.getCurrentSession();
        session.update(gameInfoEntity);
    }

    @Override
    public void delete(GameInfoEntity gameInfoEntity) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(gameInfoEntity);
    }

    @Override
    public List<GameInfoEntity> getGameInfoFor(Integer userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from GameInfoEntity as g where :user in elements(g.users)");
        query.setInteger("user", userId);
        return (List<GameInfoEntity>) query.list();
    }
}
