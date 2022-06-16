package com.bithumbsystems.management.api.v1.site.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.site.model.request.SiteRegisterRequest;
import com.bithumbsystems.management.api.v1.site.service.SiteService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The type Site controller.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class SiteController {

  private final SiteService siteService;

  /**
   * List response entity.
   *
   * @param searchText the search text
   * @param isUse      the is use
   * @return the response entity
   */
  @GetMapping("/sites")
  @Operation(summary = "사이트 목록 조회", description = "통합 시스템 관리: 사이트 목록")
  public ResponseEntity<Mono<?>> list(
      @RequestParam(required = false, defaultValue = "") String searchText
      , @RequestParam(required = false) Boolean isUse) {
    return ResponseEntity.ok().body(
        siteService.findBySearchText(searchText, isUse).map(MultiResponse::new)
    );
  }

  /**
   * Create response entity.
   *
   * @param siteRegisterRequest the site register request
   * @param account             the account
   * @return the response entity
   */
  @PostMapping("/site")
  @Operation(summary = "사이트 생성", description = "통합 시스템 관리: 사이트 생성")
  public ResponseEntity<Mono<?>> create(@RequestBody SiteRegisterRequest siteRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(siteService.create(siteRegisterRequest, account)
        .map(SingleResponse::new));
  }

  /**
   * Gets one.
   *
   * @param siteId the site id
   * @return the one
   */
  @GetMapping("/site/{siteId}")
  @Operation(summary = "사이트 조회", description = "통합 시스템 관리: 사이트 단건 조회")
  public ResponseEntity<Mono<?>> getOne(@PathVariable String siteId) {
    return ResponseEntity.ok().body(siteService.getOne(siteId)
        .map(SingleResponse::new));
  }

  /**
   * Update response entity.
   *
   * @param siteId              the site id
   * @param siteRegisterRequest the site register request
   * @param account             the account
   * @return the response entity
   */
  @PutMapping("/site/{siteId}")
  @Operation(summary = "사이트 수정", description = "통합 시스템 관리: 사이트 단건 수정")
  public ResponseEntity<Mono<?>> update(@PathVariable String siteId,
      @RequestBody SiteRegisterRequest siteRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        siteService.update(siteId, siteRegisterRequest, account)
            .map(SingleResponse::new));
  }
}
