package torganizer.core.matches;

import java.util.UUID;

import torganizer.core.entities.IToEntity;
import torganizer.core.persistance.orm.MatchOrm;

public class Game extends AbstractMatch {
	private UUID sideASubmittedResult;
	private UUID sideBSubmittedResult;
	private UUID adminSubmittedResult;

	public Game(final UUID playerA, final UUID playerB, final String name) {
		super(playerA, playerB, name);
		refresh();
	}

	public Game(final MatchOrm orm) {
		super(orm);
	}

	public Game(final String name) {
		super(name);
	}

	public void submitResultSideA(final UUID winner) {
		sideASubmittedResult = winner;

		refresh();
	}

	public void submitResultSideB(final UUID winner) {
		sideBSubmittedResult = winner;

		refresh();
	}

	public void submitResultAdmin(final UUID winner) {
		adminSubmittedResult = winner;

		refresh();
	}

	@Override
	public UUID calculateWinner() {
		if (adminSubmittedResult != null) {
			return adminSubmittedResult;
		}
		if ((getSideA() == null) && (getSideB() != null)) {
			return getSideB();
		}
		if ((getSideB() == null) && (getSideA() != null)) {
			return getSideA();
		}
		if (sideBSubmittedResult == null) {
			return sideASubmittedResult;
		}
		if (sideASubmittedResult == null) {
			return sideBSubmittedResult;
		}
		if (sideASubmittedResult == sideBSubmittedResult) {
			return sideBSubmittedResult;
		}
		return null;
	}

	@Override
	public void callback(final IToEntity sender) {
	}
}
