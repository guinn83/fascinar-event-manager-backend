package com.api.fascinareventos.services;

import com.api.fascinareventos.models.User;
import com.api.fascinareventos.repositories.UserRepository;
import com.api.fascinareventos.services.exceptions.DatabaseException;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private static final String USER_NOT_FOUND = "User not found.";
    private UserRepository repository;

    @Transactional
    public User insertUser(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new DatabaseException(HttpStatus.CONFLICT, "User already exists.");
        }
        try {
            return repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> userById(Long id) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        }
        return repository.findById(id);
    }

    @Transactional
    public User updateUser(Long id, User obj) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        }
        var newUser = new User();
        BeanUtils.copyProperties(obj, newUser);
        newUser.setId(userOptional.get().getId());
        return repository.save(newUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (!repository.existsByUsername(username)) {
            throw new DatabaseException(HttpStatus.NOT_FOUND, "User [" + username + "] not found");
        }
        return repository.findByUsername(username);
    }
}
