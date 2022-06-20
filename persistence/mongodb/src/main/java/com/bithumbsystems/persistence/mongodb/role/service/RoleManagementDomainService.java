package com.bithumbsystems.persistence.mongodb.role.service;

import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import com.bithumbsystems.persistence.mongodb.role.repsository.RoleManagementCustomRepository;
import com.bithumbsystems.persistence.mongodb.role.repsository.RoleManagementRepository;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoleManagementDomainService {

  private final RoleManagementRepository roleManagementRepository;
  private final RoleManagementCustomRepository roleRepository;

  public Mono<RoleManagement> save(RoleManagement roleManagement, String accountId) {
    roleManagement.setCreateAdminAccountId(accountId);
    roleManagement.setCreateDate(LocalDateTime.now());
    return roleManagementRepository.insert(roleManagement);
  }

  public Mono<RoleManagement> update(RoleManagement roleManagement, String accountId, String roleManagementId) {
    return roleManagementRepository.findById(roleManagementId).flatMap(before -> {
      roleManagement.setId(before.getId());
      roleManagement.setCreateDate(before.getCreateDate());
      roleManagement.setCreateAdminAccountId(before.getCreateAdminAccountId());
      roleManagement.setUpdateAdminAccountId(accountId);
      roleManagement.setUpdateDate(LocalDateTime.now());
      return roleManagementRepository.save(roleManagement);
    });
  }

  public Mono<Boolean> isExist(String id) {
    return roleManagementRepository.existsById(id);
  }

  public Flux<RoleManagement> findBySiteIdAndIsUse(String siteId, Boolean isUse) {
    return roleManagementRepository.findBySiteIdAndIsUse(siteId, isUse);
  }

  public Flux<RoleManagement> findBySiteIdAndIsUseAndType(String siteId, Boolean isUse, String type) {
    return roleRepository.findBySiteIdAndIsUseAndType(siteId, isUse, type);
  }

  public Mono<RoleManagement> findById(String roleManagementId) {
    return roleManagementRepository.findById(roleManagementId);
  }

  public Flux<RoleManagement> findByRoleInIds(Set<String> roleManagementIds) {
    return roleManagementRepository.findByIdIn(roleManagementIds);
  }

  public Mono<Boolean> existsById(String roleManagementId) {
    return roleManagementRepository.existsById(roleManagementId);
  }
}
