package torganizer.exceptions;

public class NotYetImplementedException extends RuntimeException {
	private static final long serialVersionUID = 3413023676540092682L;

	public NotYetImplementedException(final String string) {
		super(string);
	}

	public NotYetImplementedException() {
		super();
	}
}
