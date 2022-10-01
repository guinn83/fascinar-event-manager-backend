package com.api.fascinareventos.DTOs;

import com.api.fascinareventos.models.EventStatus;
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
