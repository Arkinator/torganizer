package torganizer.core.matches;

import java.util.UUID;

import torganizer.utils.GlobalUtilities;

public class BestOfMatchSinglePlayer extends AbstractMatchSeries<Game> {
	private final int numberOfSets;

	public BestOfMatchSinglePlayer(final int numberOfSets, final UUID sideA, final UUID sideB, final String name) {
		super(numberOfSets, sideA, sideB, name);
		if ((numberOfSets % 2) == 0) {
			throw new UnsupportedFormatException("Could not create a Best-Of-Series with " + numberOfSets + " games. Only odd numbers are supported");
		}
		this.numberOfSets = numberOfSets;
		refresh();
	}

	@Override
	public Game constructNewSet() {
		final Game game = new Game(getSideA(), getSideB(), GlobalUtilities.createNewSetName(this));
		game.addCallbackObject(this);
		return game;
	}

	@Override
	public UUID calculateWinner() {
		final int scoreSideA = getScoreSideA();
		final int scoreSideB = getScoreSideB();
		if (scoreSideA > (numberOfSets / 2.)) {
			return getSideA();
		}
		if (scoreSideB > (numberOfSets / 2.)) {
			return getSideB();
		}
		return null;
	}

	public class UnsupportedFormatException extends RuntimeException {
		private static final long serialVersionUID = -3594688140876149319L;

		public UnsupportedFormatException(final String string) {
			super(string);
		}
	}
}
