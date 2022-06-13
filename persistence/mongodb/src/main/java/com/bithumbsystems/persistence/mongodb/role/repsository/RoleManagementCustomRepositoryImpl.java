package com.bithumbsystems.persistence.mongodb.role.repsository;

import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import com.bithumbsystems.persistence.mongodb.site.model.entity.Site;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleManagementCustomRepositoryImpl implements RoleManagementCustomRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public Flux<RoleManagement> findBySiteIdAndIsUseAndType(String siteId, Boolean isUse, String type) {
        var condition = new Query();
        if(type == null) {
            condition = query(new Criteria()
                    .andOperator(
                            where("site_id").is(siteId),
                            where("is_use").is(isUse)));
        } else {
            condition = query(new Criteria()
                    .andOperator(
                            where("is_use").is(isUse)
                    )
                    .orOperator(
                            where("site_id").is(siteId),
                            where("type").is(type)));
        }

        return reactiveMongoTemplate
                .find(condition, RoleManagement.class);
    }
}
