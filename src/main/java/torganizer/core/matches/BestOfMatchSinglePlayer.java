package torganizer.core.matches;

import torganizer.core.entities.Player;

public class BestOfMatchSinglePlayer extends AbstractMatchSeries<Player, Game> {
	private final int numberOfSets;

	public BestOfMatchSinglePlayer(final int numberOfSets, final Player sideA, final Player sideB) {
		super(numberOfSets, sideA, sideB);
		if ((numberOfSets % 2) == 0) {
			throw new UnsupportedFormatException("Could not create a Best-Of-Series with " + numberOfSets + " games. Only odd numbers are supported");
		}
		this.numberOfSets = numberOfSets;
		refresh();
	}

	@Override
	public Game constructNewSet() {
		final Game game = new Game(getSideA(), getSideB());
		game.addCallbackObject(this);
		return game;
	}

	public class UnsupportedFormatException extends RuntimeException {
		private static final long serialVersionUID = -3594688140876149319L;

		public UnsupportedFormatException(final String string) {
			super(string);
		}
	}

	@Override
	public Player calculateWinner() {
		final int scoreSideA = getScore(getSideA());
		final int scoreSideB = getScore(getSideB());
		if (scoreSideA > (numberOfSets / 2.)) {
			return getSideA();
		}
		if (scoreSideB > (numberOfSets / 2.)) {
			return getSideB();
		}
		return null;
	}
}
