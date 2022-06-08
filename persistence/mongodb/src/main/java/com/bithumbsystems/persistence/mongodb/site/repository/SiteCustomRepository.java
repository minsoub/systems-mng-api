package com.bithumbsystems.persistence.mongodb.site.repository;

import com.bithumbsystems.persistence.mongodb.site.model.entity.Site;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SiteCustomRepository {

  Flux<Site> findBySearchText(String searchText, Pageable page);

  Mono<Long> countBySearchText(String searchText);
}
