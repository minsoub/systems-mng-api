package com.bithumbsystems.persistence.mongodb.menu.repository;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.SiteMenuProgram;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SiteMenuProgramRepository extends ReactiveMongoRepository<SiteMenuProgram, String> {
  Flux<SiteMenuProgram> findBySiteIdAndMenuId(String siteId, String menuId);

  Mono<Void> deleteBySiteIdAndMenuId(String siteId, String menuId);

  Flux<SiteMenuProgram> findByMenuId(String menuId);
}
