package com.bithumbsystems.management.api.v1.accessip.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.accessip.model.request.AccessIpRequest;
import com.bithumbsystems.management.api.v1.accessip.service.AccessIpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/access/ip")
@Profile("local")
public class AccessIpController {

  private final AccessIpService accessIpService;

  /**
   * 관리자 접속 허가 아이피 조회
   *
   * @param siteId the site id
   * @param name the name
   * @param email the email
   * @return response entity
   */
  @Operation(summary = "관리자 접속 허가 아이피 조회", description = "관리자 접속 허가 아이피 조회", tags = "통합 관리 > 접근 IP 관리")
  @GetMapping("/{siteId}")
  public ResponseEntity<?> getAccessIpList(@PathVariable(value = "siteId", required = false) String siteId,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String email) {
    return ResponseEntity.ok().body(accessIpService.getAccessIpList(siteId, name, email).map(MultiResponse::new));
  }

  /**
   * 관리자 접속 허가 아이피리스트 살세조회
   *
   * @param siteId the accessIp id
   * @param adminAccountId the admin account id
   * @return response entity
   */
  @Operation(summary = "관리자 접속 허가 아이피리스트 살세조회", description = "관리자 접속 허가 아이피리스트 살세조회", tags = "통합 관리 > 접근 IP 관리")
  @GetMapping("/{siteId}/{adminAccountId}")
  public ResponseEntity<?> getAccessIp(@PathVariable("siteId") String siteId,
      @PathVariable("adminAccountId") String adminAccountId) {
    return ResponseEntity.ok().body(accessIpService.getAccessIp(siteId, adminAccountId).map(SingleResponse::new));
  }

  /**
   * 관리자 접속 허가 아이피 삭제
   *
   * @param id the accessIp id
   * @param account the account
   * @return response entity
   */
  @Operation(summary = "관리자 접속 허가 아이피 삭제", description = "관리자 접속 허가 아이피 삭제", tags = "통합 관리 > 접근 IP 관리")
  @DeleteMapping("/{accessIpId}")
  public ResponseEntity<?> updateAccessIp(@PathVariable("accessIpId") String id,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(accessIpService.deleteAccount(id, account).map(SingleResponse::new));
  }

  /**
   * 관리자 접속 허가 아이피 등록
   *
   * @param request the accessIp request
   * @param account the account
   * @return response entity
   */
  @Operation(summary = "관리자 접속 허가 아이피 등록", description = "관리자 접속 허가 아이피 삭제", tags = "통합 관리 > 접근 IP 관리")
  @PostMapping()
  public ResponseEntity<?> createAccessIp(@RequestBody AccessIpRequest request,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(accessIpService.createAccessIp(request, account).map(SingleResponse::new));
  }
}
