package com.diaia.notification_service.repository;

import com.diaia.notification_service.model.entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
}
