package torganizer.core.persistance.interfaces;

import java.util.List;
import java.util.UUID;

import torganizer.core.persistance.orm.MatchOrm;

public interface MatchDao {
	Long persist(MatchOrm entity);

	MatchOrm getById(UUID id);

	MatchOrm getByName(String name);

	List<MatchOrm> getAllMatches();
}
