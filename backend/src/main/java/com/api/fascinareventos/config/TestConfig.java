package com.api.fascinareventos.config;

import com.api.fascinareventos.models.UserModel;
import com.api.fascinareventos.models.UserRole;
import com.api.fascinareventos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        UserModel u1 = new UserModel("guinn83", "123456", UserRole.ADMIN, false, true);
        UserModel u2 = new UserModel("vaninha.85", "123456", UserRole.PLANNER, false, true);
        UserModel u3 = new UserModel("moniky", "123456", UserRole.ASSISTANT, false, true);
        UserModel u4 = new UserModel("michele", "123456", UserRole.CUSTOMER, false, true);


        userRepository.saveAll(Arrays.asList(u1, u2, u3, u4));
    }
}
