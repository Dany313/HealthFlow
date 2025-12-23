package com.diaia.notification_service.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {
    private String userId;
    private String doctorId;
    private String dateTime;
}
