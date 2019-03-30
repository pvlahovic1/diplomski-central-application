package hr.foi.diplomski.central.constants;

public class SecurityConstants {

    public static final String AUTH_LOGIN_URL = "/api/authenticate";

    public static final String JWT_SECRET = "c9e350fedda19b625b050a732bb09269c2f2e6867b8c2598a31a5863d2658b7a";

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    public static final String REQUEST_USERNAME = "username";
    public static final String REQUEST_PASSWORD = "password";

}
