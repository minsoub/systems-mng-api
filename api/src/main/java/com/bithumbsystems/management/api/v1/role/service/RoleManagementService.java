package com.bithumbsystems.management.api.v1.role.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.management.api.v1.role.exception.RoleManagementException;
import com.bithumbsystems.management.api.v1.role.model.mapper.RoleMapper;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementRegisterRequest;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementUpdateRequest;
import com.bithumbsystems.management.api.v1.role.model.request.RoleResourceRequest;
import com.bithumbsystems.management.api.v1.role.model.response.MenuResourceResponse;
import com.bithumbsystems.management.api.v1.role.model.response.ProgramResourceResponse;
import com.bithumbsystems.management.api.v1.role.model.response.RoleAccessResponse;
import com.bithumbsystems.management.api.v1.role.model.response.RoleManagementResponse;
import com.bithumbsystems.management.api.v1.role.model.response.RoleMappingAccountResponse;
import com.bithumbsystems.management.api.v1.role.model.response.RoleMappingResourceResponse;
import com.bithumbsystems.management.api.v1.role.model.response.RoleResourceResponse;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccessDomainService;
import com.bithumbsystems.persistence.mongodb.menu.service.MenuDomainService;
import com.bithumbsystems.persistence.mongodb.menu.service.ProgramDomainService;
import com.bithumbsystems.persistence.mongodb.role.model.entity.AuthorizationResource;
import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleAuthorization;
import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import com.bithumbsystems.persistence.mongodb.role.service.RoleAuthorizationDomainService;
import com.bithumbsystems.persistence.mongodb.role.service.RoleManagementDomainService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * The type Role management service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleManagementService {

  private final RoleManagementDomainService roleManagementDomainService;
  private final AdminAccessDomainService adminAccessDomainService;
  private final MenuDomainService menuDomainService;
  private final ProgramDomainService programDomainService;
  private final RoleAuthorizationDomainService roleAuthorizationDomainService;

  /**
   * Check duplicate mono.
   *
   * @param roleId the role id
   * @return the mono
   */
  public Mono<Boolean> checkDuplicate(String roleId) {
    return roleManagementDomainService.isExist(roleId);
  }

  /**
   * Create mono.
   *
   * @param registerRequest the register request
   * @param account         the account
   * @return the mono
   */
  public Mono<RoleManagement> create(Mono<RoleManagementRegisterRequest> registerRequest,
      Account account) {
    return registerRequest.map(RoleMapper.INSTANCE::registerRequestToRoleManagement)
        .flatMap(roleManagement ->
            roleManagementDomainService.save(roleManagement, account.getAccountId()));
  }

  /**
   * Update mono.
   *
   * @param updateRequest    the update request
   * @param account          the account
   * @param roleManagementId the role management id
   * @return the mono
   */
  public Mono<RoleManagement> update(Mono<RoleManagementUpdateRequest> updateRequest,
      Account account, String roleManagementId) {
    return updateRequest.map(RoleMapper.INSTANCE::updateRequestToRoleManagement)
        .flatMap(roleManagement ->
            roleManagementDomainService.update(roleManagement, account.getAccountId(),
                roleManagementId));
  }

  /**
   * Gets role managements.
   *
   * @param siteId the site id
   * @param isUse  the is use
   * @param type   the type
   * @return the role managements
   */
  public Mono<List<RoleManagementResponse>> getRoleManagements(String siteId, Boolean isUse,
      String type) {
    return roleManagementDomainService.findBySiteIdAndIsUseAndType(siteId, isUse, type)
        .flatMap(roleManagement ->
            Mono.just(RoleMapper.INSTANCE.roleManagementToResponse(roleManagement)))
        .collectList();
  }

  /**
   * 등록된 Role의 사용자 리스트를 가져온다.
   *
   * @param roleManagementId the role management id
   * @return access user list
   */
  public Mono<List<RoleAccessResponse>> getAccessUserList(String roleManagementId) {
    return adminAccessDomainService.findByRoleManagementId(roleManagementId)
        .flatMap(roleAccess ->
            Mono.just(RoleMapper.INSTANCE.roleAccessToResponse(roleAccess)))
        .collectList();
  }

  /**
   * Gets one.
   *
   * @param roleManagementId the role management id
   * @return the one
   */
  public Mono<RoleManagement> getOne(String roleManagementId) {
    return roleManagementDomainService.findById(roleManagementId);
  }

  /**
   * Mapping accounts mono.
   *
   * @param accounts         the accounts
   * @param roleManagementId the role management id
   * @param account          the account
   * @return the mono
   */
  @Transactional
  public Mono<RoleMappingAccountResponse> mappingAccounts(List<String> accounts,
      String roleManagementId, Account account) {
    var roleManagementMappingResponse = roleManagementDomainService.findById(roleManagementId)
        .switchIfEmpty(Mono.error(new RoleManagementException(ErrorCode.NOT_EXIST_ROLE)))
        .map(roleManagement -> RoleMappingAccountResponse.builder()
            .id(roleManagementId)
            .name(roleManagement.getName())
            .build());

    var getAccountEmails = adminAccessDomainService.findByAdminAccountIds(accounts)
        .flatMap(adminAccess -> {
          log.info(adminAccess.getEmail());
          adminAccess.setRoleManagementId(roleManagementId);
          return adminAccessDomainService.update(adminAccess, account.getAccountId()).map(
              AdminAccess::getEmail);
        })
        .collectList();

    return roleManagementMappingResponse.zipWith(getAccountEmails)
        .map(tuple -> {
          tuple.getT1().setEmailList(tuple.getT2());
          return tuple.getT1();
        });
  }

  /**
   * Gets resources.
   *
   * @param roleManagementId the role management id
   * @return the resources
   */
  public Mono<RoleMappingResourceResponse> getResources(String roleManagementId) {
    var roleMappingResourceResponse = RoleMappingResourceResponse.builder()
        .menuList(new ArrayList<>())
        .roleManagementId(roleManagementId)
        .build();
    var roleAuthorization = roleAuthorizationDomainService.findByRoleManagementId(roleManagementId);
    var allMenuList =
        menuDomainService.findAll()
            .flatMap(menu -> Mono.just(MenuResourceResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .build()))
            .publishOn(Schedulers.boundedElastic())
            .doOnNext(menu -> roleAuthorization.flatMap(r -> {
              var visible = r.getAuthorizationResources().stream()
                  .anyMatch(a -> a.getMenuId().equals(menu.getId()));
              log.info("v {}", visible);
              menu.setVisible(visible);
              return Mono.just(menu);
            }).subscribe())
            .flatMap(menuResource -> programDomainService.findMenuPrograms(menuResource.getId())
                .flatMap(program -> Mono.just(ProgramResourceResponse.builder()
                    .id(program.getId())
                    .name(program.getName())
                    .type(program.getType())
                    .kindName(program.getKindName())
                    .actionMethod(program.getActionMethod())
                    .actionUrl(program.getActionUrl())
                    .description(program.getDescription())
                    .build()))
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(program -> roleAuthorization.flatMap(r -> {
                  var isCheck = r.getAuthorizationResources().stream()
                      .anyMatch(a -> a.getProgramId().contains(program.getId()));
                  log.info("isCheck {}", isCheck);
                  program.setIsCheck(isCheck);
                  return Mono.just(program);
                }).subscribe())
                .collectList()
                .flatMap(c -> {
                  menuResource.setProgramList(c);
                  return Mono.just(menuResource);
                }))
            .collectList()
            .flatMap(Mono::just);

    return allMenuList.flatMap(list -> {
      roleMappingResourceResponse.setMenuList(list);
      return Mono.just(roleMappingResourceResponse);
    });
  }

  /**
   * Mapping resources mono.
   *
   * @param roleResourceRequests the role resource requests
   * @param roleManagementId     the role management id
   * @param account              the account
   * @return the mono
   */
  public Mono<List<RoleResourceResponse>> mappingResources(
      Flux<RoleResourceRequest> roleResourceRequests,
      String roleManagementId,
      Account account) {
    final var roleAuthorization = RoleAuthorization.builder()
        .roleManagementId(roleManagementId)
        .build();
    return roleAuthorizationDomainService.deleteByRoleManagementId(roleManagementId)
        .then(roleResourceRequests.flatMap(
                roleResourceRequest -> Mono.just(AuthorizationResource.builder()
                    .menuId(roleResourceRequest.getMenuId())
                    .visible(true)
                    .programId(roleResourceRequest.getProgramId())
                    .build()))
            .collectList()
            .flatMap(authorizationResources -> {
              roleAuthorization.setAuthorizationResources(authorizationResources);
              return roleAuthorizationDomainService.save(roleAuthorization, account.getAccountId());
            }).flatMap(authorization -> Mono.just(authorization.getAuthorizationResources()
                .stream()
                .map(RoleMapper.INSTANCE::resourceToRoleResourceResponse)
                .collect(Collectors.toList()))));
  }
}