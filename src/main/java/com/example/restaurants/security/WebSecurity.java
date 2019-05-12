package com.example.restaurants.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import com.example.restaurants.security.UserDetailsServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Bean;

import java.security.Signature;

import static com.example.restaurants.security.SecurityConstants.*;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurity(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(SIGN_UP_URL, USERS_URL, CITIES_URL, COUNTRIES_URL, COUSINES_URL, CITIES_COUNTRIES_URL, RESTAURANTS_URL, RESTAURANTS_ALL_URL, RESTAURANT_URL, SEARCH_URL, TABLES, TABLES_RESTAURANT, RESTAURANT_FILTER, USERS_COUNT, RESTS_COUNT, LOCATIONS_COUNT, USER_DELETE, RESTAURANT_DELETE, LOCATION_DELETE, COUSINE_DELETE, GET_COUSINE, GET_LOCATION, GET_USER, GET_COUSINES, GET_USERS, GET_LOCATIONS, GET_RESTAURANTS, SEARCH_ADMIN, SAVE_COUSINE, EDIT_COUSINE, SAVE_CITY, EDIT_CITY, EDIT_USER, SAVE_RESTAURANT, EDIT_RESTAURANT, COVER, LOGO, GET_LOGO, GET_RESTAURANT, GET_RESTAURANT_COUSINE, GET_ROLE, SAVE_RESTAURANT_TABLES, GET_RESTAURANT_TABLES, EDIT_RESTAURANT_TABLES, GET_ALL_LOCATIONS, LOCATIONS_EDIT, LOCATIONS_SAVE, SORT, CONFIRM, CANCEL, CHECKTEMP).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
