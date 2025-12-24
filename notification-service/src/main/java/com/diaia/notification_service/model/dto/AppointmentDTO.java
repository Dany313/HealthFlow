package com.diaia.notification_service.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentDTO(
        UUID id,
        String userId,
        String doctorId,
        LocalDateTime dateTime,
        String status // Possiamo usare String per semplicità o ricreare l'Enum
) {}
