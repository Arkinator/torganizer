package torganizer.core.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import torganizer.core.entities.Player;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.tournaments.BasicRoundBasedTournament;
import torganizer.core.tournaments.KoTournament;

public class KoTournamentTest {
	static Player[] playerArray;
	private final static int maxNumberPlayers = 8;
	static List<Player> playerListFourPlayers;
	static List<Player> playerListEightPlayers;
	private static Player admin;

	@BeforeClass
	public static void initializeClass() {
		admin = new Player("admin");
		admin.setAdmin(true);
		playerArray = new Player[maxNumberPlayers];
		playerListFourPlayers = new ArrayList<Player>();
		playerListEightPlayers = new ArrayList<Player>();
		for (int i = 0; i < playerArray.length; i++) {
			playerArray[i] = new Player("player" + i);
			if (i < 4) {
				playerListFourPlayers.add(playerArray[i]);
			}
			if (i < 8) {
				playerListEightPlayers.add(playerArray[i]);
			}
		}
	}

	@Test
	public void construct4PlayerKoTournament() {
		final BasicRoundBasedTournament tournament = new KoTournament(1, playerListFourPlayers);
		assertNull(tournament.getWinner());
	}

	@Test
	public void fourPlayerTourney_Creation() {
		final KoTournament tournament = new KoTournament(1, playerListFourPlayers);
		assertEquals(0, tournament.getCurrentRound());
		assertEquals(2, tournament.getNumberOfRounds());

		assertThat(tournament.getMatchesForRound(0)).hasSize(2);
		assertThat(tournament.getPlayersForRound(0)).containsAll(playerListFourPlayers);

		assertEquals(1, tournament.getMatchesForRound(1).size());
	}

	@Test
	public void eightPlayerTourney_Creation() {
		final KoTournament tournament = new KoTournament(1, playerListEightPlayers);
		assertEquals(3, tournament.getNumberOfRounds());
		assertThat(tournament.getMatchesForRound(0)).hasSize(4);
		assertThat(tournament.getPlayersForRound(0)).containsAll(playerListEightPlayers);

		assertEquals(2, tournament.getMatchesForRound(1).size());
		assertEquals(1, tournament.getMatchesForRound(2).size());
	}

	@Test
	public void fourPlayerTourney_Semifinal() {
		// construct tourney
		final KoTournament tournament = new KoTournament(1, playerListFourPlayers);
		assertEquals(0, tournament.getCurrentRound());
		// play semifinal
		for (final BestOfMatchSinglePlayer match : tournament.getAbstractMatchesForRound(0)) {
			match.getSet(0).submitPlayerResult(admin, match.getSideA());
		}
		assertEquals(1, tournament.getCurrentRound());
		assertEquals(2, tournament.getPlayersForRound(1).size());
		assertThat(tournament.getPlayersForRound(1)).isSubsetOf(playerListEightPlayers);
	}

	@Test
	public void fourPlayerTourney_FinalWinner() {
		// construct tourney
		final KoTournament tournament = new KoTournament(1, playerListFourPlayers);
		assertEquals(0, tournament.getCurrentRound());
		assertFalse(playerListEightPlayers.contains(tournament.getWinner()));
		// play semifinal
		for (final BestOfMatchSinglePlayer match : tournament.getAbstractMatchesForRound(0)) {
			match.getSet(0).submitPlayerResult(admin, match.getSideA());
		}
		// play final
		for (final BestOfMatchSinglePlayer match : tournament.getAbstractMatchesForRound(1)) {
			match.getSet(0).submitPlayerResult(admin, match.getSideA());
			assertEquals(match.getSideA(), match.getSet(0).getWinner());
		}
		assertEquals(2, tournament.getCurrentRound());
		assertTrue(playerListEightPlayers.contains(tournament.getWinner()));
	}

	@Test
	public void testNextRoundNumberCalculations_EightPlayers() {
		final KoTournament tournament = new KoTournament(1, playerListEightPlayers);
		assertEquals(4, tournament.calculateAdvancingMatchNumber(0));
		assertEquals(4, tournament.calculateAdvancingMatchNumber(1));
		assertEquals(5, tournament.calculateAdvancingMatchNumber(2));
		assertEquals(5, tournament.calculateAdvancingMatchNumber(3));
		assertEquals(6, tournament.calculateAdvancingMatchNumber(4));
		assertEquals(6, tournament.calculateAdvancingMatchNumber(5));
	}

	@Test
	public void testNextRoundNumberCalculations_FourPlayers() {
		final KoTournament tournament = new KoTournament(1, playerListFourPlayers);
		assertEquals(2, tournament.calculateAdvancingMatchNumber(0));
		assertEquals(2, tournament.calculateAdvancingMatchNumber(1));
	}
}
