package torganizer.core.persistance.objectservice;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.OrmFactory;

public class EntityObjectService {
	private EntityDao entityDao;

	public void createEntity(final AbstractToEntity entity) {
		final EntityOrm entityOrm = OrmFactory.getEntityOrm(entity);
		final Long id = entityDao.persist(entityOrm);
		entity.setUid(id);
	}

	public EntityDao getEntityDao() {
		return entityDao;
	}

	public void setEntityDao(final EntityDao entityDao) {
		this.entityDao = entityDao;
	}
}
