package quick.click.advertservice.commons.exceptions;

public abstract class QCApplicationException extends RuntimeException {

	public QCApplicationException(final String message) {
		super(message);
	}

	public QCApplicationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public QCApplicationException(final Throwable cause) {
		super(cause);
	}
}
