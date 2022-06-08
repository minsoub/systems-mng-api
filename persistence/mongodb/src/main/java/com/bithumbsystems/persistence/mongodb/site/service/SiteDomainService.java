package com.bithumbsystems.persistence.mongodb.site.service;

import com.bithumbsystems.persistence.mongodb.site.model.entity.Site;
import com.bithumbsystems.persistence.mongodb.site.repository.SiteRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SiteDomainService {

  private final SiteRepository siteRepository;

  public Flux<Site> findBySearchText(String searchText, Pageable pageable) {
    return siteRepository.findBySearchText(searchText, pageable);
  }

  public Mono<Long> countBySearchText(String searchText) {
    return siteRepository.countBySearchText(searchText).log();
  }

  public Mono<Site> save(Site site) {
    site.setCreateDate(LocalDateTime.now());
    return siteRepository.save(site);
  }

  public Mono<Site> update(Site site) {
    site.setUpdateDate(LocalDateTime.now());
    return siteRepository.save(site);
  }

  public Mono<Site> findById(String siteId) {
    return siteRepository.findById(siteId);
  }
}
