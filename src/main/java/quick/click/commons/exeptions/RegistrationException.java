package quick.click.commons.exeptions;

public class RegistrationException extends QCApplicationException {
    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}

