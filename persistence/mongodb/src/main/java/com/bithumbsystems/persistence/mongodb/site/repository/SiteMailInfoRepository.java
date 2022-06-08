package com.bithumbsystems.persistence.mongodb.site.repository;

import com.bithumbsystems.persistence.mongodb.site.model.entity.SiteMailInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SiteMailInfoRepository extends ReactiveMongoRepository<SiteMailInfo, String> {

}
