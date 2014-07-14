package com.softserveinc.ita.kaiji.dao.hibernate;

import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.game.StatisticsDTO;
import com.softserveinc.ita.kaiji.model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Paziy Evgeniy
 * @version 1.1
 * @since 08.04.14
 */
@SuppressWarnings("JpaQlInspection")
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class UserHibernateDAOImpl implements UserDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Integer save(User user) {
        Session session = sessionFactory.getCurrentSession();
        return (Integer) session.save(user);
    }

    @Override
    public User get(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return (User) session.get(User.class, id);
    }

    @Override
    public int count() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("select count(*) from User");
        return (int) query.uniqueResult();
    }

    @Override
    public List<User> getAll() {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User");
        return query.list();
    }

    @Override
    public List<User> getAll(int from, int limit) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User");
        query.setFirstResult(from);
        query.setMaxResults(limit);
        return query.list();
    }

    @Override
    public User getByNickname(String nickname) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User where nickname = ?");
        query.setString(0, nickname);
        return (User) query.uniqueResult();
    }

    @Override
    public User getByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from User where email = ?");
        query.setString(0, email);
        return (User) query.uniqueResult();
    }

    @Override
    public void update(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.update(user);
    }

    @Override
    public void delete(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public StatisticsDTO getStatistics(User user) {
        Session session = sessionFactory.getCurrentSession();
        List resultToBean = session.createSQLQuery("SELECT count(IF(duel_result='WIN',1,null)) as win,\n" +
                " count(IF(duel_result='LOSE',1,null)) as lose,\n" +
                "  count(IF(duel_result='DRAW',1,null)) as draw,\n" +
                "  (SELECT COUNT(*) from game_winners as gw where gw.user_id=:id) as gameWins,\n" +
                " (SELECT COUNT(*) from link_game_to_user as links where links.user_id=:id) as totalGames FROM user as u, round_detail as rd where u.id=:id and rd.user_id=:id")
                .addScalar("win", LongType.INSTANCE)
                .addScalar("lose", LongType.INSTANCE)
                .addScalar("draw", LongType.INSTANCE)
                .addScalar("gameWins", LongType.INSTANCE)
                .addScalar("totalGames", LongType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(StatisticsDTO.class))
                .setParameter("id", user.getId())
                .list();

        return (StatisticsDTO) resultToBean.get(0);
    }
}
