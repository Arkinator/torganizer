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
			admin = playerObjectService.getPlayerByName(adminName);
		}
	}

	@Test
	public void playerPersistanceTest() {
		final String playerName = "player" + Math.random();
		Player p = new Player(playerName);
		p.setAdmin(true);
		final Player bBack = p;

		playerObjectService.addPlayer(p);
		final UUID playerId = p.getUid();
		p = null;

		p = playerObjectService.getPlayerByName(playerName);
		assertEquals(playerName, p.getName());
		assertEquals(playerId, p.getUid());
		assertTrue(p.isAdmin());
		assertEquals(bBack, p);

		p = playerObjectService.getPlayerById(playerId);
		assertEquals(playerName, p.getName());
		assertEquals(playerId, p.getUid());
		assertTrue(p.isAdmin());
		assertEquals(bBack, p);

		for (final Player player : playerObjectService.getAllPlayers()) {
			System.out.println(player);
		}
	}

	@Test
	public void gamePersistanceTest() {
		final Player p1 = new Player("player1");
		final Player p2 = new Player("player2");
		playerObjectService.addPlayer(p1);
		playerObjectService.addPlayer(p2);

		Game game = new Game(p1, p2);
		game.submitPlayerResult(admin, p1);
		matchObjectService.addGame(game);
		final UUID gameId = game.getUid();
		game = null;

		game = (Game) matchObjectService.getMatchById(gameId);
		assertEquals(p1, game.getSideA());
		assertEquals(p2, game.getSideB());
		assertEquals(p1, game.getWinner());
		assertEquals(1, game.getSubmittedResults().size());
		assertEquals(admin, game.getSubmittedResults().get(0).getSubmitter());
		assertEquals(p1, game.getSubmittedResults().get(0).getWinner());
	}
}
