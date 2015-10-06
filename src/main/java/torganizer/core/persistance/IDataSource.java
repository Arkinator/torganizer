package torganizer.core.persistance;

import torganizer.core.entities.AbstractToEntity;

public interface IDataSource {

	void persistEntity(AbstractToEntity entity);

	AbstractToEntity findEntityById(long playerId);

}
