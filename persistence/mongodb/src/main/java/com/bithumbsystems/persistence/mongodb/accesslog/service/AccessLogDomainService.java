package com.bithumbsystems.persistence.mongodb.accesslog.service;

import com.bithumbsystems.persistence.mongodb.accesslog.model.entity.AccessLog;
import com.bithumbsystems.persistence.mongodb.accesslog.repository.AccessLogCustomRepository;
import com.bithumbsystems.persistence.mongodb.accesslog.repository.AccessLogRepository;
import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AccessLogDomainService {
    private final AccessLogRepository accessLogRepository;

    public Mono<AccessLog> insert(AccessLog log) {
        return accessLogRepository.save(log);
    }

    public Flux<AccessLog> findPageBySearchText(LocalDate fromDate, LocalDate toDate, String keyword, String mySiteId) {
        return accessLogRepository.findPageBySearchText(fromDate, toDate, keyword, mySiteId);
    }
}
