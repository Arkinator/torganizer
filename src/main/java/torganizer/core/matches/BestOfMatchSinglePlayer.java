package torganizer.core.matches;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.Player;

public class BestOfMatchSinglePlayer extends AbstractMatchSeries<Player, Game> {
	private final int numberOfSets;

	public BestOfMatchSinglePlayer(final int numberOfSets, final Player sideA, final Player sideB) {
		super(numberOfSets, sideA, sideB);
		if ((numberOfSets % 2) == 0) {
			throw new UnsupportedFormatException("Could not create a Best-Of-Series with " + numberOfSets + " games. Only odd numbers are supported");
		}
		this.numberOfSets = numberOfSets;
	}

	@Override
	public Game constructNewSet() {
		return new Game(getSideA(), getSideB());
	}

	@Override
	public Player getWinner() {
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

	public class UnsupportedFormatException extends RuntimeException {
		private static final long serialVersionUID = -3594688140876149319L;

		public UnsupportedFormatException(final String string) {
			super(string);
		}
	}

	public void callback(final IToEntity sender) {
		// TODO Auto-generated method stub

	}
}
