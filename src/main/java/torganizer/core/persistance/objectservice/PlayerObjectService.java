package torganizer.core.persistance.objectservice;

import java.util.ArrayList;
import java.util.List;

import torganizer.core.entities.Player;
import torganizer.core.persistance.interfaces.PlayerDao;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.OrmFactory;
import torganizer.core.persistance.orm.PlayerOrm;

public class PlayerObjectService {
	private PlayerDao playerDao;
	private EntityObjectService entityObjectService;

	public void addPlayer(final Player player) {
		entityObjectService.createEntity(player);
		final EntityOrm entityOrm = entityObjectService.getEntityDao().getById(player.getUid());
		final PlayerOrm playerOrm = OrmFactory.getPlayerOrm(player, entityOrm);
		playerDao.persist(playerOrm);
	}

	public List<Player> getAllPlayers() {
		final List<Player> result = new ArrayList<>();
		playerDao.getAllPlayers().forEach(orm -> result.add(OrmFactory.getPlayerBo(orm)));
		return result;
	}

	public PlayerDao getPlayerDao() {
		return playerDao;
	}

	public void setPlayerDao(final PlayerDao playerDao) {
		this.playerDao = playerDao;
	}

	public EntityObjectService getEntityObjectService() {
		return entityObjectService;
	}

	public void setEntityObjectService(final EntityObjectService entityObjectService) {
		this.entityObjectService = entityObjectService;
	}
}
