package com.api.fascinareventos.controllers;

import com.api.fascinareventos.models.UserModel;
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
//@PreAuthorize("hasAuthority('ADMIN')")
@CrossOrigin //temporario
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private UserRepository repository;

    private static final String USER_NOT_FOUND = "User not found.";

    @PostMapping
    public ResponseEntity<UserModel> createUser(@RequestBody UserModel userModel) {
        userModel = service.insertUser(userModel);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userModel.getId()).toUri();
        return ResponseEntity.created(uri).body(userModel);
    }

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok().body(repository.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") Long id) {
        Optional<UserModel> obj = repository.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        }
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") Long id,
                                             @RequestBody UserModel userModel) {
        return ResponseEntity.ok().body(service.updateUser(id, userModel));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
