package torganizer.core;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private Player playerA;
	private Player playerB;
	private final List<SubmittedGameResult> submittedGameResults;

	public Game(final Player playerA, final Player playerB) {
		submittedGameResults = new ArrayList<SubmittedGameResult>();
		this.playerA = playerA;
		this.playerB = playerB;
	}

	public Game() {
		submittedGameResults = new ArrayList<SubmittedGameResult>();
	}

	public Player getPlayerB() {
		return playerB;
	}

	public void setPlayerB(final Player playerB) {
		this.playerB = playerB;
	}

	public Player getPlayerA() {
		return playerA;
	}

	public void setPlayerA(final Player playerA) {
		this.playerA = playerA;
	}

	public Player getWinner() {
		final Player winner = getAdminVoteIfPresent();
		if (winner != null) {
			return winner;
		}
		if (submittedGameResults.size() != 2) {
			return null;
		}
		if (submittedGameResults.get(0).getWinner().equals(submittedGameResults.get(1).getWinner())) {
			return submittedGameResults.get(0).getWinner();
		} else {
			return null;
		}
	}

	protected Player getAdminVoteIfPresent() {
		for (final SubmittedGameResult submittedGameResult : submittedGameResults) {
			if (submittedGameResult.getSubmitter().isAdmin()) {
				return submittedGameResult.getWinner();
			}
		}
		return null;
	}

	public void submitPlayerResult(final Player submitter, final Player winner) {
		if (!submitter.equals(playerA) && !submitter.equals(playerB) && !submitter.isAdmin()) {
			throw new SubmitterIsNotPlayerException("The player " + submitter + " is not a player in this match!");
		}
		for (final SubmittedGameResult existingResult : submittedGameResults) {
			if (existingResult.getSubmitter().equals(submitter)) {
				throw new GameResultSubmittedAlreadyException("The player " + submitter + " did already submit a result!");
			}
		}
		submittedGameResults.add(new SubmittedGameResult(submitter, winner));
	}

	public List<SubmittedGameResult> getSubmittedResults() {
		return submittedGameResults;
	}

	public static class GameResultSubmittedAlreadyException extends RuntimeException {
		private static final long serialVersionUID = 2795439708511522527L;

		public GameResultSubmittedAlreadyException(final String string) {
			super(string);
		}
	}

	public static class SubmitterIsNotPlayerException extends RuntimeException {
		private static final long serialVersionUID = -3010646733741702377L;

		public SubmitterIsNotPlayerException(final String string) {
			super(string);
		}
	}
}
