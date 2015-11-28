package torganizer.core.persistance.interfaces;

import java.util.List;

import torganizer.core.persistance.orm.PlayerOrm;

public interface PlayerDao {

	Long persist(PlayerOrm entity);

	PlayerOrm getById(long id);

	PlayerOrm getByName(String name);

	List<PlayerOrm> getAllPlayers();

}
