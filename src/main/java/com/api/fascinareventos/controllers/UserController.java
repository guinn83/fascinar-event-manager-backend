package com.api.fascinareventos.controllers;

import com.api.fascinareventos.models.User;
import com.api.fascinareventos.repositories.UserRepository;
import com.api.fascinareventos.services.UserService;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@PreAuthorize("hasAuthority('ADMIN')")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private UserRepository repository;

    private static final String USER_NOT_FOUND = "User not found.";

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user = service.insertUser(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") Long id) {
        Optional<User> obj = repository.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        }
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") Long id,
                                             @RequestBody User user) {
        return ResponseEntity.ok().body(service.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
