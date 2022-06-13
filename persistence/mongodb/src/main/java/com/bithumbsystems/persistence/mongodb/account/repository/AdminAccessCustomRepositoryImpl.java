package com.bithumbsystems.persistence.mongodb.account.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AdminAccessCustomRepositoryImpl implements AdminAccessCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  public Flux<AdminAccess> findBySearchText(String searchText) {
    var reg = ".*" + searchText + ".*";
    return reactiveMongoTemplate
        .find(query(new Criteria()
            .orOperator(
                where("name").regex(reg),
                where("email").regex(reg))), AdminAccess.class);
  }
//
//  public Flux<AdminAccess> findByAdminAccountIdAndRoleManagementIdAndSiteId(String adminAccountId, String roleManagementId, String siteId) {
//    return reactiveMongoTemplate
//            .find(query(new Criteria()
//                    .andOperator(
//                            where("admin_account_id").regex(adminAccountId),
//                            where("role_management_id").regex(roleManagementId),
//                            where("site_id").regex(siteId))), AdminAccess.class);
//  }
}
