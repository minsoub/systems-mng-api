package com.bithumbsystems.persistence.mongodb.site.repository;

import com.bithumbsystems.persistence.mongodb.site.model.entity.SiteFileInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SiteFileInfoRepository extends ReactiveMongoRepository<SiteFileInfo, String> {
  Mono<SiteFileInfo> findBySiteIdAndIsUse(String siteId, Boolean isUse);

  Flux<SiteFileInfo> findByIsUse(Boolean isUse);

}
