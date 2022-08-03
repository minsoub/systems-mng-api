package com.bithumbsystems.management.api.v1.multilingual.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.v1.multilingual.model.dto.Multilingual.MultilingualRequest;
import com.bithumbsystems.management.api.v1.multilingual.model.dto.Multilingual.MultilingualResponse;
import com.bithumbsystems.management.api.v1.multilingual.model.mapper.MultilingualMapper;
import com.bithumbsystems.persistence.mongodb.multilingual.model.entity.SiteMultilingual;
import com.bithumbsystems.persistence.mongodb.multilingual.service.SiteMultilingualDomainService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MultilingualService {

  private final SiteMultilingualDomainService siteMultilingualDomainService;

  private final MultilingualMapper multilingualMapper;

  public Mono<List<MultilingualResponse>> getMultilingualList() {
    return siteMultilingualDomainService.findAll().map(multilingualMapper::multilingualToResponse)
        .collectList();
  }

  public Mono<MultilingualResponse> getMultilingual(String id) {
    return siteMultilingualDomainService.findById(id)
        .map(multilingualMapper::multilingualToResponse);
  }

  public Mono<MultilingualResponse> createMultilingual(MultilingualRequest request, Account account) {
    SiteMultilingual siteMultilingual = makeMultilingualBuilder(request).id(
        UUID.randomUUID().toString()).build();

    siteMultilingual.setCreateDate(LocalDateTime.now());
    siteMultilingual.setCreateAdminAccountId(account.getAccountId());

    return siteMultilingualDomainService.insert(siteMultilingual)
        .map(multilingualMapper::multilingualToResponse);
  }

  public Mono<MultilingualResponse> updateMultilingual(String id, MultilingualRequest request, Account account) {
    SiteMultilingual siteMultilingual = makeMultilingualBuilder(request).id(id).build();

    return siteMultilingualDomainService.findById(id)
            .flatMap(multilingual -> {
              siteMultilingual.setCreateDate(multilingual.getCreateDate());
              siteMultilingual.setCreateAdminAccountId(multilingual.getCreateAdminAccountId());
              siteMultilingual.setUpdateDate(LocalDateTime.now());
              siteMultilingual.setUpdateAdminAccountId(account.getAccountId());

              return siteMultilingualDomainService.update(siteMultilingual).map(multilingualMapper::multilingualToResponse);
            });
  }

  public Mono<List<MultilingualResponse>> createMultilingualList(List<MultilingualRequest> requestList, Account account) {
    List<SiteMultilingual> siteMultilingualList = requestList.stream().map(
        multilingualRequest -> {
          SiteMultilingual siteMultilingual;
          if (multilingualRequest.getId() == null) {
            siteMultilingual = makeMultilingualBuilder(multilingualRequest).id(
                UUID.randomUUID().toString()).build();

            siteMultilingual.setCreateDate(LocalDateTime.now());
            siteMultilingual.setCreateAdminAccountId(account.getAccountId());
          } else {
            siteMultilingual = makeMultilingualBuilder(multilingualRequest).id(
                multilingualRequest.getId()).build();

            siteMultilingual.setCreateDate(LocalDateTime.now());
            siteMultilingual.setCreateAdminAccountId(account.getAccountId());
            siteMultilingual.setUpdateDate(LocalDateTime.now());
            siteMultilingual.setUpdateAdminAccountId(account.getAccountId());
          }
          return siteMultilingual;
        }
    ).collect(Collectors.toList());

    return siteMultilingualDomainService.saveAll(Flux.fromIterable(siteMultilingualList))
        .map(multilingualMapper::multilingualToResponse).collectList();
  }

  private SiteMultilingual.SiteMultilingualBuilder makeMultilingualBuilder(MultilingualRequest request) {
    return SiteMultilingual.builder()
        .eng(request.getEng())
        .kor(request.getKor())
        .isUse(request.getIsUse())
        .type(request.getType())
        .siteId(request.getSiteId())
        .siteName(request.getSiteName());
  }

}
