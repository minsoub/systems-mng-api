package com.bithumbsystems.persistence.mongodb.role.repsository;

import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RoleManagementRepository extends ReactiveMongoRepository<RoleManagement, String> {

}
