package torganizer.core.persistance.objectservice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import torganizer.core.entities.Player;
import torganizer.core.persistance.interfaces.EntityDao;
import torganizer.core.persistance.interfaces.PlayerDao;
import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.OrmFactory;
import torganizer.core.persistance.orm.PlayerOrm;
import torganizer.exceptions.IncompatibleTypeException;

@Component(value = "playerObjectService")
public class PlayerObjectService {
	private PlayerDao playerDao;
	private final EntityDao entityDao;
	private EntityObjectService entityObjectService;
	private final OrmFactory ormFactory;

	@Autowired(required = true)
	public PlayerObjectService(final PlayerDao playerDao, final EntityObjectService entityObjectService, final OrmFactory ormFactory, final EntityDao entityDao) {
		this.playerDao = playerDao;
		this.entityObjectService = entityObjectService;
		this.ormFactory = ormFactory;
		this.entityDao = entityDao;
	}

	public void addPlayer(final Player player) {
		entityObjectService.createEntity(player);
		final EntityOrm entityOrm = entityDao.getById(player.getUid());
		final PlayerOrm playerOrm = ormFactory.getPlayerOrm(player, entityOrm);
		playerDao.persist(playerOrm);
	}

	public List<Player> getAllPlayers() {
		final List<Player> result = new ArrayList<>();
		playerDao.getAllPlayers().forEach(orm -> result.add(ormFactory.getPlayerBo(orm.getEntity())));
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

	public Player getPlayerByName(final String playerName) {
		final EntityOrm entityOrm = entityDao.getByName(playerName);
		if (entityOrm.getPlayer() == null) {
			throw new IncompatibleTypeException("Entity is not of type player and can not be accessed as such");
		}
		return ormFactory.getPlayerBo(entityOrm);
	}

	public Player getPlayerById(final UUID playerId) {
		final EntityOrm entityOrm = entityDao.getById(playerId);
		if (entityOrm.getPlayer() == null) {
			throw new IncompatibleTypeException("Entity is not of type player and can not be accessed as such");
		}
		return ormFactory.getPlayerBo(entityOrm);
	}
}
