package com.nepath.carapp.security.properties;

public class JWTProperties {
    public static final String SECRET = "UluMuluPrzyjazniZnak";
    public static final int ACCESS_TOKEN_EXPIRATION_TIME = 180 * 60 * 1000; // 5 min
    public static final int REFRESH_TOKEN_EXPIRATION_TIME = 90 * 24 * 60 * 1000; // 90 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_PATH = "/authorization/login";
    public static final String REGISTRATION_PATH = "/authorization/register";
    public static final String REFRESH_TOKEN_PATH = "/authorization/refreshToken";
    public static final String LOGOUT_PATH = "/authorization/logout";

    public static final String USERNAME = "username";
    public static final String ROLE = "role";
    public static final String USER_ID = "userId";
}
