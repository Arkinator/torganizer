package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import torganizer.core.ApplicationContextProvider;
import torganizer.core.entities.Player;
import torganizer.core.persistance.objectservice.DummyObjectService;
import torganizer.core.tournaments.TrisTournament;
import torganizer.core.types.StarcraftLeague;
import torganizer.core.types.StarcraftRace;
import torganizer.utils.TristanPlayerInfo;

public class SexySwissTest {
	static Player[] playerArray;
	private final static int maxNumberPlayers = 6;
	static List<Player> playerList;
	private static Player admin;

	@BeforeClass
	public static void initializeClass() {
		admin = new Player("admin");
		admin.setAdmin(true);
		playerArray = new Player[maxNumberPlayers];
		playerList = new ArrayList<Player>();
		for (int i = 0; i < playerArray.length; i++) {
			playerArray[i] = new Player("player" + i);
			playerArray[i].setRace(StarcraftRace.Terran);
			playerArray[i].setLeague(StarcraftLeague.values()[6 - i]);
			playerList.add(playerArray[i]);
		}
		ApplicationContextProvider.setGlobalObjectService(new DummyObjectService(Arrays.asList(playerArray)));
	}

	@Test
	public void basicInstantiationTest() {
		final TrisTournament tournament = new TrisTournament(1, 1, playerList, "");
		assertNull(tournament.getWinner());
	}

	@Test
	public void addPlayerListTest() {
		final TrisTournament tournament = new TrisTournament(1, 1, playerList, "");
		assertEquals(tournament.getParticipants().size(), 6);
		assertEquals(tournament.getMatchesForRound(0).size(), 3);
	}

	@Test
	public void addPlayerListTest_unevenNumberOfPlayers() {
		final List<Player> unevenPlayerList = new ArrayList<Player>();
		unevenPlayerList.addAll(playerList);
		unevenPlayerList.remove(0);
		final TrisTournament tournament = new TrisTournament(1, 1, unevenPlayerList, "");
		assertEquals(tournament.getParticipants().size(), 5);
		assertEquals(tournament.getMatchesForRound(0).size(), 3);
		assertTrue(tournament.getMatchesForRound(0).get(2).isPlayed());
	}

	@Test
	public void testCorrectRoundCalculation() {
		final TrisTournament tournament = new TrisTournament(1, 1, playerList, "");
		assertEquals(0, tournament.getCurrentRound());
		tournament.getBestOfMatchForRound(0).get(0).getSet(0).submitResultAdmin(tournament.getBestOfMatchForRound(0).get(0).getSideA());
		assertEquals(0, tournament.getCurrentRound());
		tournament.getBestOfMatchForRound(0).get(1).getSet(0).submitResultAdmin(tournament.getBestOfMatchForRound(0).get(1).getSideA());
		assertEquals(0, tournament.getCurrentRound());
		tournament.getBestOfMatchForRound(0).get(2).getSet(0).submitResultAdmin(tournament.getBestOfMatchForRound(0).get(2).getSideA());
		assertEquals(1, tournament.getCurrentRound());
	}

	@Test
	public void testEloStartingValues() {
		final TrisTournament tournament = createNewTournament();
		for (int i = 0; i < playerArray.length; i++) {
			assertEquals(playerArray[i].getUid(), tournament.getStanding().get(i).getPlayer());
		}
	}

	private TrisTournament createNewTournament() {
		final TrisTournament tournament = new TrisTournament(30, 1, playerList, "");
		double eloValue = 3000;
		for (final Player player : playerArray) {
			tournament.setInitialEloValue(player.getUid(), eloValue);
			eloValue -= 100;
		}
		tournament.updateNextRound();
		return tournament;
	}

	@Test
	public void testCorrectMatches() {
		final TrisTournament tournament = createNewTournament();
		// first Round
		assertEquals(playerArray[0].getUid(), tournament.getMatchesForRound(0).get(0).getSideA());
		assertEquals(playerArray[1].getUid(), tournament.getMatchesForRound(0).get(0).getSideB());
		assertEquals(playerArray[2].getUid(), tournament.getMatchesForRound(0).get(1).getSideA());
		assertEquals(playerArray[3].getUid(), tournament.getMatchesForRound(0).get(1).getSideB());
		assertEquals(playerArray[4].getUid(), tournament.getMatchesForRound(0).get(2).getSideA());
		assertEquals(playerArray[5].getUid(), tournament.getMatchesForRound(0).get(2).getSideB());
		tournament.getMatchesForRound(0).get(0).submitResultAdmin(playerArray[0].getUid(), 1, 0);
		tournament.getMatchesForRound(0).get(1).submitResultAdmin(playerArray[2].getUid(), 1, 0);
		tournament.getMatchesForRound(0).get(2).submitResultAdmin(playerArray[4].getUid(), 1, 0);
		// second Round
		assertEquals(playerArray[0].getUid(), tournament.getMatchesForRound(1).get(0).getSideA());
		assertEquals(playerArray[2].getUid(), tournament.getMatchesForRound(1).get(0).getSideB());
		assertEquals(playerArray[1].getUid(), tournament.getMatchesForRound(1).get(1).getSideA());
		assertEquals(playerArray[3].getUid(), tournament.getMatchesForRound(1).get(1).getSideB());
		assertEquals(playerArray[4].getUid(), tournament.getMatchesForRound(1).get(2).getSideA());
		assertEquals(playerArray[5].getUid(), tournament.getMatchesForRound(1).get(2).getSideB());
	}

	@Test
	public void testRaceChange() {
		final TrisTournament tournament = new TrisTournament(4, 1, playerList, "");
		assertEquals(StarcraftRace.Terran, tournament.getInfo(playerArray[0].getUid()).getRaceForRound(0));
		assertEquals(StarcraftRace.Terran, tournament.getInfo(playerArray[0].getUid()).getRaceForRound(1));
		tournament.addNewRaceChange(playerArray[0].getUid(), StarcraftRace.Zerg);
		assertEquals(StarcraftRace.Terran, tournament.getInfo(playerArray[0].getUid()).getRaceForRound(0));
		assertEquals(StarcraftRace.Zerg, tournament.getInfo(playerArray[0].getUid()).getRaceForRound(1));
	}

	@Test
	public void testDrawnMatch() {
		final TrisTournament tournament = new TrisTournament(4, 1, playerList, "");
		final double elo0Before = tournament.getInfo(playerArray[0].getUid()).getEloForRound(0);
		assertEquals(0, tournament.getCurrentRound());
		tournament.getMatchesForRound(0).get(0).submitResultAdmin(null, 0, 0);
		assertEquals(0, tournament.getCurrentRound());
		tournament.getMatchesForRound(0).get(1).submitResultAdmin(playerArray[2].getUid(), 1, 0);
		tournament.getMatchesForRound(0).get(2).submitResultAdmin(playerArray[4].getUid(), 1, 0);

		final double elo0After = tournament.getInfo(playerArray[0].getUid()).getEloForRound(0);
		final double elo1After = tournament.getInfo(playerArray[0].getUid()).getEloForRound(1);
		assertEquals(elo0Before, elo0After, 0.01);
		assertEquals(elo0Before, elo1After, 0.01);
		final TristanPlayerInfo infoPlayer1 = tournament.getInfo(playerArray[1].getUid());
		assertEquals(infoPlayer1.getEloForRound(0), infoPlayer1.getEloForRound(1), 0.01);

		assertEquals(1, tournament.getCurrentRound());
		assertEquals(playerArray[2].getUid(), tournament.getMatchesForRound(1).get(0).getSideA());
		assertEquals(playerArray[4].getUid(), tournament.getMatchesForRound(1).get(0).getSideB());
		assertEquals(playerArray[0].getUid(), tournament.getMatchesForRound(1).get(1).getSideA());
		assertEquals(playerArray[5].getUid(), tournament.getMatchesForRound(1).get(1).getSideB());
		assertEquals(playerArray[1].getUid(), tournament.getMatchesForRound(1).get(2).getSideA());
		assertEquals(playerArray[3].getUid(), tournament.getMatchesForRound(1).get(2).getSideB());
	}

	@Test
	public void testSecondRoundMatchesWhileStillInFirstRound() {
		final TrisTournament tournament = new TrisTournament(4, 1, playerList, "");

		// first Round
		assertEquals(playerArray[0].getUid(), tournament.getMatchesForRound(0).get(0).getSideA());
		assertEquals(playerArray[1].getUid(), tournament.getMatchesForRound(0).get(0).getSideB());
		assertEquals(playerArray[2].getUid(), tournament.getMatchesForRound(0).get(1).getSideA());
		assertEquals(playerArray[3].getUid(), tournament.getMatchesForRound(0).get(1).getSideB());
		assertEquals(playerArray[4].getUid(), tournament.getMatchesForRound(0).get(2).getSideA());
		assertEquals(playerArray[5].getUid(), tournament.getMatchesForRound(0).get(2).getSideB());
		tournament.getMatchesForRound(0).get(0).submitResultAdmin(playerArray[0].getUid(), 1, 0);

		/**
		 * The Players facing each others are different in this test compared to
		 * 'testCorrectMatches' since the results are different(i.e. not yet in)
		 */
		assertEquals(playerArray[0].getUid(), tournament.getMatchesForRound(1).get(0).getSideA());
		assertEquals(playerArray[2].getUid(), tournament.getMatchesForRound(1).get(0).getSideB());
		assertEquals(playerArray[3].getUid(), tournament.getMatchesForRound(1).get(1).getSideA());
		assertEquals(playerArray[4].getUid(), tournament.getMatchesForRound(1).get(1).getSideB());
		assertEquals(playerArray[5].getUid(), tournament.getMatchesForRound(1).get(2).getSideA());
		assertEquals(playerArray[1].getUid(), tournament.getMatchesForRound(1).get(2).getSideB());
	}

	@Test
	public void testHolidayInFirstWeek() {
		final TrisTournament tournament = new TrisTournament(4, 1, playerList, "");
		tournament.setPlayerToHoliday(playerArray[0].getUid());
		tournament.updateNextRound();
		tournament.updateNextRound();
		tournament.updateNextRound();
		tournament.updateNextRound();
		tournament.updateNextRound();
		tournament.setPlayerToReturnFromHoliday(playerArray[0].getUid());

		// first Round
		assertEquals(playerArray[1].getUid(), tournament.getMatchesForRound(0).get(0).getSideA());
		assertEquals(playerArray[2].getUid(), tournament.getMatchesForRound(0).get(0).getSideB());
		assertEquals(playerArray[3].getUid(), tournament.getMatchesForRound(0).get(1).getSideA());
		assertEquals(playerArray[4].getUid(), tournament.getMatchesForRound(0).get(1).getSideB());
		assertEquals(playerArray[5].getUid(), tournament.getMatchesForRound(0).get(2).getSideA());
		assertEquals(null, tournament.getMatchesForRound(0).get(2).getSideB());
		// assertEquals(playerArray[0].getUid(),
		// tournament.getMatchesForRound(0).get(3).getSideA());
		// assertEquals(null,
		// tournament.getMatchesForRound(0).get(3).getSideB());
		assertEquals(0, tournament.getCurrentRound());
		tournament.getMatchesForRound(0).get(0).submitResultAdmin(playerArray[1].getUid(), 1, 0);
		assertEquals(0, tournament.getCurrentRound());
		tournament.getMatchesForRound(0).get(1).submitResultAdmin(playerArray[3].getUid(), 1, 0);
		assertEquals(1, tournament.getCurrentRound());
		final TristanPlayerInfo playerInfo = tournament.getInfo(playerArray[0].getUid());
		assertEquals(playerInfo.getEloForRound(0) - 40, playerInfo.getEloForRound(1), 0.1);
		assertEquals(playerInfo.getEloForRound(1), playerInfo.getElo(), 0.1);
	}

	@Test
	public void testHolidayOption() {
		final TrisTournament tournament = new TrisTournament(4, 1, playerList, "");
		tournament.setPlayerToHoliday(playerArray[0].getUid());
		tournament.getMatchesForRound(0).get(0).submitResultAdmin(playerArray[0].getUid(), 1, 0);
		tournament.getMatchesForRound(0).get(1).submitResultAdmin(playerArray[2].getUid(), 1, 0);
		tournament.getMatchesForRound(0).get(2).submitResultAdmin(playerArray[4].getUid(), 1, 0);

		// second Round
		assertNull(tournament.getMatchesForRound(1).get(2).getSideB());
	}
}
