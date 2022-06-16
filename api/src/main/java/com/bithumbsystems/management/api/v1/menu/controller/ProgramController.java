package com.bithumbsystems.management.api.v1.menu.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.menu.model.request.ProgramRegisterRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.ProgramUpdateRequest;
import com.bithumbsystems.management.api.v1.menu.service.ProgramService;
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
  public ResponseEntity<Mono<?>> getList(@PathVariable String siteId,
      @RequestParam(required = false, defaultValue = "") String searchText,
      @RequestParam Boolean isUse) {
    return ResponseEntity.ok().body(programService.getList(siteId, searchText, isUse)
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
  public ResponseEntity<Mono<?>> delete(@PathVariable String siteId,
      @PathVariable String programId) {
    return ResponseEntity.ok().body(programService.delete(siteId, programId)
        .map(SingleResponse::new));
  }

}
