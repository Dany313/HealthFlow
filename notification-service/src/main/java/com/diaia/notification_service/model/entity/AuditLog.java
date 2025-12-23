package com.diaia.notification_service.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "audit_logs") // NoSQL Collection
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class AuditLog {
    @Id
    private String id; // Mongo usa spesso String o ObjectId per gli ID
    private UUID appointmentId;
    private String doctorId;
    private String userId;
    private String status;
    private String message;
    private LocalDateTime timestamp;
}
