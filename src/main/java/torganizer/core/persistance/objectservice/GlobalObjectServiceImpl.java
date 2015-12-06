package torganizer.core.persistance.objectservice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;
import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.OrmFactory;
import torganizer.exceptions.NotYetImplementedException;

@Transactional
public class GlobalObjectServiceImpl implements GlobalObjectService {
	private final EntityDao entityDao;
	private final OrmFactory ormFactory;

	@Autowired(required = true)
	public GlobalObjectServiceImpl(final EntityDao entityDao, final OrmFactory ormFactory) {
		this.entityDao = entityDao;
		this.ormFactory = ormFactory;
	}

	public void createEntity(final AbstractToEntity entity) {
		final EntityOrm entityOrm = ormFactory.getEntityOrm(entity);
		entityDao.persist(entityOrm);
	}

	@Override
	public Player getPlayerById(final UUID id) {
		return new Player(entityDao.getById(id).getPlayer());
	}

	@Override
	public void addEntity(final AbstractToEntity entity) {
		throw new NotYetImplementedException();
	}

	@Override
	public Player getPlayerByName(final String name) {
		return new Player(entityDao.getById(id).getPlayer());
	}
}
