package com.api.fascinareventos.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "tb_bills")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bill implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "supplier", nullable = false)
    private String supplier;
    private String description;
    private BillStatus status;

//    @Transient
    private LocalDate nextDate;

    @JsonIgnore
    @OneToOne
    private EventModel eventModel;

    @OneToMany(mappedBy = "billModels")
    private List<BillInstallment> installments = new ArrayList<>();

    public Bill(String supplier, String description, EventModel eventModel) {
//        this.initialDate = initialDate;
        this.supplier = supplier;
        this.description = description;
        this.eventModel = eventModel;
    }

    public Bill(String supplier, String description, EventModel eventModel, List<BillInstallment> installments) {
//        this.initialDate = initialDate;
        this.supplier = supplier;
        this.description = description;
        this.eventModel = eventModel;
        this.installments = installments;
    }

    public Double getTotalValue() {
        return installments.stream()
                .mapToDouble(BillInstallment::getInstallmentValue)
                .sum();
    }

    public Double sumBillNotPaid() {
        return installments.stream()
                .filter(b -> b.getStatus() == BillStatus.A_PAGAR)
                .mapToDouble(BillInstallment::getInstallmentValue)
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Bill that = (Bill) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
