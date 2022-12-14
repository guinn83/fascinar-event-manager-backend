package com.api.fascinareventos.security;

import com.api.fascinareventos.config.JwtProperties;
import com.api.fascinareventos.controllers.exceptions.ResponseError;
import com.api.fascinareventos.models.User;
import com.api.fascinareventos.security.exceptions.WebSecurityException;
import com.api.fascinareventos.services.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JWTAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;
    private final UserService userService;

    public JWTAuthFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProperties = jwtProperties;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException{
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

            UserDetails userDetails = userService.findByUsername(user.getUsername());

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    userDetails.getAuthorities()));
        } catch (UsernameNotFoundException e) {
            throw new WebSecurityException(HttpStatus.NOT_FOUND, "Unauthorized: User not found");
        } catch (IOException | RuntimeException e) {
            throw new WebSecurityException(HttpStatus.UNAUTHORIZED, "Fail to authenticate user " + obtainUsername(request));
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ResponseError.sendError(response, request, 401, "Authentication error", e);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.getWriter().flush();
    }

}
