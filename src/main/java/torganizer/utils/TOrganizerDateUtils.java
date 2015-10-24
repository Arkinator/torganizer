package torganizer.utils;

import java.time.LocalDateTime;

import torganizer.core.matches.TimeSlot;

public class TOrganizerDateUtils {
	private static int errorMarginInMinutes = 14;

	public static LocalDateTime now() {
		return LocalDateTime.now();
	}

	public static LocalDateTime inNumberOfDays(final int numberOfDays) {
		return now().plusDays(numberOfDays);
	}

	public static boolean approximatelyEqual(final LocalDateTime timeA, final LocalDateTime timeB) {
		return new TimeSlot(timeA.minusMinutes(errorMarginInMinutes), timeA.plusMinutes(errorMarginInMinutes)).isPointInSlot(timeB);
	}

}
