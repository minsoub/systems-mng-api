package com.bithumbsystems.management.api.v1.accessip.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.model.enums.JobType;
import com.bithumbsystems.management.api.core.model.request.AccessAllowIpRequest;
import com.bithumbsystems.management.api.core.util.sender.AwsSQSSender;
import com.bithumbsystems.management.api.v1.accessip.model.mapper.AccessIpMapper;
import com.bithumbsystems.management.api.v1.accessip.model.request.AccessIpRequest;
import com.bithumbsystems.management.api.v1.accessip.model.response.AccessIpDetailResponse;
import com.bithumbsystems.management.api.v1.accessip.model.response.AccessIpListResponse;
import com.bithumbsystems.management.api.v1.accessip.model.response.AccessIpResponse;
import com.bithumbsystems.persistence.mongodb.accessip.model.entity.AccessIp;
import com.bithumbsystems.persistence.mongodb.accessip.service.AccessIpDomainService;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccessDomainService;
import com.bithumbsystems.persistence.mongodb.role.service.RoleManagementDomainService;
import com.bithumbsystems.persistence.mongodb.site.service.SiteDomainService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessIpService {

  private final AccessIpDomainService accessIpDomainService;

  private final AccessIpMapper accessIpMapper;

  private final SiteDomainService siteDomainService;

  private final AwsSQSSender<AccessAllowIpRequest> awsSQSSender;

  private final AdminAccessDomainService adminAccessDomainService;

  private final RoleManagementDomainService roleManagementDomainService;

  public Mono<List<AccessIpListResponse>> getAccessIpList(String siteId, String name,
      String email) {
    return accessIpDomainService.findAccessIpBySearch(name, email, siteId).flatMap(
            accessIp -> {
              return adminAccessDomainService.findByAdminAccountId(accessIp.getAdminAccountId())
                  .flatMap(
                      adminAccess -> {
                        return roleManagementDomainService.findById(accessIp.getRoleId()).flatMap(
                            role -> {
                              return siteDomainService.findById(accessIp.getSiteId()).flatMap(
                                  site -> {
                                    return Mono.just(AccessIpListResponse.builder()
                                        .id(accessIp.getId())
                                        .adminAccountId(accessIp.getAdminAccountId())
                                        .name(adminAccess.getName())
                                        .siteName(site.getName())
                                        .allowIp(accessIp.getAllowIp())
                                        .email(adminAccess.getEmail())
                                        .roleId(role.getName())
                                        .build()
                                    );
                                  }
                              );
                            }
                        );
                      }
                  );
            }
        ).collect(Collectors.groupingBy(AccessIpListResponse::getAdminAccountId))
        .map(this::makeAccessIpList);
  }

  public Mono<List<AccessIpDetailResponse>> getAccessIp(String siteId, String adminAccountId) {
    return accessIpDomainService.findAccessIpByAdminAccountIdAndSiteId(adminAccountId, siteId)
        .map(accessIpMapper::accessIpToDetailResponse).map(
            response -> {
              response.setValidStartDate(response.getValidStartDate());
              response.setValidEndDate(response.getValidEndDate());
              return response;
            }
        ).collectList();
  }

  @Transactional
  public Mono<AccessIpResponse> deleteAccount(String id, Account account) {
    return accessIpDomainService.findById(id)
        .flatMap(
            accessIp -> {
              accessIp.setCreateDate(accessIp.getCreateDate());
              accessIp.setCreateAdminAccountId(accessIp.getCreateAdminAccountId());
              accessIp.setUpdateDate(LocalDateTime.now());
              accessIp.setUpdateAdminAccountId(account.getAccountId());
              accessIp.setIsUse(false);
              sendMessageToSqs(makeAccessAllowRequest(accessIpMapper.accessIpToResponse(accessIp),
                  JobType.DELETE));
              return accessIpDomainService.save(accessIp).map(accessIpMapper::accessIpToResponse);
            });
  }

  @Transactional
  public Mono<AccessIpResponse> createAccessIp(AccessIpRequest request, Account account) {
    AccessIp accessIp = makeAccessIpBuilder(request).id(UUID.randomUUID().toString()).build();

    accessIp.setCreateDate(LocalDateTime.now());
    accessIp.setCreateAdminAccountId(account.getAccountId());

    return accessIpDomainService.insert(accessIp).map(accessIpMapper::accessIpToResponse)
        .flatMap(accessIpResponse -> {
          accessIpResponse.setValidStartDate(accessIpResponse.getValidStartDate());
          accessIpResponse.setValidEndDate(accessIpResponse.getValidEndDate());
          sendMessageToSqs(makeAccessAllowRequest(accessIpResponse, JobType.INSERT));
          return Mono.just(accessIpResponse);
        });
  }

  public void sendMessageToSqs(AccessAllowIpRequest accessAllowIpRequest) {
    awsSQSSender.sendMessage(accessAllowIpRequest, UUID.randomUUID().toString());
  }

  private AccessAllowIpRequest makeAccessAllowRequest(AccessIpResponse request, JobType type) {
    return AccessAllowIpRequest.builder()
        .jobType(type)
        .adminAccessId(request.getId())
        .siteId(request.getSiteId())
        .roleId(request.getRoleId())
        .validStartDate(request.getValidStartDate())
        .validEndDate(request.getValidEndDate())
        .allowIp(request.getAllowIp())
        .build();
  }

  private AccessIp.AccessIpBuilder makeAccessIpBuilder(AccessIpRequest request) {
    return AccessIp.builder()
        .roleId(request.getRoleId())
        .allowIp(request.getAllowIp())
        .adminAccountId(request.getAdminAccountId())
        .siteId(request.getSiteId())
        .validStartDate(request.getValidStartDate().atStartOfDay())
        .validEndDate(request.getValidEndDate().atStartOfDay());
  }

  private List<AccessIpListResponse> makeAccessIpList(
      Map<String, List<AccessIpListResponse>> accessIpListMap) {
    return accessIpListMap.values().stream().map(
        accessIpListResponses -> {
          AccessIpListResponse accessIpListResponse = accessIpListResponses.get(0);
          return AccessIpListResponse.builder()
              .id(accessIpListResponse.getId())
              .siteName(accessIpListResponse.getSiteName())
              .name(accessIpListResponse.getName())
              .email(accessIpListResponse.getEmail())
              .roleId(accessIpListResponse.getRoleId())
              .adminAccountId(accessIpListResponse.getAdminAccountId())
              .allowIpList(
                  accessIpListResponses.stream().map(AccessIpListResponse::getAllowIp)
                      .collect(Collectors.toList()))
              .build();
        }
    ).collect(Collectors.toList());
  }

}
