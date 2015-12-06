package torganizer.core.matches;

import java.util.UUID;

import torganizer.utils.GlobalUtilities;

public class CtlMatch extends AbstractMatchSeries<Game> {
	public CtlMatch(final int numberOfSets, final UUID sideA, final UUID sideB, final String name) {
		super(numberOfSets, sideA, sideB, name);
	}

	public void setPlayer(final int team, final int setNumber, final UUID player) {
		final Game set = getSet(setNumber);
		if (team == 0) {
			set.setSideA(player);
		} else if (team == 1) {
			set.setSideB(player);
		} else {
			throw new IllegalTeamNumberSpecifiedException("Team number '" + team + "' could not be used. Only 0 and 1 are allowed");
		}
	}

	public class IllegalTeamNumberSpecifiedException extends RuntimeException {
		private static final long serialVersionUID = -5876494929525138426L;

		public IllegalTeamNumberSpecifiedException(final String string) {
			super(string);
		}
	}

	@Override
	public Game constructNewSet() {
		return new Game(GlobalUtilities.createNewSetName(this));
	}
}
