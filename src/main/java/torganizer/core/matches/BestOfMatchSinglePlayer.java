package torganizer.core.matches;

import java.util.UUID;

import torganizer.core.entities.Player;
import torganizer.core.persistance.orm.MatchOrm;
import torganizer.core.types.IPlayerShortInfo;
import torganizer.core.types.MatchType;
import torganizer.core.types.PlayerShortInfo;
import torganizer.utils.GlobalUtilities;

public class BestOfMatchSinglePlayer extends AbstractMatchSeries<Game> {
	private final int numberOfSets;
	private final IPlayerShortInfo sideAInfo;
	private final IPlayerShortInfo sideBInfo;

	public BestOfMatchSinglePlayer(final int numberOfSets, final Player sideA, final Player sideB, final String name) {
		super(numberOfSets, getUidNullSafe(sideA), getUidNullSafe(sideB), name);
		if ((numberOfSets % 2) == 0) {
			throw new UnsupportedFormatException("Could not create a Best-Of-Series with " + numberOfSets + " games. Only odd numbers are supported");
		}
		this.numberOfSets = numberOfSets;
		this.sideAInfo = PlayerShortInfo.createSideAInfo(sideA, getEntityOrm().getMatch());
		this.sideBInfo = PlayerShortInfo.createSideBInfo(sideB, getEntityOrm().getMatch());
		refresh();
	}

	private static UUID getUidNullSafe(final Player player) {
		if (player == null) {
			return null;
		} else {
			return player.getUid();
		}
	}

	public BestOfMatchSinglePlayer(final MatchOrm match) {
		super(match);
		this.numberOfSets = match.getNumberOfSets();
		this.sideAInfo = PlayerShortInfo.loadSideAInfo(getEntityOrm().getMatch());
		this.sideBInfo = PlayerShortInfo.loadSideBInfo(getEntityOrm().getMatch());
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

	public IPlayerShortInfo getSideAInfo() {
		return sideAInfo;
	}

	public IPlayerShortInfo getSideBInfo() {
		return sideBInfo;
	}
}
