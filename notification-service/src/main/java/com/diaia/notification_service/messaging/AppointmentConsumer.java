package com.diaia.notification_service.messaging;

import com.diaia.notification_service.model.dto.AppointmentDTO;
import com.diaia.notification_service.model.entity.AuditLog;
import com.diaia.notification_service.repository.AuditLogRepository;
import com.diaia.notification_service.service.BookingClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentConsumer {

    private final AuditLogRepository auditLogRepository;
    private final BookingClient bookingClient;

    @KafkaListener(topics = "appointment-topic", groupId = "notification-group")
    public void consume(Map<String, Object> message) {
        log.info("Ricevuto messaggio: {}", message);

        // Simulazione errore: se il doctorId è "FAIL", lancia un'eccezione
        if ("FAIL".equals(message.get("doctorId"))) {
            throw new RuntimeException("Errore simulato per testare il Retry!");
        }

        AuditLog audit = AuditLog.builder()
                .appointmentId(UUID.fromString(message.get("id").toString()))
                .doctorId(message.get("doctorId").toString())
                .userId(message.get("userId").toString())
                .status("PROCESSED")
                .timestamp(LocalDateTime.now())
                .build();

        handleNotification(message.get("id").toString());

        auditLogRepository.save(audit);
    }

    public void handleNotification(String appointmentId) {
        // 1. Chiamata sincrona via Feign per recuperare i dettagli mancanti
        AppointmentDTO appointment = bookingClient.getAppointmentById(UUID.fromString(appointmentId));

        // 2. Ora hai tutti i dati (nome dottore, data, ecc.) per creare la notifica
        System.out.println("Invio notifica per l'appuntamento di: " + appointment.id());

        // 3. Qui salverai nel tuo MongoDB
    }

    // Questo metodo viene chiamato se dopo 3 tentativi fallisce ancora
    @DltHandler
    public void handleDlt(Map<String, Object> message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("MESSAGGIO DEFINITIVAMENTE FALLITO nel topic {}: {}", topic, message);
        // Qui potresti inviare una mail all'admin o salvare in un log speciale
    }
}
