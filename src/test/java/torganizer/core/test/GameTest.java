package torganizer.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.Player;
import torganizer.core.matches.AbstractMatch;
import torganizer.core.matches.Game;
import torganizer.core.matches.GenericMatch;
import torganizer.core.matches.TimeSlot;
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
		final Game game = new Game(playerA.getUid(), playerB.getUid(), "");
		assertNull(game.getWinner());
		assertFalse(game.isPlayed());
	}

	@Test
	public void onlyOnePlayerSubmitsResult() {
		final Game game = new Game(playerA.getUid(), playerB.getUid(), "");
		assertNull(game.getWinner());

		game.submitResultSideA(playerA.getUid());

		assertEquals(playerA.getUid(), game.getWinner());
	}

	@Test
	public void bothPlayersSubmitSameResult() {
		final Game game = new Game(playerA.getUid(), playerB.getUid(), "");
		game.submitResultSideA(playerA.getUid());
		game.submitResultSideB(playerA.getUid());

		assertEquals(playerA.getUid(), game.getWinner());
	}

	@Test
	public void playerSubmitDifferingResults() {
		final Game game = new Game(playerA.getUid(), playerB.getUid(), "");
		game.submitResultSideA(playerA.getUid());
		game.submitResultSideB(playerB.getUid());

		assertNull(game.getWinner());
		assertFalse(game.isPlayed());
	}

	@Test
	public void adminAdjudicatesEmptyGame() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final Game game = new Game(playerA.getUid(), playerB.getUid(), "");

		game.submitResultAdmin(playerA.getUid());
		assertEquals(playerA.getUid(), game.getWinner());
	}

	@Test
	public void adminAdjudicatesEmptyGame_testCorrectInheritance() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final GenericMatch game = new Game(playerA.getUid(), playerB.getUid(), "");

		game.submitResultAdmin(playerA.getUid(), 1, 0);
		assertEquals(playerA.getUid(), game.getWinner());
	}

	@Test
	public void adminAdjudicatesGame_BothPlayersVoteDifferent() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final Game game = new Game(playerA.getUid(), playerB.getUid(), "");

		game.submitResultAdmin(playerA.getUid());
		assertEquals(playerA.getUid(), game.getWinner());
		game.submitResultSideB(playerB.getUid());
		assertEquals(playerA.getUid(), game.getWinner());
		game.submitResultSideA(playerA.getUid());
		assertEquals(playerA.getUid(), game.getWinner());
	}

	@Test
	public void adminAdjudicatesGame_BothPlayersVoteDifferent_DifferentOrder() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);

		final Game game = new Game(playerA.getUid(), playerB.getUid(), "");

		game.submitResultSideA(playerB.getUid());
		assertEquals(playerB.getUid(), game.getWinner());
		game.submitResultSideB(playerA.getUid());
		assertEquals(null, game.getWinner());
		game.submitResultAdmin(playerA.getUid());
		assertEquals(playerA.getUid(), game.getWinner());
	}

	@Test
	public void testGameCallback() {
		final Player admin = new Player("playerC");
		admin.setAdmin(true);
		final Game game = new Game(playerA.getUid(), playerB.getUid(), "");
		final IToEntity mockTarget = Mockito.mock(IToEntity.class);
		game.addCallbackObject(mockTarget);

		game.submitResultSideA(playerB.getUid());

		verify(mockTarget, times(1)).callback(game);

		game.submitResultAdmin(playerA.getUid());

		verify(mockTarget, times(2)).callback(game);
	}

	@Test
	public void testGamesEquals() {
		final Game game1 = new Game(playerA.getUid(), playerB.getUid(), "");
		final Game game2 = new Game(playerA.getUid(), playerB.getUid(), "");
		assertFalse(game1.equals(game2));
		assertFalse(game2.equals(game1));
		assertTrue(game1.equals(game1));
		assertTrue(game2.equals(game2));
	}

	@Test
	public void testGamesHashCode() {
		final Game game1 = new Game(playerA.getUid(), playerB.getUid(), "");
		final Game game2 = new Game(playerA.getUid(), playerB.getUid(), "");
		assertTrue(game1.hashCode() != game2.hashCode());
		assertTrue(game2.hashCode() != game1.hashCode());
		assertTrue(game2.hashCode() == game2.hashCode());
		assertTrue(game1.hashCode() == game1.hashCode());
	}

	@Test
	public void verifyGameStartAndEndTime() {
		final Game game = createGameWithOneWeekTimeSlot();
		assertTrue(game.getEarliestTime().isBefore(game.getLatestTime()));
	}

	@Test(expected = AbstractMatch.InvalidPlayTimeException.class)
	public void tryInvalidGameTimeAppointment() {
		final Game game = createGameWithOneWeekTimeSlot();
		game.setPlayTime(TOrganizerDateUtils.inNumberOfDays(8));
	}

	@Test
	public void validGameTimeAppointment() {
		final Game game = createGameWithOneWeekTimeSlot();
		game.setPlayTime(TOrganizerDateUtils.inNumberOfDays(2));
		assertTrue(TOrganizerDateUtils.approximatelyEqual(TOrganizerDateUtils.inNumberOfDays(2), game.getPlayTime()));
	}

	@Test
	public void queryTimeSlotsTest() {
		final TimeSlot timeSlot = new TimeSlot(TOrganizerDateUtils.inNumberOfDays(1), TOrganizerDateUtils.inNumberOfDays(3));
		assertTrue(timeSlot.isPointInSlot(TOrganizerDateUtils.inNumberOfDays(2)));
		assertFalse(timeSlot.isPointInSlot(TOrganizerDateUtils.inNumberOfDays(4)));
	}

	@Test
	public void matchTimeSlotsTest_GoodCase() {
		final TimeSlot timeSlotA = new TimeSlot(TOrganizerDateUtils.inNumberOfDays(0), TOrganizerDateUtils.inNumberOfDays(3));
		final TimeSlot timeSlotB = new TimeSlot(TOrganizerDateUtils.inNumberOfDays(2), TOrganizerDateUtils.inNumberOfDays(4));
		final LocalDateTime match = timeSlotA.getTimeMatchingWithSlot(timeSlotB);
		assertTrue(timeSlotA.isPointInSlot(match));
		assertTrue(timeSlotB.isPointInSlot(match));
		assertTrue(TOrganizerDateUtils.approximatelyEqual(TOrganizerDateUtils.inNumberOfDays(2), match));
	}

	@Test
	public void matchTimeSlotsTest_NoMatchingTimes() {
		final TimeSlot timeSlotA = new TimeSlot(TOrganizerDateUtils.inNumberOfDays(0), TOrganizerDateUtils.inNumberOfDays(1));
		final TimeSlot timeSlotB = new TimeSlot(TOrganizerDateUtils.inNumberOfDays(3), TOrganizerDateUtils.inNumberOfDays(4));
		final LocalDateTime match = timeSlotA.getTimeMatchingWithSlot(timeSlotB);
		assertNull(match);
	}

	@Test(expected = AbstractMatch.UnrecognizedParticipantException.class)
	public void submitTimeslotAsNonParticipant() {
		createGameWithOneWeekTimeSlot().submitTimeSlot(UUID.randomUUID(), null, null);
	}

	@Test
	public void setPlayTimeInAccordanceWithSetTimeslots() {
		final Game game = createGameWithOneWeekTimeSlot();
		game.submitTimeSlot(playerA.getUid(), TOrganizerDateUtils.inNumberOfDays(1), TOrganizerDateUtils.inNumberOfDays(3));
		game.submitPlayTimeProposition(playerB.getUid(), TOrganizerDateUtils.inNumberOfDays(2));
		assertTrue(TOrganizerDateUtils.approximatelyEqual(TOrganizerDateUtils.inNumberOfDays(2), game.getPlayTime()));
	}

	@Test
	public void setPlayTimeInAccordanceWithSetTimeslots_otherPlayer() {
		final Game game = createGameWithOneWeekTimeSlot();
		game.submitTimeSlot(playerB.getUid(), TOrganizerDateUtils.inNumberOfDays(1), TOrganizerDateUtils.inNumberOfDays(3));
		game.submitPlayTimeProposition(playerA.getUid(), TOrganizerDateUtils.inNumberOfDays(2));
		assertTrue(TOrganizerDateUtils.approximatelyEqual(TOrganizerDateUtils.inNumberOfDays(2), game.getPlayTime()));
	}

	@Test
	public void setPlayTimeViolatingTimeslots() {
		final Game game = createGameWithOneWeekTimeSlot();
		game.submitTimeSlot(playerA.getUid(), TOrganizerDateUtils.inNumberOfDays(1), TOrganizerDateUtils.inNumberOfDays(3));
		game.submitPlayTimeProposition(playerB.getUid(), TOrganizerDateUtils.inNumberOfDays(4));
		assertNull(game.getPlayTime());
	}

	@Test
	public void setMatchingTimeslotsAndCheckResultingPlayTime() {
		final Game game = createGameWithOneWeekTimeSlot();
		game.submitTimeSlot(playerA.getUid(), TOrganizerDateUtils.inNumberOfDays(1), TOrganizerDateUtils.inNumberOfDays(3));
		game.submitTimeSlot(playerB.getUid(), TOrganizerDateUtils.inNumberOfDays(2), TOrganizerDateUtils.inNumberOfDays(4));
		assertTrue(TOrganizerDateUtils.approximatelyEqual(TOrganizerDateUtils.inNumberOfDays(2), game.getPlayTime()));
	}

	private Game createGameWithOneWeekTimeSlot() {
		final Game game = new Game(playerA.getUid(), playerB.getUid(), "");
		game.setLatestTime(TOrganizerDateUtils.inNumberOfDays(7));
		game.setEarliestTime(TOrganizerDateUtils.now());
		return game;
	}

	@Test
	public void testByeSet_WithOnePlayerBeingNull() {
		final Game game = new Game(playerA.getUid(), null, "");
		assertEquals(playerA.getUid(), game.getWinner());
	}
}
