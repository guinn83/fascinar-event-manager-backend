package com.api.fascinareventos.security;

import com.api.fascinareventos.config.JwtProperties;
import com.api.fascinareventos.models.UserModel;
import com.api.fascinareventos.services.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
            UserModel userModel = new ObjectMapper().readValue(request.getInputStream(), UserModel.class);

            UserDetails userDetails = userService.findByUsername(userModel.getUsername());

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userModel.getUsername(),
                    userModel.getPassword(),
                    userDetails.getAuthorities()));
        } catch (IOException e) {
            throw new RuntimeException("Fail to authenticate user " + obtainUsername(request), e);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(401);
        response.getWriter().write(failed.getMessage());
        response.getWriter().flush();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UserModel userModel = (UserModel) authResult.getPrincipal();

        String token = JWT.create()
                .withSubject(userModel.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        response.addHeader(jwtProperties.getHeader(), jwtProperties.getPrefix() + token);

        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.getWriter().flush();

//        String redirectURL = "";
//
//        switch (userModel.getUserRole()) {
//            case ADMIN -> redirectURL = "/api/v1/user";
//            case PLANNER -> redirectURL = "/api/v1/planner";
//            case ASSISTANT -> redirectURL = "/api/v1/assistant";
//            case CUSTOMER-> redirectURL = "/api/v1/customer";
//        }
//
////        response.sendRedirect(redirectURL);
    }

}
