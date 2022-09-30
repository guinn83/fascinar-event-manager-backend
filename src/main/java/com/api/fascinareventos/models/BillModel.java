package com.api.fascinareventos.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_bills")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BillModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "initial_date", nullable = false)
    private LocalDate initialDate;
    @Column(name = "supplier", nullable = false)
    private String supplier;
    private String description;
    private Double totalValue;
    private BillStatus status;

    @OneToOne
    private EventModel eventModel;

    @OneToMany(mappedBy = "billModels")
    private Set<BillInstallmentModel> installments = new HashSet<>();

    public BillModel(LocalDate initialDate, String supplier, String description, Double totalValue, BillStatus status, EventModel eventModel) {
        this.initialDate = initialDate;
        this.supplier = supplier;
        this.description = description;
        this.totalValue = totalValue;
        this.status = status;
        this.eventModel = eventModel;
    }

    public BillModel(LocalDate initialDate, String supplier, String description, Double totalValue, BillStatus status, EventModel eventModel, Set<BillInstallmentModel> installments) {
        this.initialDate = initialDate;
        this.supplier = supplier;
        this.description = description;
        this.totalValue = totalValue;
        this.status = status;
        this.eventModel = eventModel;
        this.installments = installments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BillModel that = (BillModel) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
