package torganizer.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import torganizer.exceptions.EnvironmentNotProperlyInitializedException;

public class CryptoUtils {

	public static byte[] getSha256Hash(final String password) {
		try {
			final MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes("UTF-8"));
			return md.digest();
		} catch (final NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new EnvironmentNotProperlyInitializedException(e);
		}
	}
}
