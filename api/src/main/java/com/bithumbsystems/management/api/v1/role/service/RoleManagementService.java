package com.bithumbsystems.management.api.v1.role.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.management.api.v1.role.exception.RoleManagementException;
import com.bithumbsystems.management.api.v1.role.model.mapper.RoleMapper;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementRegisterRequest;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementUpdateRequest;
import com.bithumbsystems.management.api.v1.role.model.response.RoleManagementMappingResponse;
import com.bithumbsystems.management.api.v1.role.model.response.RoleManagementResponse;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccessDomainService;
import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import com.bithumbsystems.persistence.mongodb.role.service.RoleManagementDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
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

  public Mono<List<RoleManagementResponse>> getRoleManagements(String siteId, Boolean isUse, String type) {
    return roleManagementDomainService.findBySiteIdAndIsUseAndType(siteId, isUse, type)
        .flatMap(roleManagement ->
            Mono.just(RoleMapper.INSTANCE.roleManagementToResponse(roleManagement)))
        .collectList();
  }

  public Mono<RoleManagement> getOne(String roleManagementId) {
    return roleManagementDomainService.findById(roleManagementId);
  }

  @Transactional
  public Mono<RoleManagementMappingResponse> mappingAccounts(List<String> accounts, String roleManagementId, Account account) {
    var roleManagementMappingResponse = roleManagementDomainService.findById(roleManagementId)
        .switchIfEmpty(Mono.error(new RoleManagementException(ErrorCode.NOT_EXIST_ROLE)))
        .map(roleManagement -> RoleManagementMappingResponse.builder()
            .id(roleManagementId)
            .name(roleManagement.getName())
            .build());

    var getAccountEmails = adminAccessDomainService.findByAdminAccountIds(accounts)
        .flatMap(adminAccess -> {
          log.info(adminAccess.getEmail());
          adminAccess.setRoleManagementId(roleManagementId);
          return adminAccessDomainService.update(adminAccess, account.getAccountId()).map(
              AdminAccess::getEmail);
        })
        .collectList();

    return roleManagementMappingResponse.zipWith(getAccountEmails)
        .map(tuple -> {
          tuple.getT1().setEmailList(tuple.getT2());
          return tuple.getT1();
        });
  }
}