package com.bithumbsystems.management.api.v1.account.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.account.model.request.AccessRegisterRequest;
import com.bithumbsystems.management.api.v1.account.model.request.AccountMngRegisterRequest;
import com.bithumbsystems.management.api.v1.account.model.request.AccountMngUpdateRequest;
import com.bithumbsystems.management.api.v1.account.model.request.AccountRegisterRequest;
import com.bithumbsystems.management.api.v1.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
   * @param isUse      the is use
   * @return the response entity
   */
  @GetMapping("/accounts")
  @Operation(summary = "계정 검색", description = "계정 검색")
  public ResponseEntity<Mono<?>> accountsSearch(
      @RequestParam(required = false, defaultValue = "") String searchText,
      @RequestParam(required = false) Boolean isUse) {
    return ResponseEntity.ok().body(accountService.search(searchText, isUse)
        .map(MultiResponse::new));
  }

  /**
   * Create response entity.
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return the response entity
   */
  @PostMapping("/access")
  @Operation(summary = "접근 계정 생성", description = "사이트관리 > 사용자 접근관리 : 계정생성")
  public ResponseEntity<Mono<?>> create(@RequestBody AccessRegisterRequest accountRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok()
        .body(accountService.createAccessAccount(accountRegisterRequest, account)
            .map(SingleResponse::new));
  }


  /**
   * Access list response entity.
   *
   * @return the response entity
   */
  @GetMapping("/access")
  @Operation(summary = "접근 계정조회", description = "사이트관리 > 사용자 접근관리 : 계정조회")
  public ResponseEntity<Mono<?>> accessList() {
    return ResponseEntity.ok().body(accountService.allList()
        .map(MultiResponse::new));
  }


  /**
   * 통합 시스템 관리자가 계정을 등록
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return the response entity
   */
  @PostMapping("/account")
  @Operation(summary = "통합 시스템 관리자가 계정을 등록", description = "통합 시스템 관리자가 계정을 등록")
  public ResponseEntity<Mono<?>> adminAccountCreate(
      @RequestBody AccountRegisterRequest accountRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(accountService.createAccount(accountRegisterRequest, account)
        .map(SingleResponse::new));
  }

  /**
   * 통합 시스템 관리자가 계정 정보를 수정한다.
   *
   * @param adminAccountId         the admin account id
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return response entity
   */
  @PutMapping("/account/{adminAccountId}")
  @Operation(summary = "통합 시스템 관리자가 계정을 수정", description = "통합 시스템 관리자가 계정을 수정")
  public ResponseEntity<Mono<?>> adminAccountUpdate(@PathVariable String adminAccountId,
      @RequestBody AccountRegisterRequest accountRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {

    return ResponseEntity.ok()
        .body(accountService.updateAccount(accountRegisterRequest, adminAccountId, account)
            .map(SingleResponse::new));
  }

  /**
   * Delete access response entity.
   *
   * @param adminAccountId the admin account id
   * @return the response entity
   */
  @DeleteMapping("/access/{adminAccountId}")
  @Operation(summary = "접근 계정삭제", description = "사이트관리 > 사용자 접근관리 : 계정삭제")
  public ResponseEntity<Mono<?>> deleteAccess(@PathVariable String adminAccountId) {
    return ResponseEntity.ok().body(accountService.deleteAccess(adminAccountId)
        .then(Mono.just(new SingleResponse<>())));
  }

  /**
   * 통합시스템 관리 - 계정관리 상세 조회
   *
   * @param adminAccountId the admin account id
   * @return response entity
   */
  @GetMapping("/account/{adminAccountId}")
  @Operation(summary = "계정조회", description = "통합시스템 > 계정관리 : 계정조회")
  public ResponseEntity<Mono<?>> accountDetail(@PathVariable String adminAccountId) {
    return ResponseEntity.ok().body(accountService.detailData(adminAccountId)
        .map(SingleResponse::new));
  }

  /**
   * 통합시스템 관리 - 계정정보 일괄 삭제
   *
   * @param adminAccountIdList the admin account id list
   * @param account            the account
   * @return response entity
   */
  @DeleteMapping("/account/{adminAccountIdList}")
  @Operation(summary = "계정삭제", description = "통합관리 > 계정관리 : 계정삭제")
  public ResponseEntity<Mono<?>> deleteList(@PathVariable String adminAccountIdList,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(accountService.deleteAccountList(adminAccountIdList, account)
        .map(SingleResponse::new));
  }

  /**
   * Create response entity.
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return the response entity
   */
  @PostMapping("/accountmng")
  @Operation(summary = "계정 등록", description = "통합 시스템 관리 > 계정관리 : 계정 등록")
  public ResponseEntity<Mono<?>> createMng(
      @RequestBody AccountMngRegisterRequest accountRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(accountService.createMngAccount(accountRegisterRequest, account)
        .map(SingleResponse::new));
  }

  /**
   * 통합관리 - 계정정보 상세조회
   *
   * @param adminAccountId the admin account id
   * @return response entity
   */
  @GetMapping("/accountmng/{adminAccountId}")
  @Operation(summary = "계정 상세조회", description = "통합 시스템 관리 > 계정관리 : 계정 상세조회")
  public ResponseEntity<Mono<?>> detailMng(@PathVariable String adminAccountId) {
    return ResponseEntity.ok().body(accountService.findByMngAccountId(adminAccountId)
        .map(SingleResponse::new));
  }

  /**
   * 통합관리 - 계정정보 수정
   *
   * @param adminAccountId         the admin account id
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return response entity
   */
  @PutMapping("/accountmng/{adminAccountId}")
  @Operation(summary = "계정 수정", description = "통합 시스템 관리 > 계정관리 : 계정 수정")
  public ResponseEntity<Mono<?>> updateMng(@PathVariable String adminAccountId,
      @RequestBody AccountMngUpdateRequest accountRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {

    return ResponseEntity.ok()
        .body(accountService.updateMngAccount(accountRegisterRequest, adminAccountId, account)
            .map(SingleResponse::new));
  }

  /**
   * 통합관리 - 계정정보 일괄 삭제
   *
   * @param adminAccountIdList the admin account id list
   * @param account            the account
   * @return response entity
   */
  @DeleteMapping("/accountmng/{adminAccountIdList}")
  @Operation(summary = "계정 삭제", description = "통합 시스템 관리 > 계정관리 : 계정 삭제")
  public ResponseEntity<Mono<?>> deleteMngList(@PathVariable String adminAccountIdList,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(accountService.deleteMngAccountList(adminAccountIdList, account)
        .map(SingleResponse::new));
  }
}
