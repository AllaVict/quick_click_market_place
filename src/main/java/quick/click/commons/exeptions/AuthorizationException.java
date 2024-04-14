package quick.click.commons.exeptions;

public class AuthorizationException extends QCApplicationException {
    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}

