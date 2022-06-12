package com.bithumbsystems.management.api.v1.account.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.account.model.request.AccountMngRegisterRequest;
import com.bithumbsystems.management.api.v1.account.model.request.AccountMngUpdateRequest;
import com.bithumbsystems.management.api.v1.account.model.request.AccountRegisterRequest;
import com.bithumbsystems.management.api.v1.account.service.AccountService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * The type Account controller.
 */
@RequiredArgsConstructor
@RestController
public class AccountController {

  private final AccountService accountService;

  /**
   * Accounts search response entity.
   *
   * @param searchText the search text
   * @return the response entity
   */
  @GetMapping("/accounts")
  public ResponseEntity<Mono<?>> accountsSearch(@RequestParam(required = false, defaultValue = "") String searchText , @RequestParam(required = false) Boolean isUse) {
    return ResponseEntity.ok().body(accountService.search(searchText, isUse)
        .map(accountSearchResponses -> new MultiResponse(accountSearchResponses)));
  }

  /**
   * Create response entity.
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return the response entity
   */
  @PostMapping("/account")
  public ResponseEntity<Mono<?>> create(@RequestBody AccountRegisterRequest accountRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(accountService.createAccessAccount(accountRegisterRequest, account)
        .map(accountResponse -> new SingleResponse(accountResponse)));
  }


  /**
   * Access list response entity.
   *
   * @return the response entity
   */
  @GetMapping("/access")
  public ResponseEntity<Mono<?>> accessList() {
    return ResponseEntity.ok().body(accountService.allList()
        .map(accountResponse -> new MultiResponse(accountResponse)));
  }

  /**
   * Delete access response entity.
   *
   * @param adminAccountId the admin account id
   * @return the response entity
   */
  @DeleteMapping("/access/{adminAccountId}")
  public ResponseEntity<Mono<?>> deleteAccess(@PathVariable String adminAccountId) {
    return ResponseEntity.ok().body(accountService.deleteAccess(adminAccountId)
        .then(Mono.just(new SingleResponse())));
  }
  /**
   * 통합시스템 관리 - 계정관리 상세 조회
   * @param adminAccountId
   * @return
   */
  @GetMapping("/account/{adminAccountId}")
  public ResponseEntity<Mono<?>> accountDetail(@PathVariable String adminAccountId) {
    return ResponseEntity.ok().body(accountService.detailData(adminAccountId)
            .then(Mono.just(new SingleResponse())));
  }

  /**
   * Create response entity.
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return the response entity
   */
  @PostMapping("/accountmng")
  public ResponseEntity<Mono<?>> createMng(@RequestBody AccountMngRegisterRequest accountRegisterRequest,
                                        @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(accountService.createMngAccount(accountRegisterRequest, account)
            .map(accountResponse -> new SingleResponse(accountResponse)));
  }

  /**
   * 통합관리 - 계정정보 상세조회
   * @param adminAccountId
   * @return
   */
  @GetMapping("/accountmng/{adminAccountId}")
  public ResponseEntity<Mono<?>> detailMng(@PathVariable String adminAccountId) {
    return ResponseEntity.ok().body(accountService.findByMngAccountId(adminAccountId)
            .map(accountResponse -> new SingleResponse(accountResponse)));
  }

  /**
   * 통합관리 - 계정정보 수정
   * @param adminAccountId
   * @param accountRegisterRequest
   * @param account
   * @return
   */
  @PutMapping("/accountmng/{adminAccountId}")
  public ResponseEntity<Mono<?>> updateMng(@PathVariable String adminAccountId, @RequestBody AccountMngUpdateRequest accountRegisterRequest,
                                           @Parameter(hidden = true) @CurrentUser Account account) {

    return ResponseEntity.ok().body(accountService.updateMngAccount(accountRegisterRequest, adminAccountId, account)
            .map(accountResponse -> new SingleResponse(accountResponse)));
  }
  @DeleteMapping("/accountmng/{adminAccountIdList}")
  public ResponseEntity<Mono<?>> deleteMngList(@PathVariable String adminAccountIdList, @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(accountService.deleteMngAccountList(adminAccountIdList, account)
            .map(result -> new SingleResponse(result)));
  }
}
