package torganizer.utils;

import org.apache.commons.lang3.RandomStringUtils;

import torganizer.core.entities.AbstractToEntity;

public class GlobalUtilities {
	public static String createNewSetName(final AbstractToEntity entity) {
		return entity.getName() + "_set_" + RandomStringUtils.randomAlphanumeric(5);
	}

	public static String formatNanoTimeDiff(final long t1, final long t2) {
		final double millis = (t2 - t1) / (1_000_000.);
		final int m = (int) (millis * 100);
		return (m / 100.) + "ms";
	}
}
