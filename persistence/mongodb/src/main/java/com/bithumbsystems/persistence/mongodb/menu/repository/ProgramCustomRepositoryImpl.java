package com.bithumbsystems.persistence.mongodb.menu.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Program;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProgramCustomRepositoryImpl implements ProgramCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<Program> findBySearchText(String siteId, String searchText, Boolean isUse) {
    var reg = ".*" + searchText + ".*";
    var condition = new ArrayList<Criteria>();
    condition.add(new Criteria().andOperator(where("site_id").is(siteId)));
    if(isUse == null) {
      condition.add(new Criteria().orOperator(
          where("name").regex(reg),
          where("id").regex(reg)));
    }  else {
      condition.add(new Criteria().orOperator(
          where("name").regex(reg),
          where("id").regex(reg)));
      condition.add(where("is_use").is(isUse));
    }

    var where = query(new Criteria().andOperator(condition));

    return reactiveMongoTemplate
        .find(where, Program.class);
  }

}
