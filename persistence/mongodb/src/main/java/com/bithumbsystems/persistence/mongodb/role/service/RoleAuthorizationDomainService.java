package com.bithumbsystems.persistence.mongodb.role.service;

import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleAuthorization;
import com.bithumbsystems.persistence.mongodb.role.repsository.RoleAuthorizationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleAuthorizationDomainService {

  private final RoleAuthorizationRepository roleAuthorizationRepository;

  public Mono<RoleAuthorization> findByRoleManagementId(String roleManagementId) {
    return roleAuthorizationRepository.findByRoleManagementId(roleManagementId);
  }

  public Mono<Void> deleteByRoleManagementId(String roleManagementId) {
    return roleAuthorizationRepository.deleteByRoleManagementId(roleManagementId);
  }

  public Mono<RoleAuthorization> save(RoleAuthorization roleAuthorization, String accountId) {
    roleAuthorization.setCreateDate(LocalDateTime.now());
    roleAuthorization.setCreateAdminAccountId(accountId);
    return roleAuthorizationRepository.insert(roleAuthorization);
  }
}
