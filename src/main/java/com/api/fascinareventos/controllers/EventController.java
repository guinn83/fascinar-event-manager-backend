package com.api.fascinareventos.controllers;

import com.api.fascinareventos.dtos.EventDTO;
import com.api.fascinareventos.dtos.EventInfo;
import com.api.fascinareventos.models.EventModel;
import com.api.fascinareventos.models.views.View;
import com.api.fascinareventos.security.JWTAuthorityAnotation;
import com.api.fascinareventos.services.EventService;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/event")
@CrossOrigin("*")
public class EventController {

    @Autowired
    private EventService service;

    private static final String USER_NOT_FOUND = "User not found.";

    @GetMapping
    @JsonView(View.Base.class)
    @JWTAuthorityAnotation.hasPlannerAuthority
    public ResponseEntity<Page<EventModel>> getFullEvents(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "eventDate",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping("/info")
    @JsonView(View.Summary.class)
    @JWTAuthorityAnotation.hasTeamAuthority
    public ResponseEntity<Page<EventInfo>> getEventsSummary(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "eventDate",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok().body(service.findAllInfo(pageable));
    }

    @GetMapping("/{id}")
    @JsonView(View.Base.class)
    @JWTAuthorityAnotation.hasCustomerAuthority
    public ResponseEntity<?> getById(@PathVariable(value = "id") Long id) {
        Optional<EventModel> obj = service.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException(id, USER_NOT_FOUND);
        }
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @JWTAuthorityAnotation.hasPlannerAuthority
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventDTO eventDTO) {
        EventModel obj = new EventModel();
        BeanUtils.copyProperties(eventDTO, obj);
        obj = service.insertEvent(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @PutMapping("/{id}")
    @JWTAuthorityAnotation.hasPlannerAuthority
    public ResponseEntity<?> updateEvent(@PathVariable(name = "id") Long id, @RequestBody @Valid EventModel eventModel) {
        return ResponseEntity.ok().body(service.updateEvent(id, eventModel));
    }

    @DeleteMapping("/{id}")
    @JWTAuthorityAnotation.hasPlannerAuthority
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}
