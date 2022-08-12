package com.bithumbsystems.persistence.mongodb.account.repository;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import reactor.core.publisher.Flux;

public interface AdminAccountCustomRepository {

    Flux<AdminAccount> findBySearchText(String searchText, Boolean isUse);

    Flux<AdminAccount> findBySearchNameOrEmail(String name, String email);
}
