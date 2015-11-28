package torganizer.core.persistance.objectservice;

import java.util.ArrayList;
import java.util.List;

import torganizer.core.entities.Player;
import torganizer.core.persistance.interfaces.PlayerDao;
import torganizer.core.persistance.orm.OrmFactory;
import torganizer.core.persistance.orm.PlayerOrm;

public class PlayerObjectService {
	private PlayerDao playerDao;

	public void addPlayer(final Player player) {
		final PlayerOrm entity = OrmFactory.getPlayerOrm(player);
		playerDao.persist(entity);
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
}
