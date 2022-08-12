package com.bithumbsystems.persistence.mongodb.accessip.repository;

import com.bithumbsystems.persistence.mongodb.accessip.model.entity.AccessIp;
import java.util.List;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AccessIpRepository extends ReactiveMongoRepository<AccessIp, String>, AccessIpCustomRepository {

  Flux<AccessIp> findAccessIpByAdminAccountId(String adminAccountId);

  Flux<AccessIp> findAccessIpByAdminAccountIdAndSiteId(String adminAccountId, String siteId);

  Flux<AccessIp> findAccessIpBySiteId(String siteId);

  Flux<AccessIp> findAccessIpBySiteIdAndAdminAccountIdIn(String siteId, List<String> adminAccountIdList);

}

