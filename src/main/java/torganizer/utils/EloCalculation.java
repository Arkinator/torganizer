package torganizer.utils;

public class EloCalculation {

	private final double playerARating;
	private final double playerBRating;
	private double factualResult;
	private static final double ratingDiffScaling = 400;
	public static final double defaultEloValue = 1700.;

	public EloCalculation(final double playerARating, final double playerBRating) {
		this.playerARating = playerARating;
		this.playerBRating = playerBRating;
	}

	public void playerAWins() {
		factualResult = 1.;
	}

	public void playerBWins() {
		factualResult = 0.;
	}

	public double getPlayerAAdjustment() {
		return 10 * (factualResult() - getExpectedScore());
	}

	public double getPlayerBAdjustment() {
		return 10 * ((1. - factualResult()) - (1. - getExpectedScore()));
	}

	private double getDiff() {
		return playerBRating - playerARating;
	}

	public double getExpectedScore() {
		return 1. / (1 + Math.pow(10, getDiff() / ratingDiffScaling));
	}

	public double factualResult() {
		return factualResult;
	}

	public void setFactualResult(final double factualResult) {
		this.factualResult = factualResult;
	}
}
