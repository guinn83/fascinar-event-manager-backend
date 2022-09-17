package com.api.fascinareventos.security;

import com.api.fascinareventos.config.JwtProperties;
import com.api.fascinareventos.models.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtProperties jwtProperties;
    public static String TOKEN_PWD;
    public static @Min(60000) Long TOKEN_EXPIRATION;

    public JWTAuthFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties) {
        this.authenticationManager = authenticationManager;
        this.jwtProperties = jwtProperties;
        TOKEN_PWD = jwtProperties.getSecret();
        TOKEN_EXPIRATION = jwtProperties.getExpiration();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UserModel userModel = new ObjectMapper().readValue(request.getInputStream(), UserModel.class);

            System.out.println(userModel.getUsername() + " | " + userModel.getPassword());

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userModel.getUsername(),
                    userModel.getPassword(),
                    new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException("Fail to authenticate user", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UserModel userModel = (UserModel) authResult.getPrincipal();

        String token = JWT.create()
                .withSubject(userModel.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC512(TOKEN_PWD));

        response.getWriter().write(token);
        response.getWriter().flush();
    }

    public JwtProperties getJwtProperties() {
        return jwtProperties;
    }
}
