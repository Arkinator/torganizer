package torganizer.core.persistance.interfaces;

import java.util.List;

import torganizer.core.persistance.orm.EntityOrm;

public interface EntityDao {
	Long persist(EntityOrm entity);

	EntityOrm getById(long id);

	List<EntityOrm> getAllEntities();
}
