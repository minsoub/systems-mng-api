package com.bithumbsystems.management.api.v1.audit.controller;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.audit.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

//  /**
//   * Gets one.
//   *
//   * @param auditLogSearchRequest the audit log search request
//   * @return the one
//   */
//  @GetMapping("/audit/logs")
//  @Operation(summary = "감사로그 조회" , description = "운영 관리> 감사로그 조회", tags = "운영 관리> 감사로그 조회")
//  public ResponseEntity<Mono<?>> getPage(@QueryParam AuditLogSearchRequest auditLogSearchRequest) {
//    return ResponseEntity.ok().body(auditLogService.getPage(auditLogSearchRequest)
//        .map(SingleResponse::new));
//  }

  @GetMapping("/audit/logs")
  @Operation(summary = "감사 로그 관리 - 감사 로그 조회" , description = "감사 로그 관리 목록 정보를 조회합니다.", tags = "사이트 관리 > 감사 로그 관리 > 검색")
  public ResponseEntity<Mono<?>> getAuditLog(@Parameter(name = "fromDate", description = "fromDate 이전 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String fromDate,
      @Parameter(name = "toDate", description = "toDate 다음 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String toDate,
      @Parameter(name = "keyword", description = "로그 관련 키워드 조건 검색") @RequestParam(required = false) String keyword,
      @Parameter(hidden = true) @CurrentUser Account account) {

    LocalDate nFromDate = LocalDate.parse(fromDate);
    LocalDate nToDate = LocalDate.parse(toDate);

    nToDate = nToDate.plusDays(1);
    return ResponseEntity.ok().body(auditLogService.findAuditServiceLog(nFromDate, nToDate, keyword, account.getMySiteId())
        .map(SingleResponse::new));
  }

  /**
   * 감사 로그 상세 정보를 조회한다.
   * @param id
   * @param account
   * @return
   */
  @GetMapping("/audit/logs/{id}")
  @Operation(summary = "감사 로그 관리 - 감사 상세 로그 조회" , description = "감사 로그 관리 상세 정보를 조회합니다.", tags = "사이트 관리 > 감사 로그 관리 > id 검색")
  public ResponseEntity<Mono<?>> getDetailAuditLog(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
  @PathVariable("id") String id,
      @Parameter(hidden = true) @CurrentUser Account account) {

    return ResponseEntity.ok().body(auditLogService.findAuditServiceLogDetail(id)
        .map(SingleResponse::new));
  }

  @GetMapping(value = "/audit/logs/excel/export", produces = APPLICATION_OCTET_STREAM_VALUE)
  @Operation(summary = "감사 로그 관리 - 엑셀 다운로드", description = "감사 로그 관리: 엑셀 다운로드", tags = "감사 로그 관리")
  public Mono<ResponseEntity<?>> downloadExcel(@Parameter(name = "fromDate", description = "fromDate 이전 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String fromDate,
      @Parameter(name = "toDate", description = "toDate 다음 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String toDate,
      @Parameter(name = "keyword", description = "로그 관련 키워드 조건 검색") @RequestParam(required = false) String keyword,
      @Parameter(name = "reason", description = "다운로드 사유") @RequestParam(required = true) String reason,
      @Parameter(hidden = true) @CurrentUser Account account) {
    LocalDate nFromDate = LocalDate.parse(fromDate);
    LocalDate nToDate = LocalDate.parse(toDate);

    return auditLogService.downloadExcel(nFromDate, nToDate, keyword, reason, account)
        .flatMap(inputStream -> {
          HttpHeaders headers = new HttpHeaders();
          String fileName = "감사로그.xlsx";
          headers.setContentDispositionFormData(fileName, fileName);
          return Mono.just(ResponseEntity.ok().cacheControl(CacheControl.noCache())
              .headers(headers)
              .body(new InputStreamResource(inputStream)));
        });
  }

}
