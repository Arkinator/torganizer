package torganizer.persistance.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import torganizer.core.entities.Player;
import torganizer.core.persistance.objectservice.PlayerObjectService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-module.xml" })
public class DataSourceTest {
	@Autowired
	private PlayerObjectService playerObjectService;

	@Test
	public void playerPersistanceTest() {
		final String playerName = "jsdklpiorevmre";
		Player p = new Player(playerName);

		playerObjectService.addPlayer(p);
		final UUID playerId = p.getUid();
		p = null;
		assertNull(p);

		p = playerObjectService.getAllPlayers().get(0);
		assertEquals(playerName, p.getName());
		assertEquals(playerId, p.getUid());
	}
}