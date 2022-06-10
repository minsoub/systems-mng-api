package com.bithumbsystems.management.api.v1.role.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.management.api.v1.role.exception.RoleManagementException;
import com.bithumbsystems.management.api.v1.role.model.mapper.RoleMapper;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementRegisterRequest;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementUpdateRequest;
import com.bithumbsystems.management.api.v1.role.model.response.RoleManagementMappingResponse;
import com.bithumbsystems.management.api.v1.role.model.response.RoleManagementResponse;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccessDomainService;
import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import com.bithumbsystems.persistence.mongodb.role.service.RoleManagementDomainService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleManagementService {

  private final RoleManagementDomainService roleManagementDomainService;
  private final AdminAccessDomainService adminAccessDomainService;

  public Mono<Boolean> checkDuplicate(String roleId) {
    return roleManagementDomainService.isExist(roleId);
  }

  public Mono<RoleManagement> create(Mono<RoleManagementRegisterRequest> registerRequest,
      Account account) {
    return registerRequest.map(RoleMapper.INSTANCE::registerRequestToRoleManagement)
        .flatMap(roleManagement ->
            roleManagementDomainService.save(roleManagement, account.getAccountId()));
  }

  public Mono<RoleManagement> update(Mono<RoleManagementUpdateRequest> updateRequest,
      Account account, String roleManagementId) {
    return updateRequest.map(RoleMapper.INSTANCE::updateRequestToRoleManagement)
        .flatMap(roleManagement ->
            roleManagementDomainService.update(roleManagement, account.getAccountId(), roleManagementId));
  }

  public Mono<List<RoleManagementResponse>> getRoleManagements(String siteId, Boolean isUse) {
    return roleManagementDomainService.findBySiteIdAndIsUse(siteId, isUse)
        .flatMap(roleManagement ->
            Mono.just(RoleMapper.INSTANCE.roleManagementToResponse(roleManagement)))
        .collectList();
  }

  public Mono<RoleManagement> getOne(String roleManagementId) {
    return roleManagementDomainService.findById(roleManagementId);
  }

  @Transactional
  public Mono<RoleManagementMappingResponse> mappingAccounts(String[] accounts, String roleManagementId, Account account) {
    return roleManagementDomainService.findById(roleManagementId)
        .switchIfEmpty(Mono.error(new RoleManagementException(ErrorCode.NOT_EXIST_ROLE)))
        .flatMap(roleManagement -> {
          RoleManagementMappingResponse response = RoleManagementMappingResponse.builder()
              .id(roleManagementId)
              .name(roleManagement.getName())
              .emailList(Collections.emptyList())
              .build();
          return adminAccessDomainService.findByAdminAccountIds(accounts)
              .flatMap(adminAccess -> {
                adminAccess.setRoleManagementId(roleManagementId);
                return adminAccessDomainService.update(adminAccess, account.getAccountId());
              })
              .doOnNext((adminAccess) -> response.getEmailList().add(adminAccess.getEmail()))
              .then(Mono.just(response));
        });
  }
}