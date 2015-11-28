package torganizer.core.persistance.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.orm.EntityOrm;

@Transactional
public class EntityDaoImpl implements EntityDao {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Long persist(final EntityOrm entity) {
		final Session session = getSessionFactory().getCurrentSession();
		return (Long) session.save(entity);
	}

	@Override
	public EntityOrm getById(final long id) {
		return (EntityOrm) getSession().get(EntityOrm.class, id);
	}

	@Override
	public List<EntityOrm> getAllEntities() {
		final Criteria criteria = getSession().createCriteria(EntityOrm.class);
		final List<EntityOrm> entities = criteria.list();
		return entities;
	}

	private Session getSession() {
		return getSessionFactory().getCurrentSession();
	}
}
