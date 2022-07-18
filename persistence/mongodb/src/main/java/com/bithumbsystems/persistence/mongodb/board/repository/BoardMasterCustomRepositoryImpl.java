package com.bithumbsystems.persistence.mongodb.board.repository;

import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BoardMasterCustomRepositoryImpl implements BoardMasterCustomRepository{
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<BoardMaster> findBySearchCondition(String siteId, Boolean isUse) {
    var query = new Query();

    if (StringUtils.hasLength(siteId)) {
      query.addCriteria(new Criteria().where("site_id").is(siteId));
    }

    query.addCriteria(new Criteria().where("is_use").is(isUse));
    query.with(Sort.by("name"));

    return reactiveMongoTemplate.find(query,BoardMaster.class);
  }
}
