package com.bithumbsystems.persistence.mongodb.site.repository;

import com.bithumbsystems.persistence.mongodb.site.model.entity.SiteFileInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SiteFileInfoRepository extends ReactiveMongoRepository<SiteFileInfo, String> {

}
