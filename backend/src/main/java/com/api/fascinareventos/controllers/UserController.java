package com.api.fascinareventos.controllers;

import com.api.fascinareventos.models.UserModel;
import com.api.fascinareventos.repositories.UserRepository;
import com.api.fascinareventos.services.UserService;
import com.api.fascinareventos.services.exceptions.DatabaseException;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private UserRepository repository;

    private static final String USER_NOT_FOUND = "User not found.";

    @PostMapping
    @Transactional
    public ResponseEntity<UserModel> createUser(@RequestBody UserModel userModel) {
        if (repository.existsByUsername(userModel.getUsername())) {
            throw new DatabaseException(HttpStatus.CONFLICT, "User already exists.");
        }
        try {
            repository.save(userModel);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userModel.getId()).toUri();
            return ResponseEntity.created(uri).body(userModel);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") Long id) {
        Optional<UserModel> obj = repository.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        }
        return new ResponseEntity<>(obj.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") Long id,
                                             @RequestBody UserModel userModel) {
        Optional<UserModel> userOptional = repository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        }
        var newUser = new UserModel();
        BeanUtils.copyProperties(userModel, newUser);
        newUser.setId(userOptional.get().getId());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
