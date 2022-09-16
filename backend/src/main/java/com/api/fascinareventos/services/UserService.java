package com.api.fascinareventos.services;

import com.api.fascinareventos.repositories.UserRepository;
import com.api.fascinareventos.services.exceptions.DatabaseException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (!repository.existsByUsername(username)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "User [" + username + "] not found");
        }
        return repository.findByUsername(username);
    }
}
