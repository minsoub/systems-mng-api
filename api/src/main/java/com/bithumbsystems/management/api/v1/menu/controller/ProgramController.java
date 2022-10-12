package com.bithumbsystems.management.api.v1.menu.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.menu.model.request.ProgramRegisterRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.ProgramUpdateRequest;
import com.bithumbsystems.management.api.v1.menu.service.ProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The type Program controller.
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProgramController {

  private final ProgramService programService;

  /**
   * Gets list.
   *
   * @param siteId     the site id
   * @param searchText the search text
   * @param isUse      the is use
   * @return the list
   */
  @GetMapping("/site/{siteId}/programs")
  @Operation(summary = "프로그램 목록" , description = "통합 시스템 관리> 프로그램 관리: 목록", tags = "통합 시스템 관리> 프로그램 관리")
  public ResponseEntity<Mono<?>> getList(@PathVariable String siteId,
      @RequestParam(required = false, defaultValue = "true") Boolean isWhole,
      @RequestParam(required = false, defaultValue = "") String searchText,
      @RequestParam(required = false) Boolean isUse) {
    return ResponseEntity.ok().body(programService.getList(siteId, searchText, isUse, isWhole)
        .map(SingleResponse::new));
  }

  /**
   * Create response entity.
   *
   * @param siteId                 the site id
   * @param programRegisterRequest the program register request
   * @param account                the account
   * @return the response entity
   */
  @PostMapping("/site/{siteId}/program")
  @Operation(summary = "프로그램 생성" , description = "통합 시스템 관리> 프로그램 관리: 생성", tags = "통합 시스템 관리> 프로그램 관리")
  public ResponseEntity<Mono<?>> create(@PathVariable String siteId,
      @RequestBody ProgramRegisterRequest programRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(programService.create(siteId, programRegisterRequest, account)
        .map(SingleResponse::new));
  }

  /**
   * Gets one.
   *
   * @param siteId    the site id
   * @param programId the program id
   * @return the one
   */
  @GetMapping("/site/{siteId}/program/{programId}")
  @Operation(summary = "프로그램 조회" , description = "통합 시스템 관리> 프로그램 관리: 조회", tags = "통합 시스템 관리> 프로그램 관리")
  public ResponseEntity<Mono<?>> getOne(@PathVariable String siteId,
      @PathVariable String programId) {
    return ResponseEntity.ok().body(programService.getOne(siteId, programId)
        .map(SingleResponse::new));
  }

  /**
   * Update response entity.
   *
   * @param siteId               the site id
   * @param programId            the program id
   * @param programUpdateRequest the program update request
   * @param account              the account
   * @return the response entity
   */
  @PutMapping("/site/{siteId}/program/{programId}")
  @Operation(summary = "프로그램 수정" , description = "통합 시스템 관리> 프로그램 관리: 수정", tags = "통합 시스템 관리> 프로그램 관리")
  public ResponseEntity<Mono<?>> update(@PathVariable String siteId, @PathVariable String programId,
      @RequestBody ProgramUpdateRequest programUpdateRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok()
        .body(programService.update(siteId, programId, programUpdateRequest, account)
            .map(SingleResponse::new));
  }

  /**
   * Delete response entity.
   *
   * @param siteId    the site id
   * @param programId the program id
   * @return the response entity
   */
  @DeleteMapping("/site/{siteId}/program/{programId}")
  @Operation(summary = "프로그램 삭제" , description = "통합 시스템 관리> 프로그램 관리: 삭제", tags = "통합 시스템 관리> 프로그램 관리")
  public ResponseEntity<Mono<?>> delete(@PathVariable String siteId,
      @PathVariable String programId) {
    return ResponseEntity.ok().body(programService.delete(siteId, programId)
        .map(SingleResponse::new));
  }

}
