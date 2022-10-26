package com.api.fascinareventos.security;

import com.api.fascinareventos.config.JwtProperties;
import com.api.fascinareventos.controllers.exceptions.ResponseError;
import com.api.fascinareventos.services.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Service
public class JWTValidateFilter extends BasicAuthenticationFilter {

    private final JwtProperties jwtProperties;
    private final UserService userService;
    public static String HEADER_ATRIBUTE;
    public static String ATRIBUTE_PREFIX;

    public JWTValidateFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties, UserService userService) {
        super(authenticationManager);
        this.jwtProperties = jwtProperties;
        this.userService = userService;
        HEADER_ATRIBUTE = jwtProperties.getHeader();
        ATRIBUTE_PREFIX = jwtProperties.getPrefix();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException, AuthenticationException {
        String atribute = request.getHeader(HEADER_ATRIBUTE);

        if (atribute == null || !atribute.startsWith(ATRIBUTE_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = atribute.replace(ATRIBUTE_PREFIX, "");

        try {
            UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        } catch (RuntimeException e) {
//            throw new WebSecurityException(HttpStatus.BAD_REQUEST, e.getMessage());
            ResponseError.sendError(response, request, 401, "Unauthorized", e);
        }

    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {

        DecodedJWT jwt = JWT.decode(token);
        if( jwt.getExpiresAt().before(new Date())) {
            System.out.println("token is expired");
            //throw new WebSecurityException(HttpStatus.BAD_REQUEST, "Token is expired");
        }

        String username = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
                .build()
                .verify(token)
                .getSubject();

        if (username == null) return null;
        UserDetails userDetails = userService.findByUsername(username);

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
