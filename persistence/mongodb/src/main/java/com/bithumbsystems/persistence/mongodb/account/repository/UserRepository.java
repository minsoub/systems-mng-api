package com.bithumbsystems.persistence.mongodb.account.repository;

import com.bithumbsystems.persistence.mongodb.account.model.entity.UserAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserAccount, String> {
    Mono<UserAccount> findByEmail(String email);
}
