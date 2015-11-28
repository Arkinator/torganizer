package torganizer.core.persistance.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.persistance.interfaces.PlayerDao;
import torganizer.core.persistance.orm.PlayerOrm;

@Repository
@Transactional
public class PlayerDaoImpl implements PlayerDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void persist(final PlayerOrm entity) {
		final Session session = getSessionFactory().getCurrentSession();
		// session.beginTransaction();
		session.save(entity);
		// session.getTransaction().commit();
	}

	@Override
	public PlayerOrm getById(final long id) {
		return null;
	}

	@Override
	public PlayerOrm getByName(final String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PlayerOrm> getAllPlayers() {
		final Session session = getSessionFactory().getCurrentSession();
		// session.beginTransaction();
		final Criteria criteria = session.createCriteria(PlayerOrm.class);
		final List<PlayerOrm> persons = criteria.list();
		// session.getTransaction().commit();
		return persons;
	}
}
