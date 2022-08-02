package com.bithumbsystems.management.api.v1.mail.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.mail.model.request.SiteMailRequest;
import com.bithumbsystems.management.api.v1.mail.model.request.SiteMailListRequest;
import com.bithumbsystems.management.api.v1.mail.service.SiteMailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class SiteMailController {

  private final SiteMailService siteMailService;

  /**
   * 메일 연동 정보 조회
   *
   * @param request the mail request
   * @return response entity
   */
  @GetMapping
  @Operation(summary = "메일 연동 정보 조회", description = "메일 연동 정보 조회", tags = "통합 관리 > 연동 관리 > 메일 관리")
  public ResponseEntity<?> getMailList(SiteMailListRequest request) {
    return ResponseEntity.ok()
        .body(siteMailService.getSiteMailList(request).map(MultiResponse::new));
  }

  /**
   * 메일 연동 정보 상세조회
   *
   * @param id the mail id
   * @return response entity
   */
  @GetMapping("/{siteMailId}")
  @Operation(summary = "메일 연동 정보 상세조회", description = "메일 연동 정보 상세조회", tags = "통합 관리 > 연동 관리 > 메일 관리")
  public ResponseEntity<?> getSiteMail(@PathVariable("siteMailId") String id) {
    return ResponseEntity.ok().body(siteMailService.getSiteMail(id).map(SingleResponse::new));
  }

  /**
   * 메일 연동 정보 등록
   *
   * @param request the mail register request
   * @param account the account
   * @return response entity
   */
  @PostMapping
  @Operation(summary = "메일 연동 정보 등록", description = "메일 연동 정보 등록", tags = "통합 관리 > 연동 관리 > 메일 관리")
  public ResponseEntity<?> createSiteMail(@RequestBody SiteMailRequest request,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok()
        .body(siteMailService.createSiteMail(request, account).map(SingleResponse::new));
  }

  /**
   * 메일 연동 정보 수정
   *
   * @param id      the mail id
   * @param request the mail update request
   * @param account the account
   * @return response entity
   */
  @PutMapping("/{siteMailId}")
  @Operation(summary = "메일 연동 정보 수정", description = "메일 연동 정보 수정", tags = "통합 관리 > 연동 관리 > 메일 관리")
  public ResponseEntity<?> updateSiteMail(@PathVariable("siteMailId") String id,
      @RequestBody SiteMailRequest request,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok()
        .body(siteMailService.updateSiteMail(id, request, account).map(SingleResponse::new));
  }


}
