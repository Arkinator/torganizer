package torganizer.util.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import torganizer.utils.EloCalculation;

public class EloUtilTester {
	final int playerARating = 2806;
	final int playerBRating = 2577;

	@Test
	public void testWithWikiNumbers_playerAWinning() {
		final EloCalculation calc = new EloCalculation(playerARating, playerBRating);
		assertEquals(0.789, calc.getExpectedScore(), 0.02);
		calc.playerAWins();
		assertEquals(2.11, calc.getPlayerAAdjustment(), 0.02);
		assertEquals(-2.11, calc.getPlayerBAdjustment(), 0.02);
	}

	@Test
	public void testWithWikiNumbers_playerBWinning() {
		final EloCalculation calc = new EloCalculation(playerARating, playerBRating);
		calc.playerBWins();
		assertEquals(-7.89, calc.getPlayerAAdjustment(), 0.02);
		assertEquals(7.89, calc.getPlayerBAdjustment(), 0.02);
	}

	@Test
	public void testWithWikiNumbers_draw() {
		final EloCalculation calc = new EloCalculation(playerARating, playerBRating);
		calc.setFactualResult(0.5);
		assertEquals(-2.88, calc.getPlayerAAdjustment(), 0.05);
		assertEquals(2.88, calc.getPlayerBAdjustment(), 0.05);
	}
}
