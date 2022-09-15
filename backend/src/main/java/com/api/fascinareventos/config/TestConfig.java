package com.api.fascinareventos.config;

import com.api.fascinareventos.models.User;
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

        User u1 = new User("guinn83", "123456", UserRole.ADMIN, false, true);
        User u2 = new User("vaninha.85", "123456", UserRole.PLANNER, false, true);
        User u3 = new User("moniky", "123456", UserRole.ASSISTANT, false, true);
        User u4 = new User("michele", "123456", UserRole.CUSTOMER, false, true);


        userRepository.saveAll(Arrays.asList(u1, u2, u3, u4));
    }
}
