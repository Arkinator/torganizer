package torganizer.core.persistance.objectservice;

import java.util.UUID;

import torganizer.core.entities.AbstractToEntity;
import torganizer.core.entities.Player;

public interface GlobalObjectService {
	Player getPlayerById(UUID submitter);

	void addEntity(AbstractToEntity entity);

	Player getPlayerByName(String name);
}