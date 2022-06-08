package com.bithumbsystems.persistence.mongodb.account.repository;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AdminAccountRepository extends ReactiveMongoRepository<AdminAccount, String>, AdminAccountCustomRepository {
    Mono<AdminAccount> findByEmail(String email);

    Mono<AdminAccount> findById(String adminAccountId);
}
