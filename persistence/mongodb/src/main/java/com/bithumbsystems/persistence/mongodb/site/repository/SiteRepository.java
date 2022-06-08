package com.bithumbsystems.persistence.mongodb.site.repository;

import com.bithumbsystems.persistence.mongodb.site.model.entity.Site;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends ReactiveMongoRepository<Site, String>, SiteCustomRepository {
}
