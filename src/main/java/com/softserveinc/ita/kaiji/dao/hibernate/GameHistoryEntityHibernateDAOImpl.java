package com.softserveinc.ita.kaiji.dao.hibernate;

import com.softserveinc.ita.kaiji.dao.GameHistoryEntityDAO;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Paziy Evgeniy
 * @version 1.1
 * @since 13.04.14
 */
@SuppressWarnings("JpaQlInspection")
@Transactional(propagation = Propagation.REQUIRED)
@Repository
public class GameHistoryEntityHibernateDAOImpl implements GameHistoryEntityDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Integer save(GameHistoryEntity gameHistoryEntity) {
        Session session = sessionFactory.getCurrentSession();
        return (Integer) session.save(gameHistoryEntity);
    }

    @Override
    public GameHistoryEntity get(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return (GameHistoryEntity) session.get(GameHistoryEntity.class, id);
    }

    @Override
    public int count() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select count(*) from GameHistoryEntity");
        return (int) query.uniqueResult();
    }

    @Override
    public List<GameHistoryEntity> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from GameHistoryEntity");
        return query.list();
    }

    @Override
    public List<GameHistoryEntity> getAll(int from, int limit) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from GameHistoryEntity");
        query.setFirstResult(from);
        query.setMaxResults(limit);
        return query.list();
    }

    @Override
    public void update(GameHistoryEntity gameHistoryEntity) {
        Session session = sessionFactory.getCurrentSession();
        session.update(gameHistoryEntity);
    }

    @Override
    public void delete(GameHistoryEntity gameHistoryEntity) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(gameHistoryEntity);
    }

    @Override
    public List<GameHistoryEntity> getGameHistoryByWinner(Integer userId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from GameHistoryEntity as g where :winner in elements(g.winners)");
        query.setInteger("winner",userId);
        return query.list();
    }
}
