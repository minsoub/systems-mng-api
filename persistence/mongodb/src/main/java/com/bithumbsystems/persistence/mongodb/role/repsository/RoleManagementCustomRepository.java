package com.bithumbsystems.persistence.mongodb.role.repsository;

import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RoleManagementCustomRepository {
    Flux<RoleManagement> findBySiteIdAndIsUseAndType(String siteId, Boolean isUse, String type);
}
