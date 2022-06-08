package com.bithumbsystems.persistence.mongodb.account.repository;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AdminAccessRepository extends ReactiveMongoRepository<AdminAccess, String> {

    Flux<AdminAccess> findBySiteId(String siteId);

    Mono<AdminAccess> findByAdminAccountId(String adminAccountId);
}
