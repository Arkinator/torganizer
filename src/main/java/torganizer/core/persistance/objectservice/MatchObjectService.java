package torganizer.core.persistance.objectservice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import torganizer.core.matches.Game;
import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.interfaces.MatchDao;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.MatchOrm;
import torganizer.core.persistance.orm.OrmFactory;

@Component(value = "matchObjectService")
public class MatchObjectService {
	private PlayerObjectService playerObjectService;
	private EntityObjectService entityObjectService;
	private final EntityDao entityDao;
	private final MatchDao matchDao;
	private final OrmFactory ormFactory;

	@Autowired
	public MatchObjectService(final PlayerObjectService playerObjectService, final EntityObjectService entityObjectService, final EntityDao entityDao, final MatchDao matchDao,
			final OrmFactory ormFactory) {
		this.playerObjectService = playerObjectService;
		this.entityObjectService = entityObjectService;
		this.entityDao = entityDao;
		this.ormFactory = ormFactory;
		this.matchDao = matchDao;
	}

	public void addGame(final Game game) {
		entityObjectService.createEntity(game);
		final EntityOrm entityOrm = entityDao.getById(game.getUid());
		final MatchOrm matchOrm = ormFactory.getMatchOrm(game, entityOrm);
		matchDao.persist(matchOrm);
	}

	public PlayerObjectService getPlayerObjectService() {
		return playerObjectService;
	}

	public void setPlayerObjectService(final PlayerObjectService playerObjectService) {
		this.playerObjectService = playerObjectService;
	}

	public EntityObjectService getEntityObjectService() {
		return entityObjectService;
	}

	public void setEntityObjectService(final EntityObjectService entityObjectService) {
		this.entityObjectService = entityObjectService;
	}

	public Game getGameById(final UUID gameId) {
		return ormFactory.getGameBo(entityDao.getById(gameId));
	}
}
