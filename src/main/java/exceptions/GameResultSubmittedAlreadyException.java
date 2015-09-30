package exceptions;

public class GameResultSubmittedAlreadyException extends RuntimeException {
	private static final long serialVersionUID = 2795439708511522527L;

	public GameResultSubmittedAlreadyException(final String string) {
		super(string);
	}
}