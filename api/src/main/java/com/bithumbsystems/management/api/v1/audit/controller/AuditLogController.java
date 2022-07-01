package com.bithumbsystems.management.api.v1.audit.controller;

import com.bithumbsystems.management.api.core.config.resolver.QueryParam;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.audit.model.request.AuditLogSearchRequest;
import com.bithumbsystems.management.api.v1.audit.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The type Menu controller.
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuditLogController {

  private final AuditLogService auditLogService;

  /**
   * Gets one.
   *
   * @param auditLogSearchRequest the audit log search request
   * @return the one
   */
  @GetMapping("/audit/logs")
  @Operation(summary = "감사로그 조회" , description = "운영 관리> 감사로그 조회", tags = "운영 관리> 감사로그 조회")
  public ResponseEntity<Mono<?>> getPage(@QueryParam AuditLogSearchRequest auditLogSearchRequest) {
    return ResponseEntity.ok().body(auditLogService.getPage(auditLogSearchRequest)
        .map(SingleResponse::new));
  }

}
