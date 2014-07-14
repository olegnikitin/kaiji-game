package com.softserveinc.ita.kaiji.dao.hibernate;

import com.softserveinc.ita.kaiji.dao.RoundResultEntityDAO;
import com.softserveinc.ita.kaiji.dto.game.RoundResultEntity;
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
public class RoundResultEntityHibernateDAOImpl implements RoundResultEntityDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Integer save(RoundResultEntity roundResultEntity) {
        Session session = sessionFactory.getCurrentSession();
        return (Integer) session.save(roundResultEntity);
    }

    @Override
    public RoundResultEntity get(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return (RoundResultEntity) session.get(RoundResultEntity.class, id);
    }

    @Override
    public int count() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select count(*) from RoundResultEntity");
        return (int) query.uniqueResult();
    }

    @Override
    public List<RoundResultEntity> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from RoundResultEntity");
        return query.list();
    }

    @Override
    public List<RoundResultEntity> getAll(int from, int limit) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from RoundResultEntity");
        query.setFirstResult(from);
        query.setMaxResults(limit);
        return query.list();
    }

    @Override
    public void update(RoundResultEntity roundResultEntity) {
        Session session = sessionFactory.getCurrentSession();
        session.update(roundResultEntity);
    }

    @Override
    public void delete(RoundResultEntity roundResultEntity) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(roundResultEntity);
    }
}
