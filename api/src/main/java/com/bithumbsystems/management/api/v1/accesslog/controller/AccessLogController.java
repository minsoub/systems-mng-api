package com.bithumbsystems.management.api.v1.accesslog.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.v1.accesslog.service.AccessLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@Tag(name = "개인정보 취급자 접속 로그 관리", description = "개인정보 취급자 접속 로그 관리 API")
public class AccessLogController {
    private final AccessLogService accessLogService;

    @GetMapping("/access/logs")
    @Operation(summary = "개인정보 취급자 접속로그" , description = "개인정보 취급자 접속로그 목록 정보를 조회합니다.", tags = "사이트 관리 > 개인정보 취급자 접속로그 > 검색")
    public ResponseEntity<Mono<?>> getAuditLog(@Parameter(name = "fromDate", description = "fromDate 이전 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String fromDate,
                                               @Parameter(name = "toDate", description = "toDate 다음 날짜(* 날짜 입력 형식 2022-02-22)", required = true) @RequestParam(required = false) String toDate,
                                               @Parameter(name = "keyword", description = "로그 관련 키워드 조건 검색") @RequestParam(required = false) String keyword,
                                               @Parameter(hidden = true) @CurrentUser Account account) {

        LocalDate nFromDate = LocalDate.parse(fromDate);
        LocalDate nToDate = LocalDate.parse(toDate);

        nToDate = nToDate.plusDays(1);
        return ResponseEntity.ok().body(accessLogService.findAccessServiceLog(nFromDate, nToDate, keyword, account.getMySiteId())
                .map(MultiResponse::new));
    }
}
