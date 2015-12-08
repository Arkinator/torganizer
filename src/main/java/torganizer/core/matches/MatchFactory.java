package torganizer.core.matches;

import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.types.MatchType;
import torganizer.exceptions.NotYetImplementedException;

public class MatchFactory {

	public static GenericMatch constructMatchFromOrm(final EntityOrm setOrm) {
		if (setOrm == null) {
			throw new IncohesiveDatabaseDetectedException();
		}
		if (setOrm.getMatch() == null) {
			throw new IncohesiveDatabaseDetectedException();
		}
		if (setOrm.getMatch().getType() == null) {
			throw new IncohesiveDatabaseDetectedException();
		}
		if (setOrm.getMatch().getType() == MatchType.GAME) {
			return new Game(setOrm.getMatch());
		}
		if (setOrm.getMatch().getType() == MatchType.SINGLE_PLAYER_MATCH_SERIES) {
			return new BestOfMatchSinglePlayer(setOrm.getMatch());
		}
		throw new NotYetImplementedException();
	}

	public static class IncohesiveDatabaseDetectedException extends RuntimeException {
		private static final long serialVersionUID = 815874504371105093L;
	}
}
