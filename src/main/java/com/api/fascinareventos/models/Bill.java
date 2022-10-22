package com.api.fascinareventos.models;

import com.api.fascinareventos.models.enums.BillStatus;
import com.api.fascinareventos.models.views.View;
import com.api.fascinareventos.utils.enums.RoundOption;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.api.fascinareventos.utils.MyUtils.RoundDecimal;
import static com.api.fascinareventos.utils.MyUtils.RoundMultiple;
import static com.api.fascinareventos.utils.enums.RoundOption.DISABLE;


@Entity
@Table(name = "tb_bills")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bill implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JsonView(View.Full.class)
    private Long id;
    @Column(name = "supplier", nullable = false)
    @JsonView(View.Summary.class)
    private String supplier;
    @JsonView(View.Summary.class)
    private String description;

    @JsonIgnore
    @ManyToOne
    private EventModel eventModel;

    @JsonView(View.Full.class)
    @OneToMany(mappedBy = "billModels", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BillInstallment> installmentsList = new ArrayList<>();

    @Transient
    private BillInstallment nextInstallment;
    @JsonView(View.Summary.class)
    private BillStatus status;
    @JsonView(View.Summary.class)
    private LocalDate nextDate;
    @JsonView(View.Summary.class)
    private Double nextInstallmentValue;

    public Bill(String supplier, String description, EventModel eventModel) {
        this.supplier = supplier;
        this.description = description;
        this.eventModel = eventModel;
    }

    public Bill(String supplier, String description, EventModel eventModel, List<BillInstallment> installmentsList) {
        this.supplier = supplier;
        this.description = description;
        this.eventModel = eventModel;
        setInstallmentsList(installmentsList);
    }

    public void setInstallmentsList(List<BillInstallment> installmentsList) {
        this.installmentsList = installmentsList;
        if (!installmentsList.isEmpty()) {
            updateBill();
        }
    }

    @JsonView(View.Base.class)
    public Double getTotalValue() {
        return installmentsList.stream()
                .mapToDouble(BillInstallment::getInstallmentValue)
                .sum();
    }

    @JsonView(View.Base.class)
    public Double getTotalNotPaid() {
        return installmentsList.stream()
                .filter(b -> b.getStatus() != BillStatus.PAGO)
                .mapToDouble(BillInstallment::getInstallmentValue)
                .sum();
    }

    @JsonView(View.Base.class)
    public Double getTotalPayed() {
        return getTotalValue() - getTotalNotPaid();
    }

    @JsonView(View.Base.class)
    public Double getPayedPercent() {
        return RoundDecimal(getTotalPayed() / getTotalValue() * 100.0, 1);
    }

    public void setNextInstallment() {
        for (BillInstallment b : installmentsList) {
            if (b.getStatus() != BillStatus.PAGO) {
                nextInstallment = b;
                break;
            }
        }
    }

    public void setNextDate() {
        nextDate = nextInstallment.getInstallmentDate();
    }

    public void setStatus() {
        if (nextInstallment == null) {
            status = BillStatus.PAGO;
        } else {
            status = nextInstallment.getStatus();
        }
    }

    public void setNextInstallmentValue() {
        nextInstallmentValue = nextInstallment.getInstallmentValue();
    }

    public void updateInstallmentStatus() {
        for (BillInstallment b : installmentsList) {
            b.updateStatus();
        }
    }

    public void updateBill() {
        updateInstallmentStatus();
        setNextInstallment();
        setNextDate();
        setStatus();
        setNextInstallmentValue();
    }

    public List<BillInstallment> calcInstallmentList(LocalDate firstDate, double totalValue, double downPaymentvalue, double downPaymentPercent, byte installments, RoundOption roundOption) {
        List<BillInstallment> list = new ArrayList<>();
        double dpValue = downPaymentvalue;

        if (downPaymentvalue != 0.0 || downPaymentPercent != 0.0) {
            if (downPaymentPercent > 0) {
                dpValue = totalValue * downPaymentPercent / 100.0;
            }
            dpValue = (roundOption == DISABLE) ?
                    RoundDecimal(dpValue, 2) :
                    RoundMultiple(dpValue, roundOption.getValue()) ;
            list.add(new BillInstallment(firstDate, (byte) 0, dpValue, BillStatus.A_PAGAR, this));
        }

        double newValue = totalValue - dpValue;

        double installmentValue = newValue / installments;
        installmentValue = (roundOption == DISABLE) ?
                RoundDecimal(installmentValue, 2) :
                RoundMultiple(installmentValue, roundOption.getValue());

        double lastInstallmentValue = newValue - (installmentValue * (installments - 1));
        lastInstallmentValue = (roundOption == DISABLE) ?
                RoundDecimal(lastInstallmentValue, 2) :
                lastInstallmentValue;

        for (int i = 1; i <= installments; i++) {
            list.add(new BillInstallment(firstDate.plusMonths(i), (byte) i, (i != installments) ? installmentValue : lastInstallmentValue, BillStatus.A_PAGAR, this));
        }
        return list;
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
