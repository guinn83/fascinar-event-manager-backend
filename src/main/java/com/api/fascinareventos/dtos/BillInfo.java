package com.api.fascinareventos.dtos;

import com.api.fascinareventos.models.Bill;
import com.api.fascinareventos.models.enums.BillStatus;
import com.api.fascinareventos.models.views.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Comparator;
import java.util.List;

import static com.api.fascinareventos.utils.MyUtils.RoundDecimal;

@Data
public class BillInfo {

    @JsonIgnore
    @NotBlank
    private List<Bill> billList;

    @JsonView(View.Summary.class)
    private Bill nextBill;

    public BillInfo(List<Bill> billList) {
        this.billList = billList;
        if (!billList.isEmpty()) {
            setNextBill();
        }
    }

    @JsonView(View.Summary.class)
    public Double getTotalValue() {
        return billList.stream()
                .mapToDouble(Bill::getTotalValue)
                .sum();
    }

    @JsonView(View.Summary.class)
    public Double getTotalNotPaid() {
        return billList.stream()
                .filter(b -> b.getStatus() != BillStatus.PAGO)
                .mapToDouble(Bill::getTotalNotPaid)
                .sum();
    }

    @JsonView(View.Summary.class)
    public Double getTotalPayed() {
        return getTotalValue() - getTotalNotPaid();
    }

    @JsonView(View.Summary.class)
    public Double getPayedPercent() {
        return RoundDecimal(getTotalPayed() / getTotalValue() * 100.0, 1);
    }

    public void setNextBill() {
        nextBill = billList.stream()
                .filter(b -> b.getNextDate() != null)
                .min(Comparator.comparing(Bill::getNextDate))
                .orElse(null);
    }
}
