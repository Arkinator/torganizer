package torganizer.core.persistance.interfaces;

import java.util.List;

import torganizer.core.persistance.orm.SubmittedResultOrm;

public interface SubmittedResultDao {
	SubmittedResultOrm persist(SubmittedResultOrm entity);

	List<SubmittedResultOrm> getAllEntities();

	SubmittedResultOrm getByResultId(long id);
}
