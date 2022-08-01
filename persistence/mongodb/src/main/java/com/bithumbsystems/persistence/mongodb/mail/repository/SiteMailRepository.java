package com.bithumbsystems.persistence.mongodb.mail.repository;

import com.bithumbsystems.persistence.mongodb.mail.model.entity.SiteMail;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SiteMailRepository extends ReactiveMongoRepository<SiteMail, String> {
  Flux<SiteMail> findMailBySiteIdAndIsUseOrderByCreateDateDesc(String siteId, Boolean isUse);
}
