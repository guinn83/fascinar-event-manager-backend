package com.api.fascinareventos.dtos;

import com.api.fascinareventos.utils.enums.RoundOption;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BillCalcInstallmentDTO {

    private LocalDate firstDate;
    private Double totalValue;
    private Double downPaymentValue;
    private Double downPaymentPercent;
    private byte installments;
    private RoundOption roundOption;
}
