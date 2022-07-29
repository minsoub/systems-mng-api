package com.bithumbsystems.persistence.mongodb.role.repsository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleManagementCustomRepositoryImpl implements RoleManagementCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  /**
   * 사이트/사용여부/타입에 의한 조회
   *
   * @param siteId
   * @param isUse
   * @param type
   * @return
   */
  public Flux<RoleManagement> findBySiteIdSearchTextAndIsUseAndType(String siteId,
      String searchText, Boolean isUse, String type) {
    var condition = new Query();
    var reg = ".*" + searchText + ".*";
    Criteria[] common = {where("name").regex(reg),where("type").regex(reg), where("id").regex(reg)};
    if (!StringUtils.hasLength(type)) {
      condition = query(new Criteria()
          .andOperator(
              where("site_id").is(siteId),
              where("is_use").is(isUse))
          .orOperator(common));
    } else {
      condition = query(new Criteria()
          .andOperator(
              where("is_use").is(isUse),
              where("site_id").is(siteId),
              where("type").is(type),
              where("name").regex(reg),
              where("type").regex(reg),
              where("id").regex(reg)).orOperator(common));
    }

    return reactiveMongoTemplate
        .find(condition, RoleManagement.class);
  }

//    @Override
//    public Flux<Site> findPageBySearchText(String searchText, Pageable page) {
//        var reg = ".*" + searchText + ".*";
//        return reactiveMongoTemplate
//            .find(query(new Criteria()
//                .orOperator(
//                    where("name").regex(reg),
//                    where("id").regex(reg))).with(page), Site.class);
//    }

}
