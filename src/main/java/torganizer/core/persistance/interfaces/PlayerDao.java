package torganizer.core.persistance.interfaces;

import java.util.List;

import torganizer.core.persistance.orm.PlayerOrm;

public interface PlayerDao {
	PlayerOrm persist(PlayerOrm entity);

	List<PlayerOrm> getAllPlayers();
}
