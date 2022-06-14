package com.bithumbsystems.persistence.mongodb.account.repository;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AdminAccessCustomRepository {

    Flux<AdminAccess> findBySearchText(String searchText);

}
