package torganizer.core.persistance.objectservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.OrmFactory;

@Transactional
@Component(value = "entityObjectService")
public class EntityObjectService {
	private final EntityDao entityDao;
	private final OrmFactory ormFactory;

	@Autowired(required = true)
	public EntityObjectService(final EntityDao entityDao, final OrmFactory ormFactory) {
		this.entityDao = entityDao;
		this.ormFactory = ormFactory;
	}

	public void createEntity(final AbstractToEntity entity) {
		final EntityOrm entityOrm = ormFactory.getEntityOrm(entity);
		entityDao.persist(entityOrm);
	}
}
