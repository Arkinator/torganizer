package torganizer.core.matches;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import torganizer.core.entities.IToEntity;
import torganizer.core.persistance.orm.MatchOrm;

public abstract class AbstractMatch extends GenericMatch {
	private UUID sideA;
	private UUID sideB;
	private List<TimeSlot> timeSlotsSideA;
	private List<TimeSlot> timeSlotsSideB;
	private UUID winner = null;
	private LocalDateTime earliestTime;
	private LocalDateTime latestTime;
	private LocalDateTime playTime;

	public AbstractMatch(final UUID sideA, final UUID sideB, final String name) {
		super(name);
		constructMatchOrm();
		setSideA(sideA);
		setSideB(sideB);
		this.timeSlotsSideA = new ArrayList<TimeSlot>();
		this.timeSlotsSideB = new ArrayList<TimeSlot>();
	}

	public AbstractMatch(final String name) {
		super(name);
		this.timeSlotsSideA = new ArrayList<TimeSlot>();
		this.timeSlotsSideB = new ArrayList<TimeSlot>();
		constructMatchOrm();
	}

	public AbstractMatch(final MatchOrm orm) {
		super(orm.getEntity());
		this.sideA = orm.getSideAId();
		this.sideB = orm.getSideBId();
		this.winner = orm.getWinner();
	}

	protected void constructMatchOrm() {
		final MatchOrm matchOrm = new MatchOrm();
		getEntityOrm().setMatch(matchOrm);
		matchOrm.setEntity(getEntityOrm());
	}

	@Override
	public UUID getWinner() {
		return winner;
	}

	@Override
	public void setSideA(final UUID sideA) {
		this.sideA = sideA;
		getEntityOrm().getMatch().setSideAId(sideA);
	}

	@Override
	public UUID getSideA() {
		return sideA;
	}

	@Override
	public void setSideB(final UUID sideB) {
		this.sideB = sideB;
		getEntityOrm().getMatch().setSideBId(sideB);
	}

	@Override
	public UUID getSideB() {
		return sideB;
	}

	@Override
	public String toString() {
		return sideA + " vs " + sideB + "(" + getName() + ")";
	}

	@Override
	public void callback(final IToEntity sender) {
		refresh();
	}

	public void refresh() {
		final UUID newWinner = calculateWinner();
		if (newWinner != winner) {
			winner = newWinner;
			getEntityOrm().getMatch().setWinner(winner);
			fireCallback();
		}
	}

	public abstract UUID calculateWinner();

	public void submitTimeSlot(final UUID side, final LocalDateTime startTime, final LocalDateTime endTime) {
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

	public void submitPlayTimeProposition(final UUID submittingSide, final LocalDateTime proposition) {
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
