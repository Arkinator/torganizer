package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.core.matches.CtlMatch;
import torganizer.core.matches.GenericMatch;
import torganizer.core.matches.IllegalSetNumberSpecifiedException;

public class MatchTest {
	private static Player playerA_1;
	private static Player playerA_2;
	private static Player playerA_3;
	private static Player playerB_1;
	private static Player playerB_2;
	private static Player playerB_3;
	private static Team teamA;
	private static Team teamB;

	@BeforeClass
	public static void intialize() {
		playerA_1 = new Player("playerA1");
		playerA_2 = new Player("playerA2");
		playerA_3 = new Player("playerA3");

		playerB_1 = new Player("playerB1");
		playerB_2 = new Player("playerB2");
		playerB_3 = new Player("playerB3");

		teamA = new Team("teamA");
		teamB = new Team("teamB");

		teamA.addPlayer(playerA_1);
		teamA.addPlayer(playerA_2);
		teamA.addPlayer(playerA_3);

		teamB.addPlayer(playerB_1);
		teamB.addPlayer(playerB_2);
		teamB.addPlayer(playerB_3);
	}

	@Test
	public void setUp3v3Match() {
		final CtlMatch match = new CtlMatch(3, teamA.getUid(), teamB.getUid(), "test");
		assertEquals(3, match.getNumberOfSets());
	}

	@Test(expected = IllegalSetNumberSpecifiedException.class)
	public void setNonExistentSet() {
		final CtlMatch match = new CtlMatch(3, teamA.getUid(), teamB.getUid(), "test");
		match.setPlayer(1, 3, playerB_3.getUid());
	}

	@Test(expected = CtlMatch.IllegalTeamNumberSpecifiedException.class)
	public void setNonExistentTeam() {
		final CtlMatch match = new CtlMatch(3, teamA.getUid(), teamB.getUid(), "test");
		match.setPlayer(2, 1, playerB_3.getUid());
	}

	@Test
	public void addPlayersToMatch_3v3() {
		final CtlMatch match = construct3v3Match();

		assertEquals(playerA_1.getUid(), match.getSet(0).getSideA());
		assertEquals(playerB_1.getUid(), match.getSet(0).getSideB());

		assertEquals(playerA_2.getUid(), match.getSet(1).getSideA());
		assertEquals(playerB_2.getUid(), match.getSet(1).getSideB());

		assertEquals(playerA_3.getUid(), match.getSet(2).getSideA());
		assertEquals(playerB_3.getUid(), match.getSet(2).getSideB());
	}

	@Test
	public void testEmptyWinnerOnNewMatch() {
		final GenericMatch match = new CtlMatch(3, teamA.getUid(), teamB.getUid(), "test");
		assertNull(match.getWinner());
	}

	@Test
	public void testWinnerOnOneSidedMatch() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final CtlMatch match = construct3v3Match();
		match.getSet(0).submitResultAdmin(playerA_1.getUid());
		match.getSet(1).submitResultAdmin(playerA_2.getUid());
		match.getSet(2).submitResultAdmin(playerA_3.getUid());

		assertEquals(teamA.getUid(), match.getWinner());
		assertTrue(match.isPlayed());
	}

	@Test
	public void testScoreOnOneSidedMatch() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final CtlMatch match = construct3v3Match();
		match.getSet(0).submitResultAdmin(playerA_1.getUid());
		match.getSet(1).submitResultAdmin(playerA_2.getUid());
		match.getSet(2).submitResultAdmin(playerA_3.getUid());

		assertEquals(3, match.getScoreSideA());
	}

	@Test
	public void testWinnerOnTightMatch() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final CtlMatch match = construct3v3Match();
		match.getSet(0).submitResultAdmin(playerB_1.getUid());
		match.getSet(1).submitResultAdmin(playerA_2.getUid());
		match.getSet(2).submitResultAdmin(playerA_3.getUid());

		assertEquals(teamA.getUid(), match.getWinner());
		assertTrue(match.isPlayed());
	}

	protected CtlMatch construct3v3Match() {
		final CtlMatch match = new CtlMatch(3, teamA.getUid(), teamB.getUid(), "test");
		match.setPlayer(0, 0, playerA_1.getUid());
		match.setPlayer(0, 1, playerA_2.getUid());
		match.setPlayer(0, 2, playerA_3.getUid());

		match.setPlayer(1, 0, playerB_1.getUid());
		match.setPlayer(1, 1, playerB_2.getUid());
		match.setPlayer(1, 2, playerB_3.getUid());
		return match;
	}
}
