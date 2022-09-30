package com.api.fascinareventos.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "tb_bills_installment")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BillInstallment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "installment_date", nullable = false)
    private LocalDate installmentDate;
    private Byte installment;
    private Double installmentValue;
    private BillStatus status;
    private LocalDate paymentDate;

    @ManyToOne
    private Bill billModels;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillInstallment that = (BillInstallment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
