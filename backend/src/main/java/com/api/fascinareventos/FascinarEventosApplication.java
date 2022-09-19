package com.api.fascinareventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.api.fascinareventos")
public class FascinarEventosApplication {

	public static void main(String[] args) {
		SpringApplication.run(FascinarEventosApplication.class, args);
	}
}
