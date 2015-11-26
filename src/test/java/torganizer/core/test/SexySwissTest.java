package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import torganizer.core.entities.Player;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.tournaments.TrisTournament;

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
			playerList.add(playerArray[i]);
		}
	}

	@Test
	public void basicInstantiationTest() {
		final TrisTournament tournament = new TrisTournament(1, 1, playerList);
		assertNull(tournament.getWinner());
	}

	@Test
	public void addPlayerListTest() {
		final TrisTournament tournament = new TrisTournament(1, 1, playerList);
		assertEquals(tournament.getParticipants().size(), 6);
		assertEquals(tournament.getMatchesForRound(0).size(), 3);
	}

	@Test
	public void addPlayerListTest_unevenNumberOfPlayers() {
		final List<Player> unevenPlayerList = new ArrayList<Player>();
		unevenPlayerList.addAll(playerList);
		unevenPlayerList.remove(0);
		final TrisTournament tournament = new TrisTournament(1, 1, unevenPlayerList);
		assertEquals(tournament.getParticipants().size(), 5);
		assertEquals(tournament.getMatchesForRound(0).size(), 3);
		assertNotNull(tournament.getMatchesForRound(0).get(2).getWinner());
	}

	@Test
	public void testCorrectRoundCalculation() {
		final TrisTournament tournament = new TrisTournament(1, 1, playerList);
		assertEquals(0, tournament.getCurrentRound());
		tournament.getBestOfMatchForRound(0).get(0).getSet(0).submitPlayerResult(admin, tournament.getBestOfMatchForRound(0).get(0).getSideA());
		assertEquals(0, tournament.getCurrentRound());
		tournament.getBestOfMatchForRound(0).get(1).getSet(0).submitPlayerResult(admin, tournament.getBestOfMatchForRound(0).get(1).getSideA());
		assertEquals(0, tournament.getCurrentRound());
		tournament.getBestOfMatchForRound(0).get(2).getSet(0).submitPlayerResult(admin, tournament.getBestOfMatchForRound(0).get(2).getSideA());
		assertEquals(1, tournament.getCurrentRound());
	}

	@Test
	public void testEloStartingValues() {
		final TrisTournament tournament = createNewTournament();
		for (int i = 0; i < playerArray.length; i++) {
			assertEquals(playerArray[i], tournament.getStanding().get(i).getPlayer());
		}
	}

	private TrisTournament createNewTournament() {
		final TrisTournament tournament = new TrisTournament(30, 1, playerList);
		double eloValue = 3000;
		for (final Player player : playerArray) {
			tournament.setInitialEloValue(player, eloValue);
			eloValue -= 100;
		}
		tournament.updateNextRound();
		return tournament;
	}

	@Test
	public void testCorrectMatches() {
		final TrisTournament tournament = createNewTournament();
		// first Round
		assertEquals(playerArray[0], tournament.getMatchesForRound(0).get(0).getSideA());
		assertEquals(playerArray[1], tournament.getMatchesForRound(0).get(0).getSideB());
		assertEquals(playerArray[2], tournament.getMatchesForRound(0).get(1).getSideA());
		assertEquals(playerArray[3], tournament.getMatchesForRound(0).get(1).getSideB());
		assertEquals(playerArray[4], tournament.getMatchesForRound(0).get(2).getSideA());
		assertEquals(playerArray[5], tournament.getMatchesForRound(0).get(2).getSideB());
		playCurrentRound(tournament);
		// second Round
		assertEquals(playerArray[0], tournament.getMatchesForRound(1).get(0).getSideA());
		assertEquals(playerArray[2], tournament.getMatchesForRound(1).get(0).getSideB());
		assertEquals(playerArray[1], tournament.getMatchesForRound(1).get(1).getSideA());
		assertEquals(playerArray[3], tournament.getMatchesForRound(1).get(1).getSideB());
		assertEquals(playerArray[4], tournament.getMatchesForRound(1).get(2).getSideA());
		assertEquals(playerArray[5], tournament.getMatchesForRound(1).get(2).getSideB());
		playCurrentRound(tournament);
		for (int i = 2; i < 10; i++) {
			System.out.println();
			System.out.println(tournament.getMatchesForRound(i).get(0));
			System.out.println(tournament.getMatchesForRound(i).get(1));
			System.out.println(tournament.getMatchesForRound(i).get(2));
			playCurrentRound(tournament);
			System.out.println(tournament.getStanding());
		}
	}

	@Test
	public void eloDemo() {
		final List<Player> playerList = new ArrayList<>();
		playerList.add(new Player("silver1"));
		playerList.add(new Player("silver2"));
		playerList.add(new Player("gold1"));
		playerList.add(new Player("gold2"));
		playerList.add(new Player("plat1"));
		playerList.add(new Player("plat2"));
		playerList.add(new Player("dia1"));
		playerList.add(new Player("dia2"));
		playerList.add(new Player("master1"));
		playerList.add(new Player("master2"));
		final TrisTournament tournament = new TrisTournament(30, 1, playerList);
		tournament.setInitialEloValue(playerList.get(0), 1500);
		tournament.setInitialEloValue(playerList.get(1), 1500);
		tournament.setInitialEloValue(playerList.get(2), 1600);
		tournament.setInitialEloValue(playerList.get(3), 1600);
		tournament.setInitialEloValue(playerList.get(4), 1700);
		tournament.setInitialEloValue(playerList.get(5), 1700);
		tournament.setInitialEloValue(playerList.get(6), 1800);
		tournament.setInitialEloValue(playerList.get(7), 1800);
		tournament.setInitialEloValue(playerList.get(8), 1950);
		tournament.setInitialEloValue(playerList.get(9), 1950);
		tournament.updateNextRound();
		for (int i = 0; i < 10; i++) {
			System.out.println();
			System.out.println(tournament.printStanding());
			for (final BestOfMatchSinglePlayer match : tournament.getBestOfMatchForRound(tournament.getCurrentRound())) {
				if (Math.random() > 0.5) {
					match.getSet(0).submitPlayerResult(admin, match.getSideA());
				} else {
					match.getSet(0).submitPlayerResult(admin, match.getSideB());
				}
			}
			System.out.println(tournament.printMatchesForRound(i));
		}
	}

	private void playCurrentRound(final TrisTournament tournament) {
		for (final BestOfMatchSinglePlayer match : tournament.getBestOfMatchForRound(tournament.getCurrentRound())) {
			for (int i = 0; i < match.getNumberOfSets(); i++) {
				if (playerList.indexOf(match.getSideA()) > playerList.indexOf(match.getSideB())) {
					match.getSet(i).submitPlayerResult(admin, match.getSideA());
				} else {
					match.getSet(i).submitPlayerResult(admin, match.getSideB());
				}
			}
		}
	}
}
