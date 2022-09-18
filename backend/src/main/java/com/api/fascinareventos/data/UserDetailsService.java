package com.api.fascinareventos.data;

import com.api.fascinareventos.repositories.UserRepository;
import com.api.fascinareventos.services.exceptions.DatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (!repository.existsByUsername(username)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "User [" + username + "] not found");
        }
        return repository.findByUsername(username);
    }
}
