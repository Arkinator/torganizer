package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import torganizer.core.entities.Player;

public class PlayerTest {
	@Test
	public void testNewPlayerCreation() {
		final Player newPlayer = new Player(null);
	}

	@Test
	public void testPlayerName() {
		final String playerName = "Barcode";
		final Player newPlayer = new Player(playerName);
		assertEquals(playerName, newPlayer.getName());
	}

	@Test
	public void createPlayerPromotoToAdmin() {
		final String playerName = "Barcode";
		final Player newPlayer = new Player(playerName);
		assertFalse(newPlayer.isAdmin());
		newPlayer.setAdmin(true);
		assertTrue(newPlayer.isAdmin());
	}
}
