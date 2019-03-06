package com.example.restaurants.security;

import com.auth0.jwt.JWT;
import com.example.restaurants.data.models.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.example.restaurants.security.SecurityConstants.EXPIRATION_TIME;
import static com.example.restaurants.security.SecurityConstants.HEADER_STRING;
import static com.example.restaurants.security.SecurityConstants.SECRET;
import static com.example.restaurants.security.SecurityConstants.TOKEN_PREFIX;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            Users creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Users.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String username = ((User) auth.getPrincipal()).getUsername();

        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        //res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        LocalDateTime date =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis() + EXPIRATION_TIME), ZoneId.systemDefault());

        JSONObject result = new JSONObject();
        result.put("user", username);
        result.put("tokenExpiresAt", date);
        result.put("token", TOKEN_PREFIX + token);

        res.getWriter().write(result.toString());
        res.getWriter().flush();
        res.getWriter().close();
    }
}
