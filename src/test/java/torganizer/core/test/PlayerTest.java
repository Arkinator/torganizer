package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.Test;

import torganizer.core.entities.Player;
import torganizer.utils.TOrganizerDateUtils;

public class PlayerTest {
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

	@Test
	public void testPlayerTimezoneConversion() {
		final Player playerA = new Player("kfldsö");
		final Player playerB = new Player("fgrmaplvrsd");
		playerA.setTimezoneOffset(-2);
		playerB.setTimezoneOffset(2);
		final LocalDateTime time = TOrganizerDateUtils.now();
		final OffsetDateTime playerATime = time.atOffset(ZoneOffset.ofHours(playerA.getTimezoneOffset()));
		final OffsetDateTime playerBTime = time.atOffset(ZoneOffset.ofHours(playerB.getTimezoneOffset()));
		assertFalse(playerATime.isEqual(playerBTime));
		assertTrue(TOrganizerDateUtils.approximatelyEqual(playerATime, playerBTime.plusHours(4)));
	}
}
