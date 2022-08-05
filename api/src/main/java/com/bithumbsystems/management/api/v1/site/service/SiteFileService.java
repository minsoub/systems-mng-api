package com.bithumbsystems.management.api.v1.site.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.v1.site.model.mapper.SiteMapper;
import com.bithumbsystems.management.api.v1.site.model.request.SiteFileInfoRequest;
import com.bithumbsystems.management.api.v1.site.model.response.SiteFileInfoResponse;
import com.bithumbsystems.persistence.mongodb.site.service.SiteDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * The type Site service.
 */
@Service
@RequiredArgsConstructor
public class SiteFileService {

  private final SiteDomainService siteDomainService;


  public Mono<SiteFileInfoResponse> createFileManagement(String siteId, SiteFileInfoRequest siteFileInfoRequest, Account account) {
    return siteDomainService.saveFileInfo(siteId,
            SiteMapper.INSTANCE.siteFileInfoRequestToSiteFileInfo(siteFileInfoRequest),
            account.getAccountId())
        .map(SiteMapper.INSTANCE::siteFileInfoToResponse);
  }

  public Mono<SiteFileInfoResponse> updateFileManagement(String siteId, SiteFileInfoRequest siteFileInfoRequest, Account account) {
    return siteDomainService.updateFileInfo(siteId,
            SiteMapper.INSTANCE.siteFileInfoRequestToSiteFileInfo(siteFileInfoRequest),
            account.getAccountId())
        .map(SiteMapper.INSTANCE::siteFileInfoToResponse);
  }

  public Mono<SiteFileInfoResponse> getFileManagement(String siteId, Boolean isUse) {
    return siteDomainService.findFileInfoBySiteId(siteId, isUse).map(SiteMapper.INSTANCE::siteFileInfoToResponse);
  }

  public Mono<List<SiteFileInfoResponse>> getFileManagements(Boolean isUse) {
    return siteDomainService.findFileInfoList(isUse).flatMap(siteFile -> {
      return siteDomainService.findById(siteFile.getSiteId())
              .flatMap(result ->  {
                return Mono.just(
                        SiteFileInfoResponse.builder()
                                .id(siteFile.getId())
                                .siteId(siteFile.getSiteId())
                                .siteName(result.getName())
                                .sizeLimit(siteFile.getSizeLimit())
                                .extensionLimit(siteFile.getExtensionLimit())
                                .isUse(siteFile.getIsUse())
                                .createDate(siteFile.getCreateDate())
                                .build()
                );
              });
        //return Mono.just(SiteMapper.INSTANCE.siteFileInfoToResponse(siteFile));
    }).collectList();
  }

  public Mono<SiteFileInfoResponse> getFile(String id) {
    return siteDomainService.findFileInfoById(id).flatMap(siteFileInfo -> {
          return siteDomainService.findById(siteFileInfo.getSiteId())
              .flatMap(site -> {
                return Mono.just(SiteFileInfoResponse.builder()
                    .id(siteFileInfo.getId())
                    .siteId(site.getId())
                    .siteName(site.getName())
                    .createDate(siteFileInfo.getCreateDate())
                    .extensionLimit(siteFileInfo.getExtensionLimit())
                    .isUse(siteFileInfo.getIsUse())
                    .sizeLimit(siteFileInfo.getSizeLimit())
                    .build());
              });
        });
  }
}