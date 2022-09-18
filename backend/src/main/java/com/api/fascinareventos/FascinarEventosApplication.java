package com.api.fascinareventos;

import com.api.fascinareventos.config.JwtProperties;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
@ComponentScan(basePackages = "com.api.fascinareventos")
public class FascinarEventosApplication {

	public static void main(String[] args) {
		SpringApplication.run(FascinarEventosApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(JwtProperties jwtProperties) {
		return args -> {
//			System.out.println("Proterties: " + appProperties);
		};
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@ConfigurationProperties(prefix = "jwt")
	JwtProperties jwtProperties() {
		return new JwtProperties();
	}


}
