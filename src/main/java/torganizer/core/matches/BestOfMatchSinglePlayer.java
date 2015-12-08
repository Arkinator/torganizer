package torganizer.core.matches;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import torganizer.core.persistance.orm.EntityOrm;
import torganizer.core.persistance.orm.MatchOrm;
import torganizer.core.types.MatchType;
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

	public BestOfMatchSinglePlayer(final MatchOrm match) {
		super(match);
		this.numberOfSets = match.getNumberOfSets();
	}

	@Override
	protected void constructMatchOrm() {
		super.constructMatchOrm();
		getEntityOrm().getMatch().setType(MatchType.SINGLE_PLAYER_MATCH_SERIES);
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

	private List<EntityOrm> buildSetEntities() {
		final List<EntityOrm> result = new ArrayList<EntityOrm>();
		for (final GenericMatch match : getSets()) {
			result.add(match.getEntityOrm());
		}
		return result;
	}
}
