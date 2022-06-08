package com.bithumbsystems.persistence.mongodb.account.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class AdminAccountCustomRepositoryImpl implements AdminAccountCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<AdminAccount> findBySearchText(String searchText) {
    var reg = ".*" + searchText + ".*";
    return reactiveMongoTemplate
        .find(query(new Criteria()
            .orOperator(
                where("name").regex(reg),
                where("email").regex(reg))), AdminAccount.class);
  }
}
