package torganizer.core.persistance;

import torganizer.core.entities.AbstractToEntity;

public class DoNothingDataSource implements IDataSource {
	public void persistEntity(final AbstractToEntity entity) {
	}

	public AbstractToEntity findEntityById(final long playerId) {
		return null;
	}

}
