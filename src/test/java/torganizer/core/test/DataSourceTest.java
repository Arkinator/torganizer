package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import torganizer.core.entities.Player;
import torganizer.core.persistance.DataSource;

public class DataSourceTest {
	@Test
	public void playerPersistanceTest() {
		final String playerName = "jsdklpiorevmre";
		Player p = new Player(playerName);
		final long playerId = p.getUid();

		DataSource.persistEntity(p);
		p = null;
		assertNull(p);

		p = (Player) DataSource.getEntity(playerId);
		assertEquals(playerName, p.getName());
	}
}
