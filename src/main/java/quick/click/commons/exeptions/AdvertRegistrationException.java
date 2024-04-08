package quick.click.commons.exeptions;

public class AdvertRegistrationException extends QCApplicationException {
    public AdvertRegistrationException(String message) {
        super(message);
    }

    public AdvertRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}

