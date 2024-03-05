package quick.click.config.factory;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContext;
import quick.click.core.enums.LocaleType;
import quick.click.core.enums.Role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import quick.click.security.commons.model.AuthenticatedUser;
/**
 * Custom annotation that uses {@link WithSecurityContext} to create
 * {@link SecurityContext} already populated with {@link AuthenticatedUser}
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAuthenticatedUserSecurityContextFactory.class)
public @interface WithMockAuthenticatedUser {

    long userId() default 101L;

    String firstName() default "John";

    String lastName() default "Johnson";

    String password() default "password";

    String email() default "test@example.com";

    Role role() default Role.ROLE_ADMIN;

    LocaleType locale() default LocaleType.EN;
}
