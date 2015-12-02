package torganizer.exceptions;

public class IncompatibleTypeException extends RuntimeException {
	private static final long serialVersionUID = 9193238995765925362L;

	public IncompatibleTypeException(final String string) {
		super(string);
	}
}
