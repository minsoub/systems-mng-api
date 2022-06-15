package com.bithumbsystems.persistence.mongodb.site.service;

import com.bithumbsystems.persistence.mongodb.site.model.entity.Site;
import com.bithumbsystems.persistence.mongodb.site.model.entity.SiteFileInfo;
import com.bithumbsystems.persistence.mongodb.site.repository.SiteFileInfoRepository;
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
  private final SiteFileInfoRepository siteFileInfoRepository;

  public Flux<Site> findPageBySearchText(String searchText, Pageable pageable) {
    return siteRepository.findPageBySearchText(searchText, pageable);
  }

  public Flux<Site> findBySearchText(String searchText, Boolean isUse) {
    return siteRepository.findBySearchText(searchText, isUse);
  }

  public Mono<Long> countBySearchText(String searchText) {
    return siteRepository.countBySearchText(searchText);
  }

  public Mono<Site> save(Site site) {
    site.setCreateDate(LocalDateTime.now());
    return siteRepository.insert(site);
  }

  public Mono<Site> update(Site site) {
    site.setUpdateDate(LocalDateTime.now());
    return siteRepository.save(site);
  }

  public Mono<Site> findById(String siteId) {
    return siteRepository.findById(siteId);
  }

  public Mono<Boolean> existsById(String siteId) {
    return siteRepository.existsById(siteId);
  }

  public Mono<SiteFileInfo> saveFileInfo(String siteId, SiteFileInfo siteFileInfo, String accountId) {
    siteFileInfo.setSiteId(siteId);
    siteFileInfo.setCreateDate(LocalDateTime.now());
    siteFileInfo.setCreateAdminAccountId(accountId);
    return siteFileInfoRepository.insert(siteFileInfo);
  }

  public Mono<SiteFileInfo> updateFileInfo(String siteId, SiteFileInfo siteFileInfo, String accountId) {
    return siteFileInfoRepository.findById(siteId).flatMap(before -> {
      siteFileInfo.setSiteId(siteId);
      siteFileInfo.setCreateDate(before.getCreateDate());
      siteFileInfo.setCreateAdminAccountId(before.getCreateAdminAccountId());
      siteFileInfo.setUpdateDate(LocalDateTime.now());
      siteFileInfo.setUpdateAdminAccountId(accountId);
      return siteFileInfoRepository.save(siteFileInfo);
   });
  }

  public Mono<SiteFileInfo> findFileInfoBySiteId(String siteId, Boolean isUse) {
    return siteFileInfoRepository.findBySiteIdAndIsUse(siteId, isUse);
  }

  public Flux<SiteFileInfo> findFileInfoList(Boolean isUse) {
    return siteFileInfoRepository.findByIsUse(isUse);
  }
}
