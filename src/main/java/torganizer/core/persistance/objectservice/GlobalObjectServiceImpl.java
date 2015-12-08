package torganizer.core.persistance.objectservice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.matches.Game;
import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.exceptions.NotYetImplementedException;

@Transactional
@Component(value = "globalObjectServiceImpl")
public class GlobalObjectServiceImpl implements GlobalObjectService {
	private final EntityDao entityDao;

	@Autowired(required = true)
	public GlobalObjectServiceImpl(final EntityDao entityDao) {
		this.entityDao = entityDao;
	}

	public void createEntity(final AbstractToEntity entity) {
		entityDao.persist(entity.getEntityOrm());
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
		entityDao.persist(player.getEntityOrm());
	}

	@Override
	public void addGame(final Game game) {
		entityDao.persist(game.getEntityOrm());
	}

	@Override
	public Game getGameById(final UUID gameId) {
		final EntityOrm entity = entityDao.getById(gameId);
		if (entity == null) {
			return null;
		}
		return new Game(entity.getMatch());
	}

	@Override
	public BestOfMatchSinglePlayer getBestOfMatchById(final UUID gameId) {
		final EntityOrm entity = entityDao.getById(gameId);
		if (entity == null) {
			return null;
		}
		return new BestOfMatchSinglePlayer(entity.getMatch());
	}

	@Override
	public void addMatch(final BestOfMatchSinglePlayer match) {
		entityDao.persist(match.getEntityOrm());
	}

	@Override
	public void updateEntity(final AbstractToEntity entity) {
		if (entity.getEntityOrm() == null) {
			throw new CanNotUpdatePojoException(
					"The object you are trying to save is a pojo and has no attached DB-Object! Persist it first, or load it from the DB if it already is");
		}
		entityDao.update(entity.getEntityOrm());
	}

	public class CanNotUpdatePojoException extends RuntimeException {
		private static final long serialVersionUID = 6645786466265975027L;

		public CanNotUpdatePojoException(final String string) {
			super(string);
		}
	}
}
