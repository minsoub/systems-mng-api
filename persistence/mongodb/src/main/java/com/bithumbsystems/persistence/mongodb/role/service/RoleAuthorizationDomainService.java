package com.bithumbsystems.persistence.mongodb.role.service;

import static com.bithumbsystems.persistence.mongodb.common.util.StringUtil.generateUUIDWithOutDash;

import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleAuthorization;
import com.bithumbsystems.persistence.mongodb.role.repsository.RoleAuthorizationRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleAuthorizationDomainService {

  private final RoleAuthorizationRepository roleAuthorizationRepository;
  private static final String PREFIX = "ROLE_AUTH_";

  public Flux<RoleAuthorization> findAll() {
    return roleAuthorizationRepository.findAll();
  }
  public Mono<RoleAuthorization> findByRoleManagementId(String roleManagementId) {
    return roleAuthorizationRepository.findByRoleManagementId(roleManagementId);
  }

  public Mono<Void> deleteByRoleManagementId(String roleManagementId) {
    return roleAuthorizationRepository.deleteByRoleManagementId(roleManagementId);
  }

  public Mono<RoleAuthorization> save(RoleAuthorization roleAuthorization, String accountId) {
    roleAuthorization.setCreateDate(LocalDateTime.now());
    roleAuthorization.setCreateAdminAccountId(accountId);
    roleAuthorization.setId(PREFIX + generateUUIDWithOutDash());
    return roleAuthorizationRepository.insert(roleAuthorization);
  }

  public Mono<RoleAuthorization> update(RoleAuthorization roleAuthorization) {
    return roleAuthorizationRepository.save(roleAuthorization);
  }
}
