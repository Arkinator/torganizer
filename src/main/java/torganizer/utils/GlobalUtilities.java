package torganizer.utils;

import org.apache.commons.lang3.RandomStringUtils;

import torganizer.core.entities.AbstractToEntity;

public class GlobalUtilities {
	public static String createNewSetName(final AbstractToEntity entity) {
		return entity.getName() + "_set_" + RandomStringUtils.randomAlphanumeric(5);
	}
}
