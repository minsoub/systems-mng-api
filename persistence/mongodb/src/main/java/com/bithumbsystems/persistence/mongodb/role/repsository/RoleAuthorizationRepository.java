package com.bithumbsystems.persistence.mongodb.role.repsository;

import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleAuthorization;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface RoleAuthorizationRepository extends ReactiveMongoRepository<RoleAuthorization, String> {
  Mono<RoleAuthorization> findByRoleManagementId(String roleManagementId);
  Mono<Void> deleteByRoleManagementId(String roleManagementId);

}
