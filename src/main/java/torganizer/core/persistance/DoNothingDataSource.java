package torganizer.core.persistance;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;

public class DoNothingDataSource implements IDataSource {
	@Override
	public void persistEntity(final AbstractToEntity entity) {
	}

	@Override
	public AbstractToEntity findEntityById(final long playerId) {
		return null;
	}

	@Override
	public Player findUserByName(final String username) {
		return null;
	}
}
