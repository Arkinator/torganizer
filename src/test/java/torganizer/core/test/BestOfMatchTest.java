package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import torganizer.core.BestOfMatchSinglePlayer;
import torganizer.core.Player;

public class BestOfMatchTest {
	@Test
	public void simpleSetTest() {
		final Player playerA = new Player("playerA");
		final Player playerB = new Player("playerB");
		final BestOfMatchSinglePlayer set = new BestOfMatchSinglePlayer(3, playerA, playerB);
	}

	@Test
	public void testOneRoundPlayed() {
		final Player playerA = new Player("playerA");
		final Player playerB = new Player("playerB");
		final BestOfMatchSinglePlayer set = new BestOfMatchSinglePlayer(3, playerA, playerB);
		set.getSet(0).submitPlayerResult(playerA, playerA);
		set.getSet(0).submitPlayerResult(playerB, playerA);
		assertNull(set.getWinner());
	}

	@Test
	public void testTwoRoundsPlayed_Decided() {
		final Player playerA = new Player("playerA");
		final Player playerB = new Player("playerB");
		final BestOfMatchSinglePlayer set = new BestOfMatchSinglePlayer(3, playerA, playerB);
		set.getSet(0).submitPlayerResult(playerA, playerA);
		set.getSet(0).submitPlayerResult(playerB, playerA);
		set.getSet(1).submitPlayerResult(playerA, playerA);
		set.getSet(1).submitPlayerResult(playerB, playerA);
		assertEquals(playerA, set.getWinner());
	}

	@Test(expected = BestOfMatchSinglePlayer.UnsupportedFormatException.class)
	public void createBestOf4Failure() {
		final Player playerA = new Player("playerA");
		final Player playerB = new Player("playerB");
		final BestOfMatchSinglePlayer set = new BestOfMatchSinglePlayer(4, playerA, playerB);
	}
}
