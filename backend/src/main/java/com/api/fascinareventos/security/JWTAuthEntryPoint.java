package com.api.fascinareventos.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JWTAuthEntryPoint extends BasicAuthenticationEntryPoint {

    public JWTAuthEntryPoint() {
        super();
    }


    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException {
        response.addHeader("www-authorization", "Basic realm=" + getRealmName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.print("HTTP Status 401 - " + authEx.getMessage());
    }


    @Override
    public void afterPropertiesSet() {
        setRealmName("FascinarEventos");
        super.afterPropertiesSet();
    }
}
