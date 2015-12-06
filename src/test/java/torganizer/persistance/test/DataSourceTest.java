package torganizer.persistance.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import torganizer.core.entities.Player;
import torganizer.core.matches.Game;
import torganizer.core.persistance.objectservice.GlobalObjectService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-module.xml" })
public class DataSourceTest {
	@Autowired
	private GlobalObjectService globalObjectService;
	private Player admin;
	private final static String adminName = "admin";

	@Before
	public void initDb() {
		if (globalObjectService.getPlayerByName(adminName) == null) {
			admin = new Player(adminName);
			admin.setAdmin(true);
			globalObjectService.addPlayer(admin);
		} else {
			admin = globalObjectService.getPlayerByName(adminName);
		}
	}

	@Test
	public void playerPersistanceTest() {
		final String playerName = "player" + Math.random();
		Player p = new Player(playerName);
		p.setAdmin(true);
		final Player bBack = p;

		globalObjectService.addPlayer(p);
		final UUID playerId = p.getUid();
		p = null;

		p = globalObjectService.getPlayerByName(playerName);
		assertEquals(playerName, p.getName());
		assertEquals(playerId, p.getUid());
		assertTrue(p.isAdmin());
		assertEquals(bBack, p);

		p = globalObjectService.getPlayerById(playerId);
		assertEquals(playerName, p.getName());
		assertEquals(playerId, p.getUid());
		assertTrue(p.isAdmin());
		assertEquals(bBack, p);

		// for (final Player player : globalObjectService.getAllPlayers()) {
		// System.out.println(player);
		// }
	}

	@Test
	public void gamePersistanceTest() {
		final Player p1 = new Player("player1");
		final Player p2 = new Player("player2");

		Game game = new Game(p1.getUid(), p2.getUid(), "");
		game.submitResultAdmin(p1.getUid());
		globalObjectService.addGame(game);
		final UUID gameId = game.getUid();
		game = null;

		game = globalObjectService.getMatchById(gameId);
		assertEquals(p1.getUid(), game.getSideA());
		assertEquals(p2.getUid(), game.getSideB());
		assertEquals(p1.getUid(), game.getWinner());
		assertEquals(p1.getUid(), game.getAdminSubmittedWinner());
	}
}
