package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.core.matches.CtlMatch;
import torganizer.core.matches.IGenericMatch;
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
		final CtlMatch match = new CtlMatch(3, teamA, teamB);
		assertEquals(3, match.getNumberOfSets());
	}

	@Test(expected = IllegalSetNumberSpecifiedException.class)
	public void setNonExistentSet() {
		final CtlMatch match = new CtlMatch(3, teamA, teamB);
		match.setPlayer(1, 3, playerB_3);
	}

	@Test(expected = CtlMatch.IllegalTeamNumberSpecifiedException.class)
	public void setNonExistentTeam() {
		final CtlMatch match = new CtlMatch(3, teamA, teamB);
		match.setPlayer(2, 1, playerB_3);
	}

	@Test
	public void addPlayersToMatch_3v3() {
		final CtlMatch match = construct3v3Match();

		assertEquals(playerA_1, match.getSet(0).getSideA());
		assertEquals(playerB_1, match.getSet(0).getSideB());

		assertEquals(playerA_2, match.getSet(1).getSideA());
		assertEquals(playerB_2, match.getSet(1).getSideB());

		assertEquals(playerA_3, match.getSet(2).getSideA());
		assertEquals(playerB_3, match.getSet(2).getSideB());
	}

	@Test
	public void testEmptyWinnerOnNewMatch() {
		final IGenericMatch match = new CtlMatch(3, teamA, teamB);
		assertNull(match.getWinner());
	}

	@Test
	public void testWinnerOnOneSidedMatch() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final CtlMatch match = construct3v3Match();
		match.getSet(0).submitPlayerResult(admin, playerA_1);
		match.getSet(1).submitPlayerResult(admin, playerA_2);
		match.getSet(2).submitPlayerResult(admin, playerA_3);

		assertEquals(teamA, match.getWinner());
	}

	@Test
	public void testScoreOnOneSidedMatch() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final CtlMatch match = construct3v3Match();
		match.getSet(0).submitPlayerResult(admin, playerA_1);
		match.getSet(1).submitPlayerResult(admin, playerA_2);
		match.getSet(2).submitPlayerResult(admin, playerA_3);

		assertEquals(3, match.getScore(teamA));
	}

	@Test
	public void testWinnerOnSplitMatch() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final CtlMatch match = construct3v3Match();
		match.getSet(0).submitPlayerResult(admin, playerB_1);
		match.getSet(1).submitPlayerResult(admin, playerA_2);
		match.getSet(2).submitPlayerResult(admin, playerA_3);

		assertEquals(teamA, match.getWinner());
	}

	protected CtlMatch construct3v3Match() {
		final CtlMatch match = new CtlMatch(3, teamA, teamB);
		match.setPlayer(0, 0, playerA_1);
		match.setPlayer(0, 1, playerA_2);
		match.setPlayer(0, 2, playerA_3);

		match.setPlayer(1, 0, playerB_1);
		match.setPlayer(1, 1, playerB_2);
		match.setPlayer(1, 2, playerB_3);
		return match;
	}
}
