package torganizer.core.persistance.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.orm.EntityOrm;

@Transactional
@Component(value = "entityDao")
public class EntityDaoImpl implements EntityDao {
	private final SessionFactory sessionFactory;

	@Autowired(required = true)
	public EntityDaoImpl(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Long persist(final EntityOrm entity) {
		final Session session = sessionFactory.getCurrentSession();
		return (Long) session.save(entity);
	}

	@Override
	public EntityOrm getById(final UUID uuid) {
		final EntityOrm example = new EntityOrm();
		example.setUuid(uuid);
		return findByExample(example);
	}

	@Override
	public EntityOrm findByExample(final EntityOrm example) {
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
		return sessionFactory.getCurrentSession();
	}

	@Override
	public EntityOrm getByName(final String name) {
		final EntityOrm example = new EntityOrm();
		example.setName(name);
		return findByExample(example);
	}

	@Override
	public void update(final EntityOrm entityOrm) {
		getSession().update(entityOrm);
	}
}
