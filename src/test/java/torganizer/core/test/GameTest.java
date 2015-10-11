package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.Player;
import torganizer.core.matches.Game;
import torganizer.core.matches.Game.InvalidPlayTimeException;
import torganizer.utils.TOrganizerDateUtils;

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
		assertEquals(playerA, game.getWinner());
		game.submitPlayerResult(playerA, playerB);
		assertEquals(playerA, game.getWinner());
		game.submitPlayerResult(playerB, playerA);
		assertEquals(playerA, game.getWinner());
	}

	@Test
	public void adminAdjudicatesGame_BothPlayersVoteDifferent_DifferentOrder() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final Game game = new Game(playerA, playerB);

		game.submitPlayerResult(playerA, playerB);
		assertEquals(null, game.getWinner());
		game.submitPlayerResult(playerB, playerA);
		assertEquals(null, game.getWinner());
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

		game.submitPlayerResult(playerA, playerB);

		verify(mockTarget, times(0)).callback(game);

		game.submitPlayerResult(admin, playerA);

		verify(mockTarget, times(1)).callback(game);
	}

	@Test
	public void testGamesEquals() {
		final Game game1 = new Game(playerA, playerB);
		final Game game2 = new Game(playerA, playerB);
		assertFalse(game1.equals(game2));
		assertFalse(game2.equals(game1));
		assertTrue(game1.equals(game1));
		assertTrue(game2.equals(game2));
	}

	@Test
	public void testGamesHashCode() {
		final Game game1 = new Game(playerA, playerB);
		final Game game2 = new Game(playerA, playerB);
		assertTrue(game1.hashCode() != game2.hashCode());
		assertTrue(game2.hashCode() != game1.hashCode());
		assertTrue(game2.hashCode() == game2.hashCode());
		assertTrue(game1.hashCode() == game1.hashCode());
	}

	@Test
	public void verifyGameStartAndEndTime() {
		final Game game = new Game(playerA, playerB);
		game.setLatestTime(TOrganizerDateUtils.inNumberOfDays(7));
		game.setEarliestTime(TOrganizerDateUtils.now());
		assertTrue(game.getEarliestTime().isBefore(game.getLatestTime()));
	}

	@Test(expected = InvalidPlayTimeException.class)
	public void tryInvalidGameTimeAppointment() {
		final Game game = new Game(playerA, playerB);
		game.setLatestTime(TOrganizerDateUtils.inNumberOfDays(7));
		game.setEarliestTime(TOrganizerDateUtils.now());
		game.setPlayTime(TOrganizerDateUtils.inNumberOfDays(8));
	}

	@Test
	public void validGameTimeAppointment() {
		final Game game = new Game(playerA, playerB);
		game.setLatestTime(TOrganizerDateUtils.inNumberOfDays(7));
		game.setEarliestTime(TOrganizerDateUtils.now());
		game.setPlayTime(TOrganizerDateUtils.inNumberOfDays(2));
	}
}
