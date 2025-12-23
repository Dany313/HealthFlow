package com.diaia.notification_service.messaging;

import com.diaia.notification_service.model.entity.AuditLog;
import com.diaia.notification_service.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
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

        auditLogRepository.save(audit);
    }

    // Questo metodo viene chiamato se dopo 3 tentativi fallisce ancora
    @DltHandler
    public void handleDlt(Map<String, Object> message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("MESSAGGIO DEFINITIVAMENTE FALLITO nel topic {}: {}", topic, message);
        // Qui potresti inviare una mail all'admin o salvare in un log speciale
    }
}
