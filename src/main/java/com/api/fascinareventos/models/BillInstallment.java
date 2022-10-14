package com.api.fascinareventos.models;

import com.api.fascinareventos.models.enums.BillStatus;
import com.api.fascinareventos.models.views.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
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
    @JsonView(View.Full.class)
    private Long id;
    @Column(name = "installment_date", nullable = false)
    @JsonView(View.Base.class)
    private LocalDate installmentDate;
    @JsonView(View.Base.class)
    private Byte installment;
    @JsonView(View.Base.class)
    private Double installmentValue;
    @JsonView(View.Base.class)
    private BillStatus status = BillStatus.A_PAGAR;
    @JsonView(View.Base.class)
    private LocalDate paymentDate;

    @JsonIgnore
    @ManyToOne
    private Bill billModels;

    public BillInstallment(LocalDate installmentDate, Byte installment, Double installmentValue, BillStatus status) {
        this.installmentDate = installmentDate;
        this.installment = installment;
        this.installmentValue = installmentValue;
        this.status = status;
    }

    public BillInstallment(LocalDate installmentDate, Byte installment, Double installmentValue, BillStatus status, Bill billModels) {
        this.installmentDate = installmentDate;
        this.installment = installment;
        this.installmentValue = installmentValue;
        this.status = status;
        this.billModels = billModels;
    }

    public BillInstallment(LocalDate installmentDate, Byte installment, Double installmentValue, BillStatus status, Bill billModels, LocalDate paymentDate) {
        this.installmentDate = installmentDate;
        this.installment = installment;
        this.installmentValue = installmentValue;
        this.status = status;
        this.billModels = billModels;
        this.paymentDate = paymentDate;
    }

    public void updateStatus() {
        if (getStatus() == BillStatus.A_PAGAR && getInstallmentDate().isBefore(LocalDate.now())) {
            setStatus(BillStatus.VENCIDA);
        }
    }

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
