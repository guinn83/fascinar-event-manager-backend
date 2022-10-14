package com.api.fascinareventos.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BillDTO {

    @NotBlank
    private String supplier;
    private String description;
    private Long eventId;
}
