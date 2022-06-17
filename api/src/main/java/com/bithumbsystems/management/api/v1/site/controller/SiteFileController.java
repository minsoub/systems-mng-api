package com.bithumbsystems.management.api.v1.site.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.site.model.request.SiteFileInfoRequest;
import com.bithumbsystems.management.api.v1.site.service.SiteFileService;
import com.bithumbsystems.persistence.mongodb.site.model.enums.Extension;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The type Site controller.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class SiteFileController {

  private final SiteFileService siteFileService;

  /**
   * Gets file managements.
   *
   * @return the file managements
   */
  @GetMapping("/files/extensions")
  @Operation(summary = "확장자 조회" , description = "통합 관리> 업로드 관리: 허용 확장자 조회")
  public ResponseEntity<Mono<?>> getFileExtensions() {
    return ResponseEntity.ok().body(
        Mono.just(
            new MultiResponse<>(Arrays.stream(Extension.values()).collect(Collectors.toList()))));
  }

  /**
   * Gets file managements.
   *
   * @param isUse the is use
   * @return the file managements
   */
  @GetMapping("/files")
  @Operation(summary = "파일정보 목록" , description = "통합 관리> 업로드 관리: 파일정보 목록")
  public ResponseEntity<Mono<?>> getFileManagements(@RequestParam Boolean isUse) {
    return ResponseEntity.ok().body(
        siteFileService.getFileManagements(isUse)
            .map(SingleResponse::new));
  }

  /**
   * Gets file management.
   *
   * @param siteId the site id
   * @param isUse  the is use
   * @return the file management
   */
  @GetMapping("/site/{siteId}/file")
  @Operation(summary = "파일정보 조회" , description = "통합 관리> 업로드 관리: 파일정보 조회")
  public ResponseEntity<Mono<?>> getFileManagement(@PathVariable String siteId,
      @RequestParam Boolean isUse) {
    return ResponseEntity.ok().body(
        siteFileService.getFileManagement(siteId, isUse)
            .map(SingleResponse::new));
  }

  /**
   * Create file management mono.
   *
   * @param siteId              the site id
   * @param siteFileInfoRequest the site file info request
   * @param account             the account
   * @return the mono
   */
  @PostMapping("/site/{siteId}/file")
  @Operation(summary = "파일정보 생성" , description = "통합 관리> 업로드 관리: 파일정보 생성")
  public ResponseEntity<Mono<?>> createFileManagement(@PathVariable String siteId,
      @RequestBody SiteFileInfoRequest siteFileInfoRequest,
      @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        siteFileService.createFileManagement(siteId, siteFileInfoRequest, account)
            .map(SingleResponse::new));
  }

  /**
   * Update file management mono.
   *
   * @param siteId              the site id
   * @param siteFileInfoRequest the site file info request
   * @param account             the account
   * @return the mono
   */
  @PutMapping("/site/{siteId}/file")
  @Operation(summary = "파일정보 수정" , description = "통합 관리> 업로드 관리: 파일정보 수정")
  public ResponseEntity<Mono<?>> updateFileManagement(@PathVariable String siteId,
      @RequestBody SiteFileInfoRequest siteFileInfoRequest,
      @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        siteFileService.updateFileManagement(siteId, siteFileInfoRequest, account)
            .map(SingleResponse::new));
  }

}
