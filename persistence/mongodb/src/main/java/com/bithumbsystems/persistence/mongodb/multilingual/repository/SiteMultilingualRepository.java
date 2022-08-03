package com.bithumbsystems.persistence.mongodb.multilingual.repository;

import com.bithumbsystems.persistence.mongodb.multilingual.model.entity.SiteMultilingual;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteMultilingualRepository extends ReactiveMongoRepository<SiteMultilingual, String> {

}
