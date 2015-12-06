package torganizer.core.matches;

import java.util.UUID;

public class SubmittedGameResult {
	private UUID submitter;
	private UUID winner;

	public SubmittedGameResult(final UUID submitter, final UUID winner) {
		this.submitter = submitter;
		this.winner = winner;
	}

	public UUID getSubmitter() {
		return submitter;
	}

	public void setSubmitter(final UUID submitter) {
		this.submitter = submitter;
	}

	public UUID getWinner() {
		return winner;
	}

	public void setWinner(final UUID winner) {
		this.winner = winner;
	}
}
