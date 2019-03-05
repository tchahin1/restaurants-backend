package com.example.restaurants.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 64_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/register";
    public static final String COUNTRIES_URL = "/countries/all";
    public static final String CITIES_COUNTRIES_URL = "/cities/country";
    public static final String RESTAURANTS_URL = "/restaurants/all";
    public static final String RESTAURANT_URL = "/restaurants/name";
    public static final String SEARCH_URL = "/restaurants/search";
    public static final String TABLES = "/tables/type";
    public static final String TABLES_RESTAURANT = "/tables/restaurant";
    public static final String RESTAURANT_FILTER = "/restaurants/search/filterBy";
}
