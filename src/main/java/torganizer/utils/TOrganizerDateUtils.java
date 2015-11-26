package torganizer.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import torganizer.core.matches.TimeSlot;

public class TOrganizerDateUtils {
	private static int errorMarginInMinutes = 1;

	public static LocalDateTime now() {
		return LocalDateTime.now();
	}

	public static LocalDateTime inNumberOfDays(final int numberOfDays) {
		return now().plusDays(numberOfDays);
	}

	public static boolean approximatelyEqual(final LocalDateTime timeA, final LocalDateTime timeB) {
		return new TimeSlot(timeA.minusMinutes(errorMarginInMinutes), timeA.plusMinutes(errorMarginInMinutes)).isPointInSlot(timeB);
	}

	public static boolean approximatelyEqual(final OffsetDateTime time, final OffsetDateTime query) {
		final OffsetDateTime startTime = time.minusMinutes(errorMarginInMinutes);
		final OffsetDateTime endTime = time.plusMinutes(errorMarginInMinutes);
		if (startTime.isBefore(query) && endTime.isAfter(query)) {
			return true;
		}
		if (query.equals(startTime) || query.equals(endTime)) {
			return true;
		}
		return false;
	}
}
