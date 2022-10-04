package com.api.fascinareventos.repositories;

import com.api.fascinareventos.models.BillInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillInstallmentRepository extends JpaRepository<BillInstallment, Long> {
}
