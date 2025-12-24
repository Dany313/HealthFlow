package com.diaia.booking_healt.service;

import com.diaia.booking_healt.model.dto.AppointmentDTO;
import com.diaia.booking_healt.model.entity.Appointment;
import com.diaia.booking_healt.model.enums.AppointmentStatus;
import com.diaia.booking_healt.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repository;
    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Transactional
    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        // 1. Trasformazione DTO -> Entity
        Appointment entity = Appointment.builder()
                .doctorId(dto.getDoctorId())
                .userId("USER_TEST_ID") // Poi lo prenderemo dal token
                .dateTime(dto.getDateTime())
                .status(AppointmentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        // 2. Salvataggio su DB Relazionale (Postgres)
        entity = repository.save(entity);

        // 3. Invio messaggio asincrono a Kafka
        // Il Notification Service leggerà questo messaggio
        kafkaTemplate.send("appointment-topic", entity.getId().toString(), entity);

        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public Optional<Appointment> getById(UUID id) {
        return repository.findById(id);
    }

    public List<Appointment> getByUserId(String UserId) {
        return repository.findByUserId(UserId);
    }
}
