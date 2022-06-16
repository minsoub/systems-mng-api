package com.bithumbsystems.persistence.mongodb.menu.repository;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Repository
public interface MenuRepository extends ReactiveMongoRepository<Menu, String>, MenuCustomRepository {

  Mono<Menu> findBySiteIdAndId(String siteId, String id);

  Flux<Menu> findBySiteId(String siteId);

  Flux<Menu> findByIsUseIsTrue();

}
