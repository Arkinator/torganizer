package torganizer.core.persistance.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractDao<T> {
	private final SessionFactory sessionFactory;

	public AbstractDao(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public T persist(final T entity) {
		final Serializable id = getSession().save(entity);
		return (T) getSession().get(getEntityClass(), id);
	}

	public List<T> getAllEntities() {
		final Criteria criteria = getSession().createCriteria(getEntityClass());
		final List<T> entities = criteria.list();
		return entities;
	}

	protected abstract Class getEntityClass();

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void update(final T entity) {
		getSession().update(entity);
	}
}
