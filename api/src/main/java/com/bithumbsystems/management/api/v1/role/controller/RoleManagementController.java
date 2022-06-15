package com.bithumbsystems.management.api.v1.role.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.role.model.mapper.RoleMapper;
import com.bithumbsystems.management.api.v1.role.model.request.RoleAccountsRequest;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementRegisterRequest;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementUpdateRequest;
import com.bithumbsystems.management.api.v1.role.model.request.RoleResourceRequest;
import com.bithumbsystems.management.api.v1.role.service.RoleManagementService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Role management controller.
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class RoleManagementController {

  private final RoleManagementService roleManagementService;

  /**
   * Check duplicate response entity.
   *
   * @param roleId the role id
   * @return the response entity
   */
  @GetMapping("/role/{roleId}/check")
  public ResponseEntity<Mono<?>> checkDuplicate(@PathVariable String roleId) {
    return ResponseEntity.ok().body(roleManagementService.checkDuplicate(roleId)
        .map(SingleResponse::new));
  }

  /**
   * Role 관리 : 리스트 검색 조회
   *
   * @param siteId the site id
   * @param isUse  the is use
   * @param type   the type
   * @return role managements
   */
  @GetMapping("/roles")
  public ResponseEntity<Mono<?>> getRoleManagements(
      @RequestParam(required = false, defaultValue = "") String siteId,
      @RequestParam(required = false, defaultValue = "true") Boolean isUse,
      @RequestParam(required = false, defaultValue = "") String type) {
    return ResponseEntity.ok().body(roleManagementService.getRoleManagements(siteId, isUse, type)
        .map(MultiResponse::new));
  }

  /**
   * Create response entity.
   *
   * @param registerRequest the register request
   * @param account         the account
   * @return the response entity
   */
  @PostMapping("/role")
  public ResponseEntity<Mono<?>> create(
      @RequestBody Mono<RoleManagementRegisterRequest> registerRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        roleManagementService.create(registerRequest, account)
            .map(r ->
                new SingleResponse<>(RoleMapper.INSTANCE.roleManagementToResponse(r))
            )
    );
  }

  /**
   * Gets role management.
   *
   * @param roleManagementId the role management id
   * @return the role management
   */
  @GetMapping("/role/{roleManagementId}")
  public ResponseEntity<Mono<?>> getRoleManagement(@PathVariable String roleManagementId) {
    return ResponseEntity.ok().body(
        roleManagementService.getOne(roleManagementId)
            .map(r ->
                new SingleResponse<>(RoleMapper.INSTANCE.roleManagementToResponse(r))
            )
    );
  }

  /**
   * Update role management response entity.
   *
   * @param updateRequest    the update request
   * @param roleManagementId the role management id
   * @param account          the account
   * @return the response entity
   */
  @PutMapping("/role/{roleManagementId}")
  public ResponseEntity<Mono<?>> updateRoleManagement(
      @RequestBody Mono<RoleManagementUpdateRequest> updateRequest,
      @PathVariable String roleManagementId,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        roleManagementService.update(updateRequest, account, roleManagementId)
            .map(r ->
                new SingleResponse<>(RoleMapper.INSTANCE.roleManagementToResponse(r))
            )
    );
  }

  /**
   * 등록된 Role의 사용자 리스트를 가져온다.
   *
   * @param roleManagementId the role management id
   * @return account in role management
   */
  @GetMapping("/role/{roleManagementId}/accounts")
  public ResponseEntity<Mono<?>> getAccountInRoleManagement(@PathVariable String roleManagementId) {
    return ResponseEntity.ok().body(
        roleManagementService.getAccessUserList(roleManagementId)  // getOne(roleManagementId)
            .map(MultiResponse::new));
  }

  /**
   * Mapping accounts response entity.
   *
   * @param accounts         the accounts
   * @param roleManagementId the role management id
   * @param account          the account
   * @return the response entity
   */
  @PutMapping("/role/{roleManagementId}/accounts")
  public ResponseEntity<Mono<?>> mappingAccounts(@RequestBody RoleAccountsRequest accounts,
      @PathVariable String roleManagementId,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        roleManagementService.mappingAccounts(accounts.getAccounts(), roleManagementId, account)
            .map(SingleResponse::new)
    );
  }


  /**
   * Role에 해당되는 메뉴를 불러 온다
   *
   * @param roleManagementId the role management id
   * @return the response entity
   */
  @GetMapping("/role/{roleManagementId}/resources")
  @Operation(summary = "Role 전체 메뉴 조회", description = "Role에 해당되는 메뉴를 불러 온다")
  public ResponseEntity<Mono<?>> getResources(@PathVariable String roleManagementId) {
    return ResponseEntity.ok().body(
        roleManagementService.getResources(roleManagementId)
            .map(SingleResponse::new));
  }

  /**
   * Mapping accounts response entity.
   *
   * @param roleResourceRequests the role resource requests
   * @param roleManagementId     the role management id
   * @param account              the account
   * @return the response entity
   */
  @PostMapping("/role/{roleManagementId}/resources")
  public ResponseEntity<Mono<?>> mappingMenu(
      @RequestBody Flux<RoleResourceRequest> roleResourceRequests,
      @PathVariable String roleManagementId,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        roleManagementService.mappingResources(roleResourceRequests, roleManagementId, account)
            .map(SingleResponse::new)
    );
  }

}
