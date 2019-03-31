package hr.foi.diplomski.central.constants;

public class SecurityConstants {

    public static final String AUTH_LOGIN_URL = "/api/authenticate";

    public static final String JWT_SECRET = "F5E067FA764F978DA3D5321E44E0C45236557D61768EF2D2A379ABC99C4497B4124C0B22C9B7EC538A23D8A73B6F12027688BB8AAFBF701DC1B3A938DE104E2A";

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_ISSUER = "secure-api";
    public static final String TOKEN_AUDIENCE = "secure-app";

    public static final String REQUEST_USERNAME = "username";
    public static final String REQUEST_PASSWORD = "password";

}
