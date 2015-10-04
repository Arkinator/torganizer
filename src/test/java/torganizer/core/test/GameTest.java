package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.Player;
import torganizer.core.matches.Game;

public class GameTest {
	private static Player playerA;
	private static Player playerB;

	@BeforeClass
	public static void intialize() {
		playerA = new Player("playerA");
		playerB = new Player("playerB");
	}

	@Test
	public void getResultForUnplayedGame() {
		final Game game = new Game(playerA, playerB);
		assertNull(game.getWinner());
	}

	@Test
	public void onlyOnePlayerSubmitsResult() {
		final Game game = new Game(playerA, playerB);
		assertEquals(0, game.getSubmittedResults().size());
		assertNull(game.getWinner());

		game.submitPlayerResult(playerA, playerA);

		assertEquals(1, game.getSubmittedResults().size());
		assertNull(game.getWinner());
	}

	@Test(expected = Game.SubmitterIsNotPlayerException.class)
	public void impartialPlayerSubmitsResult() {
		final Player playerC = new Player("playerC");

		final Game game = new Game(playerA, playerB);
		game.submitPlayerResult(playerC, playerA);
	}

	@Test
	public void bothPlayersSubmitSameResult() {
		final Game game = new Game(playerA, playerB);
		game.submitPlayerResult(playerA, playerA);
		game.submitPlayerResult(playerB, playerA);

		assertEquals(2, game.getSubmittedResults().size());
		assertEquals(playerA, game.getWinner());
	}

	@Test(expected = Game.GameResultSubmittedAlreadyException.class)
	public void onePlayerSubmitTwoResults() {
		final Game game = new Game(playerA, playerB);
		game.submitPlayerResult(playerA, playerA);
		game.submitPlayerResult(playerA, playerA);
	}

	@Test
	public void playerSubmitDifferingResults() {
		final Game game = new Game(playerA, playerB);
		game.submitPlayerResult(playerA, playerA);
		game.submitPlayerResult(playerB, playerB);

		assertNull(game.getWinner());
	}

	@Test
	public void adminAdjudicatesEmptyGame() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final Game game = new Game(playerA, playerB);

		game.submitPlayerResult(admin, playerA);
		assertEquals(playerA, game.getWinner());
	}

	@Test
	public void adminAdjudicatesGame_BothPlayersVoteDifferent() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final Game game = new Game(playerA, playerB);

		game.submitPlayerResult(admin, playerA);
		game.submitPlayerResult(playerA, playerB);
		game.submitPlayerResult(playerB, playerB);
		assertEquals(playerA, game.getWinner());
	}

	@Test
	public void adminAdjudicatesGame_BothPlayersVoteDifferent_DifferentOrder() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final Game game = new Game(playerA, playerB);

		game.submitPlayerResult(playerA, playerB);
		game.submitPlayerResult(playerB, playerB);
		game.submitPlayerResult(admin, playerA);
		assertEquals(playerA, game.getWinner());
	}

	@Test
	public void testGameCallback() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);
		final Game game = new Game(playerA, playerB);

		final IToEntity mockTarget = Mockito.mock(IToEntity.class);
		game.addCallbackObject(mockTarget);

		game.submitPlayerResult(admin, playerA);

		Mockito.verify(mockTarget).callback(null);
	}
}
