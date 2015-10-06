package torganizer.core.persistance;

import java.util.HashMap;
import java.util.Map.Entry;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;

public class MemoryDataSource implements IDataSource {
	private final HashMap<Long, AbstractToEntity> cache;

	public MemoryDataSource() {
		this.cache = new HashMap<Long, AbstractToEntity>();
	}

	@Override
	public void persistEntity(final AbstractToEntity entity) {
		cache.put(entity.getUid(), entity);
	}

	@Override
	public AbstractToEntity findEntityById(final long playerId) {
		return cache.get(playerId);
	}

	@Override
	public Player findUserByName(final String username) {
		for (final Entry<Long, AbstractToEntity> entity : cache.entrySet()) {
			if (entity.getValue() instanceof Player) {
				if (((Player) (entity.getValue())).getName().equals(username)) {
					return (Player) entity.getValue();
				}
			}
		}
		return null;
	}

}
