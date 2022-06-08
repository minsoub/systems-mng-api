package com.bithumbsystems.persistence.mongodb.menu.repository;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.SiteMenuProgram;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SiteMenuProgramRepository extends ReactiveMongoRepository<SiteMenuProgram, String> {

}
