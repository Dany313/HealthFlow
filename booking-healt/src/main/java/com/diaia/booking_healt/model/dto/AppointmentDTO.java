package com.diaia.booking_healt.model.dto;

import com.diaia.booking_healt.model.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AppointmentDTO {
    private UUID id;
    private String doctorId;
    private String userId;
    private LocalDateTime dateTime;
    private AppointmentStatus status;
}
