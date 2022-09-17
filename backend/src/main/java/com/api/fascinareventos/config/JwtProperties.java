package com.api.fascinareventos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "jwt")
@Data
@Validated
public class JwtProperties {

    @NotBlank
    private String header;
    @NotBlank
    private String secret;
    @Min(60000)
    private Long expiration;
    @NotBlank
    private String prefix;
}
