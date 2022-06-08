package com.bithumbsystems.management.api.v1.role.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.v1.role.model.mapper.RoleMapper;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementRegisterRequest;
import com.bithumbsystems.management.api.v1.role.model.response.RoleManagementResponse;
import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import com.bithumbsystems.persistence.mongodb.role.service.RoleManagementDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleManagementService {

  private final RoleManagementDomainService roleManagementDomainService;
  public Mono<Boolean> checkDuplicate(String roleId) {
    return roleManagementDomainService.isExist(roleId);
  }

  public Mono<RoleManagement> create(Mono<RoleManagementRegisterRequest> registerRequest,
      Account account) {
    return registerRequest.map(RoleMapper.INSTANCE::roleManagementRegisterRequestToRoleManagement)
        .flatMap(roleManagement -> roleManagementDomainService.save(roleManagement, account.getAccountId()));
  }

  public Mono<List<RoleManagementResponse>> getRoleManagements(String siteId, Boolean isUse) {
    return roleManagementDomainService.findBySiteIdAndIsUse(siteId, isUse)
        .flatMap(roleManagement -> Mono.just(RoleMapper.INSTANCE.roleManagementToRoleManagementRegisterResponse(roleManagement)))
        .collectList();
  }
}
