package torganizer.core.persistance.interfaces;

import java.util.List;
import java.util.UUID;

import torganizer.core.persistance.orm.EntityOrm;

public interface EntityDao {
	EntityOrm persist(EntityOrm entity);

	EntityOrm getById(UUID id);

	List<EntityOrm> getAllEntities();

	EntityOrm getByName(String name);

	EntityOrm findByExample(EntityOrm example);

	void update(EntityOrm entityOrm);
}
