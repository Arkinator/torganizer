package torganizer.core.persistance.objectservice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;
import torganizer.core.matches.Game;
import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.OrmFactory;
import torganizer.exceptions.NotYetImplementedException;

@Transactional
@Component(value = "globalObjectServiceImpl")
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
		final EntityOrm entity = entityDao.getByName(name);
		if (entity == null) {
			return null;
		}
		return new Player(entity.getPlayer());
	}

	@Override
	public void addPlayer(final Player player) {
		entityDao.persist(ormFactory.getPlayerOrm(player).getEntity());
	}

	@Override
	public void addGame(final Game game) {
		entityDao.persist(ormFactory.getGameOrm(game).getEntity());
	}

	@Override
	public Game getMatchById(final UUID gameId) {
		final EntityOrm entity = entityDao.getById(gameId);
		if (entity == null) {
			return null;
		}
		return new Game(entity.getMatch());
	}
}
