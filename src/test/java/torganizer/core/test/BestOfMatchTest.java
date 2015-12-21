package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import torganizer.core.entities.Player;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.matches.GenericMatch;

public class BestOfMatchTest {
	@Test
	public void testOneRoundPlayed() {
		final Player playerA = new Player("playerA");
		final Player playerB = new Player("playerB");
		final BestOfMatchSinglePlayer set = new BestOfMatchSinglePlayer(3, playerA.getUid(), playerB.getUid(), "");
		set.getSet(0).submitResultSideA(playerA.getUid());
		set.getSet(0).submitResultSideB(playerA.getUid());
		assertNull(set.getWinner());
		assertFalse(set.isPlayed());
	}

	@Test
	public void testTwoRoundsPlayed_Decided() {
		final Player playerA = new Player("playerA");
		final Player playerB = new Player("playerB");
		final BestOfMatchSinglePlayer set = new BestOfMatchSinglePlayer(3, playerA.getUid(), playerB.getUid(), "");
		set.getSet(0).submitResultSideA(playerA.getUid());
		set.getSet(0).submitResultSideB(playerA.getUid());
		set.getSet(1).submitResultSideA(playerA.getUid());
		set.getSet(1).submitResultSideB(playerA.getUid());
		assertEquals(playerA.getUid(), set.getWinner());
	}

	@Test(expected = BestOfMatchSinglePlayer.UnsupportedFormatException.class)
	public void createBestOf4Failure() {
		final Player playerA = new Player("playerA");
		final Player playerB = new Player("playerB");
		new BestOfMatchSinglePlayer(4, playerA.getUid(), playerB.getUid(), "");
	}

	@Test
	public void testForOneSideBeingNull_byeShouldHappen() {
		final Player playerA = new Player("playerA");
		final BestOfMatchSinglePlayer set = new BestOfMatchSinglePlayer(3, playerA.getUid(), null, "");
		assertEquals(playerA.getUid(), set.getWinner());
		assertTrue(set.isPlayed());
	}

	@Test
	public void testSexyResultSubmittance() {
		final Player playerA = new Player("playerA");
		final Player playerB = new Player("playerB");
		final BestOfMatchSinglePlayer set = new BestOfMatchSinglePlayer(3, playerA.getUid(), playerB.getUid(), "");
		final GenericMatch castedPtr = set;
		castedPtr.submitResultAdmin(playerA.getUid(), 2, 1);
		assertEquals(playerA.getUid(), set.getWinner());
		assertTrue(set.isPlayed());
		assertEquals(2, set.getScoreSideA());
		assertEquals(1, set.getScoreSideB());
	}

	@Test
	public void testDrawnSeries() {
		final Player playerA = new Player("playerA");
		final Player playerB = new Player("playerB");
		final BestOfMatchSinglePlayer match = new BestOfMatchSinglePlayer(3, playerA.getUid(), playerB.getUid(), "");
		match.submitResultAdmin(null, 0, 0);
		assertNull(match.getWinner());
		assertTrue(match.isPlayed());
	}
}
