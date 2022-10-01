package com.api.fascinareventos.controllers;

import com.api.fascinareventos.DTOs.EventDTO;
import com.api.fascinareventos.models.EventModel;
import com.api.fascinareventos.repositories.EventRepository;
import com.api.fascinareventos.services.EventService;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/event")
@CrossOrigin("*")
@PreAuthorize("hasAuthority('ADMIN')")
public class EventController {

    @Autowired
    private EventService service;

    private static final String USER_NOT_FOUND = "User not found.";

    @GetMapping
    public ResponseEntity<Page<EventModel>> getAllEvents(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "eventDate",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable(value = "id") Long id) {
        Optional<EventModel> obj = service.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        }
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventDTO eventDTO) {
        EventModel obj = new EventModel();
        BeanUtils.copyProperties(eventDTO, obj);
        obj = service.insertEvent(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable(name = "id") Long id, @RequestBody @Valid EventModel eventModel) {
        return ResponseEntity.ok().body(service.updateEvent(id, eventModel));
    }
}
