package com.api.fascinareventos.dtos;

import com.api.fascinareventos.models.Bill;
import com.api.fascinareventos.models.enums.BillStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.api.fascinareventos.utils.MathUtil.RoundDecimal;

@Data
public class BillInfo {

    @NotBlank
    private List<Bill> billList;

    public BillInfo(List<Bill> billList) {
        this.billList = billList;
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

}
