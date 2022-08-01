package com.bithumbsystems.management.api.v1.mail.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.management.api.v1.mail.exception.SiteMailException;
import com.bithumbsystems.management.api.v1.mail.model.SiteMailMapper;
import com.bithumbsystems.management.api.v1.mail.model.request.SiteMailRequest;
import com.bithumbsystems.management.api.v1.mail.model.request.SiteMailListRequest;
import com.bithumbsystems.management.api.v1.mail.model.response.SiteMailResponse;
import com.bithumbsystems.persistence.mongodb.mail.model.entity.SiteMail;
import com.bithumbsystems.persistence.mongodb.mail.model.entity.SiteMail.SiteMailBuilder;
import com.bithumbsystems.persistence.mongodb.mail.service.SiteMailDomainService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteMailService {

  private final SiteMailDomainService siteMailDomainService;

  private final PasswordEncoder passwordEncoder;

  private final SiteMailMapper siteMailMapper;

  public Mono<List<SiteMailResponse>> getSiteMailList(SiteMailListRequest request) {
    return siteMailDomainService.findMailBySiteIdAndIsUse(request.getSiteId(), request.getIsUse())
        .map(siteMailMapper::siteMailToSiteMailResponse).collectList();
  }

  public Mono<SiteMailResponse> getSiteMail(String id) {
    return siteMailDomainService.findById(id)
        .switchIfEmpty(Mono.error(new SiteMailException(ErrorCode.INVALID_DATA)))
        .map(siteMailMapper::siteMailToSiteMailResponse);
  }

  public Mono<SiteMailResponse> createSiteMail(SiteMailRequest request, Account account) {
    SiteMail siteMail = makeMailBuilder(request).build();

    siteMail.setCreateDate(LocalDateTime.now());
    siteMail.setCreateAdminAccountId(account.getAccountId());

    return siteMailDomainService.save(siteMail).map(siteMailMapper::siteMailToSiteMailResponse);
  }

  public Mono<SiteMailResponse> updateSiteMail(String id, SiteMailRequest request,
      Account account) {
    SiteMail mail = makeMailBuilder(request).id(id).build();

    return siteMailDomainService.findById(id)
        .switchIfEmpty(Mono.error(new SiteMailException(ErrorCode.INVALID_DATA)))
        .flatMap(siteSiteMail -> {
          mail.setCreateDate(siteSiteMail.getCreateDate());
          mail.setCreateAdminAccountId(siteSiteMail.getCreateAdminAccountId());
          mail.setUpdateDate(LocalDateTime.now());
          mail.setUpdateAdminAccountId(account.getAccountId());

          return siteMailDomainService.update(mail).map(siteMailMapper::siteMailToSiteMailResponse);
        });
  }

  public SiteMailBuilder makeMailBuilder(SiteMailRequest request) {
    return SiteMail.builder().siteId(request.getSiteId()).accountId(request.getAccountId())
        .isUse(request.getIsUse()).siteName(request.getSiteName())
        .serverInfo(request.getServerInfo()).adminUserEmail(request.getAdminUserEmail())
        .accountPassword(passwordEncoder.encode(request.getAccountPassword()));
  }

}
