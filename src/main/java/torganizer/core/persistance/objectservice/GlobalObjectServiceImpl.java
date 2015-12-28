package torganizer.core.persistance.objectservice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.matches.Game;
import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.tournaments.TrisTournament;
import torganizer.core.types.TournamentType;

@Transactional
@Component(value = "globalObjectServiceImpl")
public class GlobalObjectServiceImpl implements GlobalObjectService {
	private final EntityDao entityDao;

	@Autowired(required = true)
	public GlobalObjectServiceImpl(final EntityDao entityDao) {
		this.entityDao = entityDao;
	}

	@Override
	public Player getPlayerById(final UUID id) {
		return new Player(entityDao.getById(id).getPlayer());
	}

	@Override
	public void addEntity(final AbstractToEntity entity) {
		entityDao.persist(entity.getEntityOrm());
	}

	@Override
	public Player getPlayerByName(final String name) {
		final EntityOrm entity = entityDao.getByName(name);
		if (entity == null) {
			return null;
		}
		if (entity.getPlayer() == null) {
			throw new IncompatibleTypeException("The referenced Object is not a Player");
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
		if (entity.getMatch() == null) {
			throw new IncompatibleTypeException("The referenced Object is not a Match");
		}
		return new Game(entity.getMatch());
	}

	@Override
	public BestOfMatchSinglePlayer getBestOfMatchById(final UUID gameId) {
		final EntityOrm entity = entityDao.getById(gameId);
		if (entity == null) {
			return null;
		}
		if (entity.getMatch() == null) {
			throw new IncompatibleTypeException("The referenced Object is not a Match");
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

	@Override
	public void addTeam(final Team team) {
		entityDao.persist(team.getEntityOrm());
	}

	@Override
	public Team getTeamById(final UUID teamId) {
		final EntityOrm entity = entityDao.getById(teamId);
		if (entity == null) {
			return null;
		}
		return new Team(entity);
	}

	@Override
	public TrisTournament getTournamentById(final UUID uid) {
		final EntityOrm entity = entityDao.getById(uid);
		if (entity == null) {
			return null;
		}
		if (entity.getTournament() == null) {
			throw new IncompatibleTypeException("The referenced Object is not a Tournament");
		}
		if (entity.getTournament().getType() == TournamentType.TrisTournament) {
			return new TrisTournament(entity, this);
		}
		throw new IncompatibleTypeException("The type " + entity.getTournament().getType() + " could not be converted");
	}

	public static class CanNotUpdatePojoException extends RuntimeException {
		private static final long serialVersionUID = 6645786466265975027L;

		public CanNotUpdatePojoException(final String string) {
			super(string);
		}
	}

	public static class IncompatibleTypeException extends RuntimeException {
		private static final long serialVersionUID = 9193238995765925362L;

		public IncompatibleTypeException(final String string) {
			super(string);
		}
	}
}
