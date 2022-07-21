package com.bithumbsystems.persistence.mongodb.audit.service;

import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import com.bithumbsystems.persistence.mongodb.audit.repository.AuditLogRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuditLogDomainService {

  private final AuditLogRepository auditLogRepository;

  public Flux<AuditLog> findPageBySearchText(String searchText, LocalDateTime startDate, LocalDateTime endDate, String mySiteId, Pageable pageable) {
    return auditLogRepository.findPageBySearchText(searchText, startDate, endDate, mySiteId, pageable);
  }

  public Mono<Long> countBySearchText(String searchText, LocalDateTime startDate, LocalDateTime endDate, String mySiteId) {
    return auditLogRepository.countBySearchText(searchText, startDate, endDate, mySiteId);
  }

  public Mono<AuditLog> save(AuditLog auditLog) {
    return auditLogRepository.save(auditLog);
  }
}
