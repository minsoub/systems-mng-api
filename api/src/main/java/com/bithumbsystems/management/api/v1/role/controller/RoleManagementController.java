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
import org.springframework.web.bind.annotation.DeleteMapping;
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

import java.util.List;

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
  @Operation(summary = "ROLE 중복체크", description = "Role 관리 : 등록시 중복체크", tags = "통합 시스템 관리> 권한 관리")
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
  @Operation(summary = "ROLE 리스트 검색", description = "Role 관리 : 리스트 검색 조회", tags = "통합 시스템 관리> 권한 관리")
  public ResponseEntity<Mono<?>> getRoleManagements(
      @RequestParam String siteId,
      @RequestParam(required = false, defaultValue = "true") Boolean isUse,
      @RequestParam(required = false) String type) {
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
  @Operation(summary = "ROLE 등록", description = "Role 관리 : 등록", tags = "통합 시스템 관리> 권한 관리")
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
  @Operation(summary = "ROLE 조회", description = "Role 관리 : ROLE 단건 조회", tags = "통합 시스템 관리> 권한 관리")
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
  @Operation(summary = "ROLE 수정", description = "Role 관리 : ROLE 단건 수정", tags = "통합 시스템 관리> 권한 관리")
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
  @Operation(summary = "ROLE 사용자 매핑 조회", description = "Role 관리 : 사용자 매핑 조회", tags = "통합 시스템 관리> 권한 관리")
  public ResponseEntity<Mono<?>> getAccountInRoleManagement(@PathVariable String roleManagementId) {
    return ResponseEntity.ok().body(
        roleManagementService.getAccessUserList(roleManagementId)  // getOne(roleManagementId)
            .map(MultiResponse::new));
  }
  @DeleteMapping("/role/{roleManagementId}/accounts/{accountId}")
  @Operation(summary = "ROLE 사용자 매핑 삭제", description = "Role 관리 : 사용자 매핑 삭제", tags = "통합 시스템 관리> 권한 관리")
  public ResponseEntity<Mono<?>> deleteRoleManagementAccount(@PathVariable String roleManagementId,
                                                             @PathVariable String accountId, @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
            roleManagementService.deleteAccessUserRole(roleManagementId, accountId, account)  // getOne(roleManagementId)
                    .map(SingleResponse::new));
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
  @Operation(summary = "ROLE 사용자 매핑", description = "Role 관리 : 사용자 매핑", tags = "통합 시스템 관리> 권한 관리")
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
  @GetMapping("/role/{roleManagementId}/sites/{siteId}")
  @Operation(summary = "Role 메뉴 프로그램 조회", description = "권한 관리: 메뉴 프로그램 조회", tags = "통합 시스템 관리> 권한 관리")
  public ResponseEntity<Mono<?>> getResources(@PathVariable String roleManagementId, @PathVariable String siteId) {
    return ResponseEntity.ok().body(
        roleManagementService.getResources(roleManagementId, siteId)
            .map(SingleResponse::new));
  }

  /**
   * Role에 해당되는 메뉴를 불러 온다
   *
   * @param roleManagementId the role management id
   * @return the response entity
   */
  @GetMapping("/role/{roleManagementId}/resources")
  @Operation(summary = "Role 메뉴 프로그램 조회", description = "권한 관리: 메뉴 프로그램 조회", tags = "사이트 관리> 권한 관리")
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
  @Operation(summary = "Role 메뉴 프로그램 매핑", description = "권한 관리: 메뉴 프로그램 매핑", tags = "통합 시스템 관리> 권한 관리")
  public ResponseEntity<Mono<?>> mappingMenu(
      @RequestBody RoleResourceRequest roleResourceRequests,
      @PathVariable String roleManagementId,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        roleManagementService.mappingResources(roleResourceRequests, roleManagementId, account)
            .map(SingleResponse::new)
    );
  }

}
