package com.api.fascinareventos.services;

import com.api.fascinareventos.dtos.BillInfo;
import com.api.fascinareventos.models.Bill;
import com.api.fascinareventos.repositories.BillRepository;
import com.api.fascinareventos.services.exceptions.DatabaseException;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    private BillRepository repository;

    @Transactional
    public Bill insertBill(Bill bill) {
        try {
            return repository.save(bill);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public Bill updateBill(Long id, Bill bill) {
        Optional<Bill> obj = repository.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException(id, "Bill not found");
        }
        var newBill = new Bill();
        BeanUtils.copyProperties(bill, newBill);
        newBill.setId(obj.get().getId());
        return repository.save(newBill);
    }

    public Page<Bill> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

//    public BillInfo findInfo() {
//        List<Bill> bills = repository.findAll();
//        return new BillInfo(bills);
//    }

    public Optional<Bill> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteBill(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id, "Bill not found");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
