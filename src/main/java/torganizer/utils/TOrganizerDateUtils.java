package torganizer.utils;

import java.time.LocalDateTime;

public class TOrganizerDateUtils {

	public static LocalDateTime now() {
		return LocalDateTime.now();
	}

	public static LocalDateTime inNumberOfDays(final int numberOfDays) {
		return now().plusDays(numberOfDays);
	}

}
