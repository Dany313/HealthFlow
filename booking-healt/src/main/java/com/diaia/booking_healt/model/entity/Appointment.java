package com.diaia.booking_healt.model.entity;

import com.diaia.booking_healt.model.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String userId;      // Lo prenderemo dal token Firebase
    private String doctorId;
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // Esempio: PENDING, CONFIRMED, FAILED

    private LocalDateTime createdAt;
}
