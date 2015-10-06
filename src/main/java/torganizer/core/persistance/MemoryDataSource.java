package torganizer.core.persistance;

import java.util.HashMap;

import torganizer.core.entities.AbstractToEntity;

public class MemoryDataSource implements IDataSource {
	private final HashMap<Long, AbstractToEntity> cache;

	public MemoryDataSource() {
		this.cache = new HashMap<Long, AbstractToEntity>();
	}

	public void persistEntity(final AbstractToEntity entity) {
		cache.put(entity.getUid(), entity);
	}

	public AbstractToEntity findEntityById(final long playerId) {
		return cache.get(playerId);
	}

}
