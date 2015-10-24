package torganizer.core.matches;

import java.time.LocalDateTime;

public class TimeSlot {
	private LocalDateTime startTime;
	private LocalDateTime endTime;

	public TimeSlot(final LocalDateTime startTime, final LocalDateTime endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(final LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(final LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public boolean isPointInSlot(final LocalDateTime query) {
		if (startTime.isBefore(query) && endTime.isAfter(query)) {
			return true;
		}
		if (query.equals(startTime) || query.equals(endTime)) {
			return true;
		}
		return false;
	}

	public LocalDateTime getTimeMatchingWithSlot(final TimeSlot other) {
		if (other.startTime.isAfter(endTime)) {
			return null;
		} else if (other.endTime.isBefore(startTime)) {
			return null;
		} else if (other.startTime.isBefore(startTime)) {
			return startTime;
		} else {
			return other.startTime;
		}
	}

	@Override
	public String toString() {
		return "[from " + startTime + " till " + endTime + "]";
	}
}
