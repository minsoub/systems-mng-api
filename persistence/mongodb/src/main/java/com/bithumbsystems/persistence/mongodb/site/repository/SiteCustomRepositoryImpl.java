package com.bithumbsystems.persistence.mongodb.site.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.bithumbsystems.persistence.mongodb.site.model.entity.Site;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SiteCustomRepositoryImpl implements SiteCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<Site> findPageBySearchText(String searchText, Pageable page) {
    var reg = ".*" + searchText + ".*";
    return reactiveMongoTemplate
        .find(query(new Criteria()
                .orOperator(
                    where("name").regex(reg),
                    where("id").regex(reg))).with(page), Site.class);
  }

  @Override
  public Flux<Site> findBySearchText(String searchText) {
    var reg = ".*" + searchText + ".*";
    return reactiveMongoTemplate
        .find(query(new Criteria()
            .orOperator(
                where("name").regex(reg),
                where("id").regex(reg))), Site.class);
  }

  @Override
  public Mono<Long> countBySearchText(String searchText) {
    var reg = ".*" + searchText + ".*";
    return reactiveMongoTemplate
        .count(query(new Criteria()
            .orOperator(
                where("name").regex(reg),
                where("id").regex(reg))), Site.class);
  }
}
