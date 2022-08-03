package com.bithumbsystems.persistence.mongodb.multilingual.service;

import com.bithumbsystems.persistence.mongodb.multilingual.model.entity.SiteMultilingual;
import com.bithumbsystems.persistence.mongodb.multilingual.repository.SiteMultilingualRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class SiteMultilingualDomainService {

  private final SiteMultilingualRepository siteMultilingualRepository;

  public Flux<SiteMultilingual> findAll() {
    return siteMultilingualRepository.findAll();
  }

  public Mono<SiteMultilingual> findById(String id) {
    return siteMultilingualRepository.findById(id);
  }

  public Mono<SiteMultilingual> insert(SiteMultilingual siteMultilingual) {
    return siteMultilingualRepository.insert(siteMultilingual);
  }

  public Mono<SiteMultilingual> update(SiteMultilingual siteMultilingual) {
    return siteMultilingualRepository.save(siteMultilingual);
  }
  public Flux<SiteMultilingual> saveAll(Publisher<SiteMultilingual> siteMultilingualList) {
    return siteMultilingualRepository.saveAll(siteMultilingualList);
  }
}

