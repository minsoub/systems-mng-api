package com.bithumbsystems.persistence.mongodb.menu.repository;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.SiteMenuProgram;
import java.util.List;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SiteMenuProgramRepository extends ReactiveMongoRepository<SiteMenuProgram, String> {
  Flux<SiteMenuProgram> findBySiteIdAndMenuId(String siteId, String menuId);

  Mono<Void> deleteBySiteIdAndMenuIdAndProgramIdIn(String siteId, String menuId, List<String> programIds);

  Flux<SiteMenuProgram> findByMenuId(String menuId);

  Flux<SiteMenuProgram> findByProgramId(String programId);

  Mono<Void> deleteBySiteIdAndMenuId(String siteId, String menuId);
}
