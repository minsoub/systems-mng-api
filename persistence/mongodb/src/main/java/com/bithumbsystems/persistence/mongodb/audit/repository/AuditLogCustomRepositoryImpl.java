package com.bithumbsystems.persistence.mongodb.audit.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AuditLogCustomRepositoryImpl implements AuditLogCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<AuditLog> findPageBySearchText(String searchText, LocalDateTime startDate,
      LocalDateTime endDate, Pageable pageable) {
    var reg = ".*" + searchText + ".*";
    return reactiveMongoTemplate
        .find(query(new Criteria()
            .orOperator(
                where("email").regex(reg),
                where("ip").regex(reg),
                where("menu_name").regex(reg),
                where("program_name").regex(reg),
                where("url").regex(reg),
                where("parameter").regex(reg))
            .andOperator(
                where("create_date").gte(startDate)
                    .lte(endDate)
            ))
            .with(pageable)
            , AuditLog.class);
  }

  @Override
  public Mono<Long> countBySearchText(String searchText, LocalDateTime startDate, LocalDateTime endDate) {
    var reg = ".*" + searchText + ".*";
    return reactiveMongoTemplate
        .count(query(new Criteria()
            .orOperator(
                where("email").regex(reg),
                where("ip").regex(reg),
                where("menu_name").regex(reg),
                where("program_name").regex(reg),
                where("url").regex(reg),
                where("parameter").regex(reg))
                .andOperator(
                    where("create_date").gte(startDate)
                        .lte(endDate)
                )),
            AuditLog.class);
  }
}
