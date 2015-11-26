package torganizer.core.matches;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import torganizer.core.entities.IToEntity;
import torganizer.core.entities.IToParticipant;

public abstract class AbstractMatch<TYPE extends IToParticipant> extends GenericMatch<TYPE> {
	private TYPE sideA;
	private TYPE sideB;
	private List<TimeSlot> timeSlotsSideA;
	private List<TimeSlot> timeSlotsSideB;
	private TYPE winner = null;
	private LocalDateTime earliestTime;
	private LocalDateTime latestTime;
	private LocalDateTime playTime;

	public AbstractMatch(final TYPE sideA, final TYPE sideB) {
		this.sideA = sideA;
		this.sideB = sideB;
		this.timeSlotsSideA = new ArrayList<TimeSlot>();
		this.timeSlotsSideB = new ArrayList<TimeSlot>();
	}

	public AbstractMatch() {
	}

	@Override
	public TYPE getWinner() {
		return winner;
	}

	@Override
	public void setSideA(final TYPE sideA) {
		this.sideA = sideA;
	}

	@Override
	public TYPE getSideA() {
		return sideA;
	}

	@Override
	public void setSideB(final TYPE sideB) {
		this.sideB = sideB;
	}

	@Override
	public TYPE getSideB() {
		return sideB;
	}

	@Override
	public String toString() {
		return sideA + " vs " + sideB;
	}

	@Override
	public void callback(final IToEntity sender) {
		refresh();
	}

	public void refresh() {
		final TYPE newWinner = calculateWinner();
		if (newWinner != winner) {
			winner = newWinner;
			fireCallback();
		}
	}

	public abstract TYPE calculateWinner();

	public void submitTimeSlot(final TYPE side, final LocalDateTime startTime, final LocalDateTime endTime) {
		final TimeSlot timeSlot = new TimeSlot(startTime, endTime);
		if (side.equals(sideA)) {
			timeSlotsSideA.add(timeSlot);
			checkPlayTimeMatchingSlots(timeSlotsSideB, timeSlot);
		} else if (side.equals(sideB)) {
			timeSlotsSideB.add(timeSlot);
			checkPlayTimeMatchingSlots(timeSlotsSideA, timeSlot);
		} else {
			throw new UnrecognizedParticipantException("Couldnt submit timeslot for participant " + side);
		}
	}

	private void checkPlayTimeMatchingSlots(final List<TimeSlot> timeSlots, final TimeSlot newTimeSlot) {
		for (final TimeSlot timeSlot : timeSlots) {
			if (playTime != null) {
				return;
			}
			playTime = newTimeSlot.getTimeMatchingWithSlot(timeSlot);
		}
	}

	public void submitPlayTimeProposition(final TYPE submittingSide, final LocalDateTime proposition) {
		if (submittingSide.equals(sideA)) {
			checkPlayTimeProposition(timeSlotsSideB, proposition);
		} else if (submittingSide.equals(sideB)) {
			checkPlayTimeProposition(timeSlotsSideA, proposition);
		}
		// TODO if any player submits a time proposition, even if a time has
		// already been calculated, the match-time should be shifted (?)
		// Check for excessive shifting!
	}

	private void checkPlayTimeProposition(final List<TimeSlot> timeSlots, final LocalDateTime proposition) {
		for (final TimeSlot timeSlot : timeSlots) {
			if (timeSlot.isPointInSlot(proposition)) {
				this.playTime = proposition;
			}
		}
	}

	public LocalDateTime getPlayTime() {
		return playTime;
	}

	public void setEarliestTime(final LocalDateTime time) {
		this.earliestTime = time;
	}

	public void setLatestTime(final LocalDateTime time) {
		this.latestTime = time;
	}

	public LocalDateTime getEarliestTime() {
		return earliestTime;
	}

	public LocalDateTime getLatestTime() {
		return latestTime;
	}

	public void setPlayTime(final LocalDateTime playTime) {
		if (playTime.isBefore(earliestTime)) {
			throw new InvalidPlayTimeException("The time " + playTime + " is before the earliest acceptable time of " + earliestTime);
		}
		if (playTime.isAfter(latestTime)) {
			throw new InvalidPlayTimeException("The time " + playTime + " is after the latest acceptable time of " + latestTime);
		}
		this.playTime = playTime;
	}

	public static class InvalidPlayTimeException extends RuntimeException {
		private static final long serialVersionUID = 7458638426213805978L;

		public InvalidPlayTimeException(final String string) {
			super(string);
		}
	}

	public static class UnrecognizedParticipantException extends RuntimeException {
		private static final long serialVersionUID = -276765681024484437L;

		public UnrecognizedParticipantException(final String string) {
			super(string);
		}
	}

	public boolean isBye() {
		return (getSideA() == null) || (getSideB() == null);
	}
}
