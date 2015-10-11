package torganizer.core.matches;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.Player;

public class Game extends AbstractMatch<Player> {
	private final List<SubmittedGameResult> submittedGameResults;
	private LocalDateTime earliestTime;
	private LocalDateTime latestTime;
	private LocalDateTime playTime;

	public Game(final Player playerA, final Player playerB) {
		super(playerA, playerB);
		submittedGameResults = new ArrayList<SubmittedGameResult>();
	}

	public Game() {
		submittedGameResults = new ArrayList<SubmittedGameResult>();
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
		if (!submitter.equals(getSideA()) && !submitter.equals(getSideB()) && !submitter.isAdmin()) {
			throw new SubmitterIsNotPlayerException("The player " + submitter + " is not a player in this match!");
		}
		for (final SubmittedGameResult existingResult : submittedGameResults) {
			if (existingResult.getSubmitter().equals(submitter)) {
				throw new GameResultSubmittedAlreadyException("The player " + submitter + " did already submit a result!");
			}
		}
		submittedGameResults.add(new SubmittedGameResult(submitter, winner));

		refresh();
	}

	@Override
	public Player calculateWinner() {
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

	@Override
	public void callback(final IToEntity sender) {
	}

	public void setEarliestTime(final LocalDateTime time) {
		this.earliestTime = time;
	}

	public void setLatestTime(final LocalDateTime time) {
		this.latestTime = time;
	}

	public LocalDateTime getEarliestTime() {
		return earliestTime;
	}

	public LocalDateTime getLatestTime() {
		return latestTime;
	}

	public void setPlayTime(final LocalDateTime playTime) {
		if (playTime.isBefore(earliestTime)) {
			throw new InvalidPlayTimeException("The time " + playTime + " is before the earliest acceptable time of " + earliestTime);
		}
		if (playTime.isAfter(latestTime)) {
			throw new InvalidPlayTimeException("The time " + playTime + " is after the latest acceptable time of " + latestTime);
		}
		this.playTime = playTime;
	}

	public class InvalidPlayTimeException extends RuntimeException {
		private static final long serialVersionUID = 7458638426213805978L;

		public InvalidPlayTimeException(final String string) {
			super(string);
		}
	}
}
