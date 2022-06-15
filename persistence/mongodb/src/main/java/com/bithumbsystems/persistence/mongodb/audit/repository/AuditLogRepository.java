package com.bithumbsystems.persistence.mongodb.audit.repository;

import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AuditLogRepository extends ReactiveMongoRepository<AuditLog, String>, AuditLogCustomRepository {

}
