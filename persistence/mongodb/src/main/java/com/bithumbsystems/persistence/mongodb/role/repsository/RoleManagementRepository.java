package com.bithumbsystems.persistence.mongodb.role.repsository;

import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleManagementRepository extends ReactiveMongoRepository<RoleManagement, String> {

  Flux<RoleManagement> findBySiteIdAndIsUse(String siteId, Boolean isUse);

}
