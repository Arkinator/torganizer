package torganizer.core.persistance.interfaces;

import java.util.List;
import java.util.UUID;

import torganizer.core.persistance.orm.PlayerOrm;

public interface PlayerDao {

	Long persist(PlayerOrm entity);

	PlayerOrm getById(UUID id);

	PlayerOrm getByName(String name);

	List<PlayerOrm> getAllPlayers();

}
