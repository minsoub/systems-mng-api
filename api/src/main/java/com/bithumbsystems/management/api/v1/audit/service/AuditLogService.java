package com.bithumbsystems.management.api.v1.audit.service;

import com.bithumbsystems.management.api.v1.audit.model.mapper.AuditLogMapper;
import com.bithumbsystems.management.api.v1.audit.model.request.AuditLogSearchRequest;
import com.bithumbsystems.management.api.v1.audit.model.response.AuditLogSearchResponse;
import com.bithumbsystems.persistence.mongodb.audit.service.AuditLogDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuditLogService {

  private final AuditLogDomainService auditLogDomainService;

  public Mono<Page<AuditLogSearchResponse>> getPage(AuditLogSearchRequest auditLogSearchRequest) {
    final var pageRequest = PageRequest.of(auditLogSearchRequest.getPage(), auditLogSearchRequest.getSize());
    return auditLogDomainService.findPageBySearchText(auditLogSearchRequest.getSearchText(),
            auditLogSearchRequest.getStartDate(),
            auditLogSearchRequest.getEndDate(),
            PageRequest.of(auditLogSearchRequest.getPage(), auditLogSearchRequest.getSize()))
        .map(AuditLogMapper.INSTANCE::auditLogToResponse)
        .collectList()
        .zipWith(auditLogDomainService.countBySearchText(auditLogSearchRequest.getSearchText(),
            auditLogSearchRequest.getStartDate(),
            auditLogSearchRequest.getEndDate())
            .map(c -> c))
        .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
  }
}
