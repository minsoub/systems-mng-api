package com.bithumbsystems.persistence.mongodb.audit.repository;

import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuditLogCustomRepository {
  Flux<AuditLog> findPageBySearchText(String searchText, LocalDateTime startDate, LocalDateTime endDate, String mySiteId, Pageable pageable);

  Mono<Long> countBySearchText(String searchText, LocalDateTime startDate, LocalDateTime endDate, String mySiteId);
}
