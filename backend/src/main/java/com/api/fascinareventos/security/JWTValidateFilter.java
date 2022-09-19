package com.api.fascinareventos.security;

import com.api.fascinareventos.config.JwtProperties;
import com.api.fascinareventos.services.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
                                    FilterChain chain) throws IOException, ServletException {
        String atribute = request.getHeader(HEADER_ATRIBUTE);

        if (atribute == null || !atribute.startsWith(ATRIBUTE_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = atribute.replace(ATRIBUTE_PREFIX, "");

        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

//        request.authenticate(response);
  /*      System.out.println(authenticationToken != null ? authenticationToken.getAuthorities() : null);
        System.out.println("Autenticado: " + request.authenticate(response));
        assert authenticationToken != null;
        System.out.println("toString: " + authenticationToken.getAuthorities().toString());
        System.out.println("toString: " + authenticationToken.getAuthorities().stream().findFirst().get().getAuthority());
        System.out.println("getCharacterEncoding: " + response.getCharacterEncoding());

        System.out.println(request.isUserInRole("ADMIN"));*/
        /*Principal userPrincipal = request.getUserPrincipal();
        GenericPrincipal genericPrincipal = (GenericPrincipal) userPrincipal;
        String[] roles = genericPrincipal.getRoles();
        System.out.println("Roles: " + Arrays.toString(roles));*/
//            PrintWriter writer = response.getWriter();
//            writer.println("HTTP Status 401 - " + e.getMessage());
        chain.doFilter(request, response);
    }



    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        String username = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
                .build()
                .verify(token)
                .getSubject();

        if (username == null) return null;
        UserDetails userDetails = userService.findByUsername(username);

        return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
    }
}
