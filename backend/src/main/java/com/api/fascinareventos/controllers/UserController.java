package com.api.fascinareventos.controllers;

import com.api.fascinareventos.models.User;
import com.api.fascinareventos.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user = service.createUser(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> fingAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> userById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok().body(service.userById(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") Long id,
                                             @RequestBody User user) {
        return ResponseEntity.ok().body(service.updateUser(id, user));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
