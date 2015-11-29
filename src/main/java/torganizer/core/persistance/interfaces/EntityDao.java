package torganizer.core.persistance.interfaces;

import java.util.List;
import java.util.UUID;

import torganizer.core.persistance.orm.EntityOrm;

public interface EntityDao {
	Long persist(EntityOrm entity);

	EntityOrm getById(UUID id);

	List<EntityOrm> getAllEntities();
}
