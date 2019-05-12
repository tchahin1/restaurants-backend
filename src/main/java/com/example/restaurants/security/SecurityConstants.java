package com.example.restaurants.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 64_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/register";
    public static final String USERS_URL = "/users/all";
    public static final String CITIES_URL = "/cities/all";
    public static final String COUNTRIES_URL = "/countries/all";
    public static final String COUSINES_URL = "/cousines/all";
    public static final String SEARCH_ADMIN = "/search/get";
    public static final String CITIES_COUNTRIES_URL = "/cities/country";
    public static final String RESTAURANTS_URL = "/restaurants/all";
    public static final String RESTAURANTS_ALL_URL = "/restaurants/find/all";
    public static final String RESTAURANT_URL = "/restaurants/name";
    public static final String SEARCH_URL = "/restaurants/search";
    public static final String TABLES = "/tables/type";
    public static final String TABLES_RESTAURANT = "/tables/restaurant";
    public static final String RESTAURANT_FILTER = "/restaurants/search/filterBy";
    public static final String USERS_COUNT = "/users/count";
    public static final String RESTS_COUNT = "/restaurants/count";
    public static final String LOCATIONS_COUNT = "/cities/count";
    public static final String USER_DELETE = "/users/delete";
    public static final String LOCATION_DELETE = "/cities/delete";
    public static final String COUSINE_DELETE = "/cousines/delete";
    public static final String RESTAURANT_DELETE = "/restaurants/delete";
    public static final String GET_USER = "/users/get";
    public static final String GET_ROLE = "/users/get/role";
    public static final String GET_LOCATION = "/cities/get";
    public static final String GET_COUSINE = "/cousines/get";

    public static final String GET_RESTAURANT = "/restaurants/get/basicDetails";
    public static final String GET_LOGO = "/restaurants/get/logo";
    public static final String GET_RESTAURANT_COUSINE = "/restaurants/get/cousine";

    public static final String GET_COUSINES = "/cousines/get/search";
    public static final String GET_USERS = "/users/get/search";
    public static final String GET_LOCATIONS = "/cities/get/search";
    public static final String GET_RESTAURANTS = "/restaurants/get/search";

    public static final String SAVE_COUSINE = "/cousines/save";
    public static final String EDIT_COUSINE = "/cousines/edit";
    public static final String SAVE_CITY = "/cities/save";
    public static final String EDIT_CITY = "/cities/edit";
    public static final String EDIT_USER = "/users/edit";
    public static final String SAVE_RESTAURANT = "/restaurants/save";
    public static final String EDIT_RESTAURANT = "/restaurants/edit";

    public static final String LOGO = "/restaurants/saveLogo";
    public static final String COVER = "/restaurants/saveCover";

    public static final String SAVE_RESTAURANT_TABLES = "/restaurants/save/tables";
    public static final String GET_RESTAURANT_TABLES = "/restaurants/get/tables";
    public static final String EDIT_RESTAURANT_TABLES = "/restaurants/edit/tables";

    public static final String GET_ALL_LOCATIONS = "/locations/all";
    public static final String LOCATIONS_SAVE = "/locations/save";
    public static final String LOCATIONS_EDIT = "/locations/edit";

    public static final String SORT = "/restaurants/sort";
    public static final String CONFIRM = "/reservations/confirm";
    public static final String CANCEL = "/reservations/cancel";
    public static final String CHECKTEMP = "/reservations/checkTemp";
}
