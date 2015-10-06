package torganizer.core.persistance;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;

public interface IDataSource {

	void persistEntity(AbstractToEntity entity);

	AbstractToEntity findEntityById(long playerId);

	Player findUserByName(String username);

}
