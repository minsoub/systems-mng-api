package com.bithumbsystems.management.api.v1.site.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.v1.site.model.mapper.SiteMapper;
import com.bithumbsystems.management.api.v1.site.model.request.SiteRegisterRequest;
import com.bithumbsystems.management.api.v1.site.model.response.SiteResponse;
import com.bithumbsystems.persistence.mongodb.site.model.entity.Site;
import com.bithumbsystems.persistence.mongodb.site.service.SiteDomainService;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * The type Site service.
 */
@Service
@RequiredArgsConstructor
public class SiteService {

  private final SiteDomainService siteDomainService;

  /**
   * Find by search text mono.
   *
   * @param searchText  the search text
   * @param pageRequest the page request
   * @return the mono
   */
  public Mono<Page<SiteResponse>> findBySearchText(String searchText, PageRequest pageRequest) {
    return siteDomainService.findPageBySearchText(searchText, pageRequest)
        .map(SiteMapper.INSTANCE::siteToSiteResponse)
        .collectList()
        .zipWith(siteDomainService.countBySearchText(searchText).map(c -> c))
        .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
  }

  public Mono<List<SiteResponse>> findBySearchText(String searchText, Boolean isUse) {
    return siteDomainService.findBySearchText(searchText, isUse).flatMap(
        site -> {
          SiteResponse SiteResponse = SiteMapper.INSTANCE.siteToSiteResponse(site);
          return Mono.just(SiteResponse);
        }
    ).collectSortedList(Comparator.comparing(SiteResponse::getCreateDate).reversed());
  }

  /**
   * Create mono.
   *
   * @param siteRegisterRequest the site register request
   * @param account             the account
   * @return the mono
   */
  public Mono<SiteResponse> create(SiteRegisterRequest siteRegisterRequest, Account account) {
    Site site = SiteMapper.INSTANCE.siteRegisterRequestToSite(siteRegisterRequest);
    site.setCreateAdminAccountId(account.getAccountId());
    return siteDomainService.save(site).map(SiteMapper.INSTANCE::siteToSiteResponse);
  }

  /**
   * Gets one.
   *
   * @param siteId the site id
   * @return the one
   */
  public Mono<SiteResponse> getOne(String siteId) {
    return siteDomainService.findById(siteId).map(SiteMapper.INSTANCE::siteToSiteResponse);
  }

  /**
   * Update mono.
   *
   * @param siteId              the site id
   * @param siteRegisterRequest the site register request
   * @param account             the account
   * @return the mono
   */
  public Mono<SiteResponse> update(String siteId, SiteRegisterRequest siteRegisterRequest, Account account) {
    Site site = SiteMapper.INSTANCE.siteRegisterRequestToSite(siteRegisterRequest);
    site.setId(siteId);
    site.setUpdateAdminAccountId(account.getAccountId());
    return siteDomainService.update(site)
        .map(SiteMapper.INSTANCE::siteToSiteResponse);
  }
}