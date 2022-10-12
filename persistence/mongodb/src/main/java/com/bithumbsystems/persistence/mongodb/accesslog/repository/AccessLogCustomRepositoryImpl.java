package com.bithumbsystems.persistence.mongodb.accesslog.repository;

import com.bithumbsystems.persistence.mongodb.accesslog.model.entity.AccessLog;
import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class AccessLogCustomRepositoryImpl implements AccessLogCustomRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<AccessLog> findPageBySearchText(LocalDate fromDate, LocalDate toDate, String keyword, String mySiteId) {
        Query query = new Query();

        // TODO: my_site_id 고려
        query.addCriteria(Criteria.where("create_date").gte(fromDate.atTime(0, 0, 0)).lte(toDate.atTime(23, 59, 59)));
        //.addCriteria(Criteria.where("my_site_id").is(mySiteId));
//        query.addCriteria(new Criteria().andOperator(
//                Criteria.where("site_id").is(mySiteId)
//        ));
        if (StringUtils.hasLength(keyword)) {   //감사 로그 관리 목록
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("email").regex(".*" + keyword + ".*", "i"),
                    Criteria.where("ip").regex(".*" + keyword + ".*", "i"),
                    Criteria.where("reason").regex(".*" + keyword + ".*", "i"),
                    Criteria.where("description").regex(".*" + keyword + ".*", "i")
            ));
        }

        return reactiveMongoTemplate.find(query, AccessLog.class);
    }

}
