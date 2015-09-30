package torganizer.core;

public class CtlMatch extends AbstractMatchSeries<Team, Game> {
	public CtlMatch(final int numberOfSets, final Team sideA, final Team sideB) {
		super(numberOfSets, sideA, sideB);
	}

	public void setPlayer(final int team, final int setNumber, final Player player) {
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
		return new Game();
	}
}
