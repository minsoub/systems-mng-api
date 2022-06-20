package com.bithumbsystems.persistence.mongodb.account.repository;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import java.util.List;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AdminAccessRepository extends ReactiveMongoRepository<AdminAccess, String>, AdminAccessCustomRepository {


    Mono<AdminAccess> findByAdminAccountId(String adminAccountId);

    Mono<Void> deleteByAdminAccountId(String adminAccountId);

    Flux<AdminAccess> findByAdminAccountIdIn(List<String> adminAccountId);

    Mono<AdminAccess> findByAdminAccountIdAndRolesContaining(String adminAccountId, String roleManagementId);

    Flux<AdminAccess> findByRolesContains(String roleManagementId);
}
