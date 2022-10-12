package com.bithumbsystems.persistence.mongodb.audit.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AuditLogCustomRepositoryImpl implements AuditLogCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<AuditLog> findPageBySearchText(String searchText, LocalDateTime startDate,
      LocalDateTime endDate, String mySiteId, Pageable pageable) {
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
                where("my_site_id").regex(mySiteId),
                where("create_date").gte(startDate)
                    .lte(endDate)
            ))
            .with(pageable)
            , AuditLog.class);
  }

  @Override
  public Mono<Long> countBySearchText(String searchText, LocalDateTime startDate, LocalDateTime endDate, String mySiteId) {
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
                    where("my_site_id").regex(mySiteId),
                    where("create_date").gte(startDate)
                        .lte(endDate)
                )),
            AuditLog.class);
  }

  @Override
  public Flux<AuditLog> findPageBySearchText(LocalDate fromDate, LocalDate toDate, String keyword, String mySiteId) {

    Query query = new Query();

    query.addCriteria(Criteria.where("create_date").gte(fromDate.atTime(0, 0, 0)).lte(toDate.atTime(23, 59, 59)));
    query.addCriteria(Criteria.where("my_site_id").is(mySiteId));
    if (StringUtils.isNotEmpty(keyword)) {   //감사 로그 관리 목록
      query.addCriteria(new Criteria().orOperator(
          Criteria.where("email").regex(".*" + keyword + ".*", "i"),
          Criteria.where("ip").regex(".*" + keyword + ".*", "i"),
          Criteria.where("menu_name").regex(".*" + keyword + ".*", "i"),
          Criteria.where("program_name").regex(".*" + keyword + ".*", "i"),
          Criteria.where("uri").regex(".*" + keyword + ".*", "i"),
          Criteria.where("parameter").regex(".*" + keyword + ".*", "i")
      ));
    }


    return reactiveMongoTemplate.find(query, AuditLog.class);
  }
}
