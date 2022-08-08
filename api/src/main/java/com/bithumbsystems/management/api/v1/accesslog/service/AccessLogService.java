package com.bithumbsystems.management.api.v1.accesslog.service;

import com.bithumbsystems.management.api.v1.accesslog.mapper.AccessLogMapper;
import com.bithumbsystems.management.api.v1.accesslog.request.AccessLogRequest;
import com.bithumbsystems.management.api.v1.accesslog.request.AccessLogResponse;
import com.bithumbsystems.persistence.mongodb.accesslog.service.AccessLogDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessLogService {
    private final AccessLogDomainService accessLogDomainService;

    public Mono<List<AccessLogResponse>> findAccessServiceLog(LocalDate fromDate, LocalDate toDate, String keyword, String mySiteId) {
        return accessLogDomainService.findPageBySearchText(
                        fromDate,
                        toDate, keyword, mySiteId)
                .map(AccessLogMapper.INSTANCE::accessLogResponse)
                .collectSortedList(Comparator.comparing(AccessLogResponse::getCreateDate).reversed());
                //.switchIfEmpty(Mono.error(new AuditLogException(ErrorCode.NOT_FOUND_CONTENT)));
    }
}
