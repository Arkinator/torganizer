package torganizer.persistance.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import torganizer.core.entities.Player;
import torganizer.core.matches.Game;
import torganizer.core.persistance.objectservice.MatchObjectService;
import torganizer.core.persistance.objectservice.PlayerObjectService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-module.xml" })
public class DataSourceTest {
	@Autowired
	private PlayerObjectService playerObjectService;
	@Autowired
	private MatchObjectService matchObjectService;

	@Test
	public void playerPersistanceTest() {
		final String playerName = "jsdklpiorevmre";
		Player p = new Player(playerName);
		p.setAdmin(true);
		final Player bBack = p;

		playerObjectService.addPlayer(p);
		final UUID playerId = p.getUid();
		p = null;

		p = playerObjectService.getAllPlayers().get(0);
		assertEquals(playerName, p.getName());
		assertEquals(playerId, p.getUid());
		assertTrue(p.isAdmin());
		assertEquals(bBack, p);

		p = playerObjectService.getPlayerByName(playerName);
		assertEquals(playerName, p.getName());
		assertEquals(playerId, p.getUid());
		assertTrue(p.isAdmin());

		p = playerObjectService.getPlayerById(playerId);
		assertEquals(playerName, p.getName());
		assertEquals(playerId, p.getUid());
		assertTrue(p.isAdmin());
	}

	@Test
	public void gamePersistanceTest() {
		final Player p1 = new Player("player1");
		final Player p2 = new Player("player2");
		playerObjectService.addPlayer(p1);
		playerObjectService.addPlayer(p2);

		Game game = new Game(p1, p2);
		matchObjectService.addGame(game);
		final UUID gameId = game.getUid();
		game = null;

		game = matchObjectService.getGameById(gameId);
		assertEquals(p1, game.getSideA());
		assertEquals(p2, game.getSideB());
	}
}
