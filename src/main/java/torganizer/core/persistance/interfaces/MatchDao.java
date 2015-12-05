package torganizer.core.persistance.interfaces;

import java.util.List;

import torganizer.core.persistance.orm.MatchOrm;

public interface MatchDao {
	MatchOrm persist(MatchOrm entity);

	List<MatchOrm> getAllMatches();

	void update(MatchOrm matchOrm);
}
