package torganizer.core;

public class SubmittedGameResult {
	private Player submitter;
	private Player winner;

	public SubmittedGameResult(Player submitter, Player winner) {
		this.submitter = submitter;
		this.winner = winner;
	}

	public Player getSubmitter() {
		return submitter;
	}

	public void setSubmitter(Player submitter) {
		this.submitter = submitter;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}
}
