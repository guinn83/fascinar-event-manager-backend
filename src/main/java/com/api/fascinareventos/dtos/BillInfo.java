package com.api.fascinareventos.dtos;

import com.api.fascinareventos.models.Bill;
import com.api.fascinareventos.models.enums.BillStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

import static com.api.fascinareventos.utils.MathUtil.RoundDecimal;

@Data
public class BillInfo {

    @NotBlank
    private List<Bill> billList;

    private Bill nextBill;

    public BillInfo(List<Bill> billList) {
        this.billList = billList;
        if (!billList.isEmpty()) {
            setNextBill();
        }
    }

    public Double getTotalValue() {
        return billList.stream()
                .mapToDouble(Bill::getTotalValue)
                .sum();
    }

    public Double getTotalNotPaid() {
        return billList.stream()
                .filter(b -> b.getStatus() != BillStatus.PAGO)
                .mapToDouble(Bill::getTotalValue)
                .sum();
    }

    public Double getTotalPayed() {
        return getTotalValue() - getTotalNotPaid();
    }

    public Double getPayedPercent() {
        return RoundDecimal(getTotalPayed() / getTotalValue() * 100.0, 1);
    }

    public void setNextBill() {
        LocalDate date = LocalDate.now();
        int diff = 10000;
        for (Bill b : billList) {
            if (b.getStatus() != BillStatus.PAGO) {
                if (b.getNextDate().compareTo(date) < diff) {
                    diff = b.getNextDate().compareTo(date);
                    nextBill = b;
                }
            }
        }
    }
}
