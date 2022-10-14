package com.api.fascinareventos.controllers;

import com.api.fascinareventos.models.BillInstallment;
import com.api.fascinareventos.services.BillInstallmentService;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/billinstallment")
@CrossOrigin("*")
@PreAuthorize("hasAuthority('ADMIN')")
public class BillInstallmentController {

    @Autowired
    private BillInstallmentService service;

    private static final String NOT_FOUND = "Bill Installment not found.";

    @GetMapping
    public ResponseEntity<List<BillInstallment>> getAllEvents() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(value = "id") Long id) {
        Optional<BillInstallment> obj = service.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException(id, NOT_FOUND);
        }
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody @Valid BillInstallment billInstallment) {
        BillInstallment obj = new BillInstallment();
        BeanUtils.copyProperties(billInstallment, obj);
        obj = service.insertBillInstallment(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable(name = "id") Long id, @RequestBody @Valid BillInstallment billInstallment) {
        return ResponseEntity.ok().body(service.updateBillInstallment(id, billInstallment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        service.deleteBillInstallment(id);
        return ResponseEntity.ok().build();
    }
}
