package torganizer.exceptions;

public class EnvironmentNotProperlyInitializedException extends RuntimeException {
	private static final long serialVersionUID = -2812269830782460637L;

	public EnvironmentNotProperlyInitializedException(final Exception e) {
		super(e);
	}

}
