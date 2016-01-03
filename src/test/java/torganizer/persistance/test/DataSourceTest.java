package torganizer.persistance.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import torganizer.core.entities.Player;
import torganizer.core.entities.Team;
import torganizer.core.matches.BestOfMatchSinglePlayer;
import torganizer.core.matches.Game;
import torganizer.core.persistance.objectservice.GlobalObjectService;
import torganizer.core.tournaments.TrisTournament;

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

		game = globalObjectService.getGameById(gameId);
		assertEquals(p1.getUid(), game.getSideA());
		assertEquals(p2.getUid(), game.getSideB());
		assertEquals(p1.getUid(), game.getWinner());
		assertEquals(p1.getUid(), game.getAdminSubmittedWinner());
	}

	@Test
	public void teamPersistanceTest() {
		Team t = new Team("testTeam");
		t.setLiquipediaFlagCode("flagCode");
		t.setShortName("shortName");
		final UUID teamId = t.getUid();
		globalObjectService.addTeam(t);

		t = globalObjectService.getTeamById(teamId);
		assertEquals("testTeam", t.getName());
		assertEquals("flagCode", t.getLiquipediaFlagCode());
		assertEquals("shortName", t.getShortName());
	}

	@Test
	public void singlePlayerMatchSeriesPersistanceTest() {
		final Player p1 = new Player("player1");
		final Player p2 = new Player("player2");

		// create new match
		BestOfMatchSinglePlayer match = new BestOfMatchSinglePlayer(3, p1, p2, RandomStringUtils.randomAlphabetic(20));
		globalObjectService.addMatch(match);
		final UUID matchId = match.getUid();
		match = null;

		// update match in db
		match = globalObjectService.getBestOfMatchById(matchId);
		match.getSet(0).submitResultAdmin(p1.getUid());
		match.getSet(1).submitResultAdmin(p2.getUid());
		match.getSet(2).submitResultAdmin(p1.getUid());
		assertEquals(p1.getUid(), match.getWinner());
		globalObjectService.updateEntity(match);

		// check
		match = globalObjectService.getBestOfMatchById(matchId);
		assertEquals(p1.getUid(), match.getSideA());
		assertEquals(p2.getUid(), match.getSideB());
		assertEquals(p1.getUid(), match.getWinner());
		assertEquals(3, match.getNumberOfSets());
		assertEquals(p1.getUid(), match.getSet(0).getWinner());
		assertEquals(p2.getUid(), match.getSet(1).getWinner());
		assertEquals(p1.getUid(), match.getSet(2).getWinner());
	}

	@Test
	public void tournamentPersistanceTest() {
		final String tournamentName = "mySuperTourney";
		final List<Player> pList = collectPlayerList();
		TrisTournament tourney = new TrisTournament(2, 3, pList, tournamentName);

		tourney.getMatchesForRound(0).get(0).submitResultAdmin(pList.get(0).getUid(), 2, 1);

		globalObjectService.addEntity(tourney);
		final UUID tournamentId = tourney.getUid();
		tourney = globalObjectService.getTournamentById(tournamentId);
		assertEquals(tournamentName, tourney.getName());

		tourney.getMatchesForRound(0).get(1).submitResultAdmin(pList.get(2).getUid(), 2, 0);

		globalObjectService.updateEntity(tourney);
		tourney = globalObjectService.getTournamentById(tournamentId);
		assertEquals(pList.get(0).getUid(), tourney.getMatchesForRound(0).get(0).getWinner());
		assertEquals(2, ((BestOfMatchSinglePlayer) tourney.getMatchesForRound(0).get(0)).getScoreSideA());
		assertEquals(0, ((BestOfMatchSinglePlayer) tourney.getMatchesForRound(0).get(0)).getScoreSideB());
		assertEquals(pList.get(2).getUid(), tourney.getMatchesForRound(0).get(1).getWinner());
		assertEquals(2, ((BestOfMatchSinglePlayer) tourney.getMatchesForRound(0).get(1)).getScoreSideA());
		assertEquals(1, ((BestOfMatchSinglePlayer) tourney.getMatchesForRound(0).get(1)).getScoreSideB());
	}

	private List<Player> collectPlayerList() {
		final List<Player> pList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			final Player p = new Player("player" + i);
			pList.add(p);
			globalObjectService.addEntity(p);
		}
		return pList;
	}
}
