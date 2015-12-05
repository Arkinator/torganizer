package torganizer.core.persistance.dao;

import java.util.List;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.orm.EntityOrm;

@Component(value = "entityDao")
public class EntityDaoImpl extends AbstractDao<EntityOrm> implements EntityDao {
	@Autowired(required = true)
	public EntityDaoImpl(final SessionFactory sessionFactory) {
		super(sessionFactory);
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
	public EntityOrm getByName(final String name) {
		final EntityOrm example = new EntityOrm();
		example.setName(name);
		return findByExample(example);
	}

	@Override
	protected Class getEntityClass() {
		return EntityOrm.class;
	}
}
