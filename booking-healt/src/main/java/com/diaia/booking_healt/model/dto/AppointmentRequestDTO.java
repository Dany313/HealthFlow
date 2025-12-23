package com.diaia.booking_healt.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {
    private String doctorId;
    private LocalDateTime dateTime;
    // Non mettiamo userId perché lo prenderemo dal Token Firebase!
}
