package com.api.fascinareventos.dtos;

import com.api.fascinareventos.models.enums.EventStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.net.URL;
import java.time.LocalDateTime;

@Data
public class EventDTO {

    private URL avatar;
    @NotBlank
    private String name;
    @DateTimeFormat
    private LocalDateTime eventDate;
    private EventStatus status;
}
