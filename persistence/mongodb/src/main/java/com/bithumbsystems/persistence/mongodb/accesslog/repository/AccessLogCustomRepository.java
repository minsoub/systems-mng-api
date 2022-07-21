package com.bithumbsystems.persistence.mongodb.accesslog.repository;

import com.bithumbsystems.persistence.mongodb.accesslog.model.entity.AccessLog;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface AccessLogCustomRepository {
    Flux<AccessLog> findPageBySearchText(LocalDate fromDate, LocalDate toDate, String keyword, String mySiteId);
}
