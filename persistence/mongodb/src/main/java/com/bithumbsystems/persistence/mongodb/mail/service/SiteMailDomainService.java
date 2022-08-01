package com.bithumbsystems.persistence.mongodb.mail.service;


import com.bithumbsystems.persistence.mongodb.mail.model.entity.SiteMail;
import com.bithumbsystems.persistence.mongodb.mail.repository.SiteMailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class SiteMailDomainService {

  private final SiteMailRepository siteMailRepository;

  public Flux<SiteMail> findMailBySiteIdAndIsUse(String siteId, Boolean isUse) {
    return siteMailRepository.findMailBySiteIdAndIsUseOrderByCreateDateDesc(siteId, isUse);
  }

  @Transactional
  public Mono<SiteMail> save(SiteMail siteMail) {
    return siteMailRepository.insert(siteMail);
  }

  @Transactional
  public Mono<SiteMail> update(SiteMail siteMail) {

    return siteMailRepository.save(siteMail);
  }

  public Mono<SiteMail> findById(String id) {
    return siteMailRepository.findById(id);
  }

}
