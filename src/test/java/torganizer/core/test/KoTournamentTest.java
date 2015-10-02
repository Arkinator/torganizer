package torganizer.core.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import torganizer.core.entities.Player;
import torganizer.core.tournaments.KoTournament;

public class KoTournamentTest {
	static Player[] playerArray;
	private final static int maxNumberPlayers = 8;
	static List<Player> playerListFourPlayers;
	static List<Player> playerListEightPlayers;

	@BeforeClass
	public static void initializeClass() {
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
		final KoTournament tournament = new KoTournament(1, playerListFourPlayers);
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
}
