package com.bithumbsystems.persistence.mongodb.account.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.site.model.entity.Site;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class AdminAccountCustomRepositoryImpl implements AdminAccountCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<AdminAccount> findBySearchText(String searchText, Boolean isUse) {
      var reg = ".*" + searchText + ".*";
      var condition = new Query();
      if(isUse ==null)
      {
        condition = query(new Criteria()
                .orOperator(
                        where("name").regex(reg),
                        where("email").regex(reg)));
      } else
      {
        condition = query(new Criteria()
                .andOperator(
                        where("is_use").is(isUse)
                )
                .orOperator(
                        where("name").regex(reg),
                        where("email").regex(reg)));
      }
      return reactiveMongoTemplate
                .find(condition,AdminAccount .class);
  }

}
