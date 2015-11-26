package torganizer.core.test;

import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import torganizer.core.entities.Player;
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
		final TrisTournament tournament = new TrisTournament(1, playerList);
		assertNull(tournament.getWinner());
	}
}
