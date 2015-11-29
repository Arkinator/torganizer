package torganizer.core.persistance.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
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
	public EntityOrm getById(final UUID uuid) {
		final EntityOrm example = new EntityOrm();
		example.setUuid(uuid);
		final List<EntityOrm> results = getSession().createCriteria(EntityOrm.class).add(Example.create(example)).list();
		if (results.size() < 1) {
			return null;
		} else {
			return results.get(0);
		}
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
