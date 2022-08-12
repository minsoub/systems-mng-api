package com.bithumbsystems.persistence.mongodb.accessip.repository;

import com.bithumbsystems.persistence.mongodb.accessip.model.entity.AccessIp;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.account.repository.AdminAccountRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class AccessIpRepositoryImpl implements AccessIpCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  private final AdminAccountRepository adminAccountRepository;

  public Flux<AccessIp> findAccessIpBySearch(String name, String email, String siteId) {

    Query query = new Query();
    query.addCriteria(
        Criteria.where("site_id").is(siteId).and("is_use").is(true)
    );

    return adminAccountRepository.findBySearchNameOrEmail(name, email).collectList().flatMapMany(
        adminAccount -> {
          query.addCriteria(
              Criteria.where("admin_account_id").in(adminAccount.stream().map(AdminAccount::getId).collect(
                  Collectors.toList())));

          return reactiveMongoTemplate.find(query, AccessIp.class);
        }
    );

  }

}
