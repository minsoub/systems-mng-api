package com.bithumbsystems.persistence.mongodb.account.repository;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface AdminAccountRepository extends ReactiveMongoRepository<AdminAccount, String>, AdminAccountCustomRepository {
    Mono<AdminAccount> findByEmail(String email);

    //Mono<AdminAccount> findById(String adminAccountId);

    Flux<AdminAccount> findByIdIn(List<String> adminAccountId);
}
