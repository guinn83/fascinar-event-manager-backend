package com.api.fascinareventos.controllers;

import com.api.fascinareventos.dtos.BillCalcInstallmentDTO;
import com.api.fascinareventos.dtos.BillDTO;
import com.api.fascinareventos.dtos.BillInfo;
import com.api.fascinareventos.models.Bill;
import com.api.fascinareventos.models.EventModel;
import com.api.fascinareventos.models.views.View;
import com.api.fascinareventos.security.JWTAuthorityAnotation;
import com.api.fascinareventos.services.BillService;
import com.api.fascinareventos.services.EventService;
import com.api.fascinareventos.services.exceptions.DatabaseException;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import com.api.fascinareventos.utils.enums.RoundOption;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bill")
@CrossOrigin("*")
@JWTAuthorityAnotation.hasCustomerAuthority
public class BillController {

    @Autowired
    private BillService service;
    @Autowired
    private EventService eventService;

    private static final String NOT_FOUND = "Bill not found.";

    @GetMapping
    public ResponseEntity<Page<Bill>> getAllBills(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "nextDate",
                    direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @GetMapping("/info")
    @JsonView(View.Summary.class)
    public ResponseEntity<BillInfo> getInfo() {
        return ResponseEntity.ok().body(service.findInfo());
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
    public ResponseEntity<?> createBill(@RequestBody @Valid BillDTO billDTO) {
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

    @PatchMapping("/{id}/calcInstallment")
    public ResponseEntity<?> createBillInstallments(@PathVariable(name = "id") Long id, @RequestBody BillCalcInstallmentDTO obj) {
        Optional<Bill> bill = service.findById(id);
        if (bill.isEmpty()) {
            throw new ResourceNotFoundException(id, NOT_FOUND);
        }

        if (!bill.get().getInstallmentsList().isEmpty()) {
            throw new DatabaseException(HttpStatus.CONFLICT, "Parcelas já existem nessa conta");
        }

        LocalDate firstDate = obj.getFirstDate();
        double totalValue = obj.getTotalValue();
        double downPaymentValue = obj.getDownPaymentValue();
        double downPaymentPercent = obj.getDownPaymentPercent();
        byte installments = (byte) obj.getInstallments();
        RoundOption roundOption = obj.getRoundOption();

        bill.get().setInstallmentsList(bill.get().calcInstallmentList(firstDate, totalValue, downPaymentValue, downPaymentPercent, installments, roundOption));
        return ResponseEntity.ok().body(service.updateBill(id, bill.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBill(@PathVariable(name = "id") Long id, @RequestBody @Valid Bill billModel) {
        return ResponseEntity.ok().body(service.updateBill(id, billModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        service.deleteBill(id);
        return ResponseEntity.ok().build();
    }
}
