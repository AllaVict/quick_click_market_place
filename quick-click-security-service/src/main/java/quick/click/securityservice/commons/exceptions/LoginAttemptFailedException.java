package quick.click.securityservice.commons.exceptions;

import org.springframework.security.core.AuthenticationException;

public class LoginAttemptFailedException extends AuthenticationException {

    public LoginAttemptFailedException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

    public LoginAttemptFailedException(final String msg) {
        super(msg);
    }
}
