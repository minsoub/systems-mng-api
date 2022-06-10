package com.bithumbsystems.management.api.v1.role.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.role.model.mapper.RoleMapper;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementRegisterRequest;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementUpdateRequest;
import com.bithumbsystems.management.api.v1.role.service.RoleManagementService;
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
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class RoleManagementController {

  private final RoleManagementService roleManagementService;

  @GetMapping("/role/{roleId}/check")
  public ResponseEntity<Mono<?>> checkDuplicate(@PathVariable String roleId) {
    return ResponseEntity.ok().body(roleManagementService.checkDuplicate(roleId)
        .map(m -> new SingleResponse(m)));
  }

  @GetMapping("/roles")
  public ResponseEntity<Mono<?>> getRoleManagements(@RequestParam String siteId,
      @RequestParam Boolean isUse) {
      return ResponseEntity.ok().body(roleManagementService.getRoleManagements(siteId, isUse)
          .map(roleManagementResponses -> new MultiResponse(roleManagementResponses)));
  }

  @PostMapping("/role")
  public ResponseEntity<Mono<?>> create(@RequestBody Mono<RoleManagementRegisterRequest> registerRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        roleManagementService.create(registerRequest, account)
        .map(r ->
            new SingleResponse(RoleMapper.INSTANCE.roleManagementToResponse(r))
        )
    );
  }

  @GetMapping("/role/{roleManagementId}")
  public ResponseEntity<Mono<?>> getRoleManagement(@PathVariable String roleManagementId) {
    return ResponseEntity.ok().body(
        roleManagementService.getOne(roleManagementId)
            .map(r ->
                new SingleResponse(RoleMapper.INSTANCE.roleManagementToResponse(r))
            )
    );
  }

  @PutMapping("/role/{roleManagementId}")
  public ResponseEntity<Mono<?>> updateRoleManagement(@RequestBody Mono<RoleManagementUpdateRequest> updateRequest,
      @PathVariable String roleManagementId,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        roleManagementService.update(updateRequest, account, roleManagementId)
            .map(r ->
                new SingleResponse(RoleMapper.INSTANCE.roleManagementToResponse(r))
            )
    );
  }

  @GetMapping("/role/{roleManagementId}/accounts")
  public ResponseEntity<Mono<?>> getAccountInRoleManagement(@PathVariable String roleManagementId) {
    return ResponseEntity.ok().body(
        roleManagementService.getOne(roleManagementId)
            .map(r ->
                new SingleResponse(RoleMapper.INSTANCE.roleManagementToResponse(r))
            )
    );
  }

  //
  @PutMapping("/role/{roleManagementId}/accounts")
  public ResponseEntity<Mono<?>> mappingAccounts(@RequestBody String[] accounts,
      @PathVariable String roleManagementId,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        roleManagementService.mappingAccounts(accounts, roleManagementId, account)
            .map(r -> new SingleResponse(r))
    );
  }
}
