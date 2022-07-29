package com.bithumbsystems.persistence.mongodb.role.repsository;

import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import java.util.Set;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface RoleManagementRepository extends ReactiveMongoRepository<RoleManagement, String>, RoleManagementCustomRepository {

  Flux<RoleManagement> findBySiteIdAndIsUse(String siteId, Boolean isUse);

  Flux<RoleManagement> findByIdIn(Set<String> roles);

}
