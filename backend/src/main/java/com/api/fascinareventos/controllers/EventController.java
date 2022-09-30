package com.api.fascinareventos.controllers;

import com.api.fascinareventos.models.EventModel;
import com.api.fascinareventos.repositories.EventRepository;
import com.api.fascinareventos.services.EventService;
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
@RequestMapping("/api/v1/event")
@CrossOrigin("*")
@PreAuthorize("hasAuthority('ADMIN')")
public class EventController {

    @Autowired
    private EventService service;
    @Autowired
    private EventRepository repository;

    private static final String USER_NOT_FOUND = "User not found.";

    @GetMapping
    public ResponseEntity<List<EventModel>> getAllEvents() {
        return ResponseEntity.ok().body(repository.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable(value = "id") Long id) {
        Optional<EventModel> obj = repository.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        }
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventModel eventModel) {
        eventModel = service.insertEvent(eventModel);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(eventModel.getId()).toUri();
        return ResponseEntity.created(uri).body(eventModel);
    }
}
