package com.api.fascinareventos.dtos;

import com.api.fascinareventos.models.enums.EventStatus;
import com.api.fascinareventos.models.views.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.net.URL;
import java.time.LocalDateTime;

@Data
public class EventInfo {

    @JsonView(View.Summary.class)
    private Long id;
    @JsonView(View.Summary.class)
    private URL avatar;
    @NotBlank
    @JsonView(View.Summary.class)
    private String name;
    @DateTimeFormat
    @JsonView(View.Summary.class)
    private LocalDateTime eventDate;
    @JsonView(View.Summary.class)
    private EventStatus status;

    @JsonView(View.Summary.class)
    private BillInfo billInfo;
}
