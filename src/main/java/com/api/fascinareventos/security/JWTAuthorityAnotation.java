package com.api.fascinareventos.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class JWTAuthorityAnotation {

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('ADMIN')")
    public @interface hasAdminAuthority {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('ADMIN') || " +
            "hasAuthority('PLANNER')")
    public @interface hasPlannerAuthority {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('ADMIN') || " +
            "hasAuthority('PLANNER') || " +
            "hasAuthority('ASSISTANT')")
    public @interface hasTeamAuthority {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('ADMIN') || " +
            "hasAuthority('PLANNER') || " +
            "hasAuthority('CUSTOMER')")
    public @interface hasCustomerAuthority {
    }
}
