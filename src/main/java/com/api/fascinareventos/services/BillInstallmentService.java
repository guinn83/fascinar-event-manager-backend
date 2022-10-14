package com.api.fascinareventos.services;

import com.api.fascinareventos.models.BillInstallment;
import com.api.fascinareventos.repositories.BillInstallmentRepository;
import com.api.fascinareventos.services.exceptions.DatabaseException;
import com.api.fascinareventos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BillInstallmentService {

    @Autowired
    private BillInstallmentRepository repository;

    @Transactional
    public BillInstallment insertBillInstallment(BillInstallment billInstallment) {
        try {
            return repository.save(billInstallment);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Transactional
    public BillInstallment updateBillInstallment(Long id, BillInstallment billInstallment) {
        Optional<BillInstallment> obj = repository.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException(id, "BillInstallment not found");
        }
        var newBillInstallment = new BillInstallment();
        BeanUtils.copyProperties(billInstallment, newBillInstallment);
        newBillInstallment.setId(obj.get().getId());
        return repository.save(newBillInstallment);
    }

    public List<BillInstallment> findAll() {
        return repository.findAll();
    }

    public Optional<BillInstallment> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteBillInstallment(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id, "BillInstallment not found");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }


}
