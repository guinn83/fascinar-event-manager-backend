package com.api.fascinareventos.controllers;

import com.api.fascinareventos.dtos.BillDTO;
import com.api.fascinareventos.models.Bill;
import com.api.fascinareventos.models.EventModel;
import com.api.fascinareventos.services.BillService;
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
@RequestMapping("/api/v1/bill")
@CrossOrigin("*")
@PreAuthorize("hasAuthority('ADMIN')")
public class BillController {

    @Autowired
    private BillService service;
    @Autowired
    private EventService eventService;

    private static final String NOT_FOUND = "Bill not found.";

    @GetMapping
    public ResponseEntity<Page<Bill>> getAllEvents(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "nextDate",
                    direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(value = "id") Long id) {
        Optional<Bill> obj = service.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException(id, NOT_FOUND);
        }
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody @Valid BillDTO billDTO) {
        Bill obj = new Bill();
        Long eventId = billDTO.getEventId();
        Optional<EventModel> eventModel = eventService.findById(eventId);
        if (eventModel.isEmpty()) {
            throw new ResourceNotFoundException(eventId, "Event not found");
        }
        BeanUtils.copyProperties(billDTO, obj);
        obj.setEventModel(eventModel.get());
        obj = service.insertBill(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable(name = "id") Long id, @RequestBody @Valid Bill billModel) {
        return ResponseEntity.ok().body(service.updateBill(id, billModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        service.deleteBill(id);
        return ResponseEntity.ok().build();
    }
}
