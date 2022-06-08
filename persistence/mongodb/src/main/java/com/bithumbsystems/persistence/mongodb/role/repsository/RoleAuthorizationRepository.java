package com.bithumbsystems.persistence.mongodb.role.repsository;

import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleAuthorization;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RoleAuthorizationRepository extends ReactiveMongoRepository<RoleAuthorization, String> {

}
