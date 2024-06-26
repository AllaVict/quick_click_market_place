package quick.click.commons.constants;

public final class Constants {

    private Constants() {
    }

    public static final class Endpoints {

        public static final String LOGIN_URL = "/login";

        public static final String LOGOUT_URL = "/logout";

        public static final String AUTH_URL = "/auth";

        public static final String SIGNUP_URL = "/signup";

        public static final String HOME_URL = "/home";

        public static final String ALL_URL = "/all";

        public static final String USERS_URL = "/users";

        public static final String ADVERTS_URL = "/adverts";

        public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";

        public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

        private Endpoints() {
        }
    }

    public static final class Headers {

        public static final String AUTHORIZATION_HEADER = "Authorization";

        private Headers() {
        }
    }

    public static final class Tokens {

        public static final String TOKEN_TYPE = "Bearer";

        public static final String ACCESS_TOKEN_PREFIX = "Bearer ";

        public static final String UNAUTHORIZED = "unauthorized";

        private Tokens() {
        }
    }

}