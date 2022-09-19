package com.api.fascinareventos.services;

import com.api.fascinareventos.models.UserModel;
import com.api.fascinareventos.repositories.UserRepository;
import com.api.fascinareventos.services.exceptions.DatabaseException;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service("userService")
public class UserService {

    private static final String USER_NOT_FOUND = "User not found.";
    @Autowired
    private final UserRepository repository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserDetails findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Transactional
    public UserModel insertUser(UserModel userModel) {
        if (repository.existsByUsername(userModel.getUsername())) {
            throw new DatabaseException(HttpStatus.CONFLICT, "User already exists.");
        }
        try {
            userModel.setPassword(encoder.encode(userModel.getPassword()));
            return repository.save(userModel);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public UserModel updateUser(Long id, UserModel obj) {
        Optional<UserModel> userOptional = repository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        }
        var newUser = new UserModel();
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
}
