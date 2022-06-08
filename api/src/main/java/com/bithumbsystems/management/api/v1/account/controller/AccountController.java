package com.bithumbsystems.management.api.v1.account.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.account.model.request.AccountRegisterRequest;
import com.bithumbsystems.management.api.v1.account.service.AccountService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
   * @return the response entity
   */
  @GetMapping("/accounts")
  public ResponseEntity<Mono<?>> accountsSearch(@RequestParam String searchText) {
    return ResponseEntity.ok().body(accountService.search(searchText)
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
}
