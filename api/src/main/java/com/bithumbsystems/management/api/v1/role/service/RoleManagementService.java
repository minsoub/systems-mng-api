package com.bithumbsystems.management.api.v1.role.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.management.api.v1.role.exception.RoleManagementException;
import com.bithumbsystems.management.api.v1.role.model.enums.FlagEnum;
import com.bithumbsystems.management.api.v1.role.model.mapper.RoleMapper;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementRegisterRequest;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementUpdateRequest;
import com.bithumbsystems.management.api.v1.role.model.request.RoleModeAccountRequest;
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
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccountDomainService;
import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import com.bithumbsystems.persistence.mongodb.menu.service.MenuDomainService;
import com.bithumbsystems.persistence.mongodb.menu.service.ProgramDomainService;
import com.bithumbsystems.persistence.mongodb.role.model.entity.AuthorizationResource;
import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleAuthorization;
import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import com.bithumbsystems.persistence.mongodb.role.service.RoleAuthorizationDomainService;
import com.bithumbsystems.persistence.mongodb.role.service.RoleManagementDomainService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Role management service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleManagementService {

  private final RoleManagementDomainService roleManagementDomainService;
  private final AdminAccessDomainService adminAccessDomainService;
  private final AdminAccountDomainService adminAccountDomainService;
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
    return registerRequest.filter(request -> (request.getValidStartDate() != null && request.getValidEndDate() != null))
        .map(RoleMapper.INSTANCE::registerRequestToRoleManagement)
        .flatMap(roleManagement ->
            roleManagementDomainService.save(roleManagement, account.getAccountId())
        ).switchIfEmpty(Mono.error(new RoleManagementException(ErrorCode.INVALID_ROLE)));
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
    return updateRequest.filter(request -> (request.getValidStartDate() != null && request.getValidEndDate() != null))
        .map(RoleMapper.INSTANCE::updateRequestToRoleManagement)
        .flatMap(roleManagement ->
            roleManagementDomainService.update(roleManagement, account.getAccountId(),
                roleManagementId))
        .switchIfEmpty(Mono.error(new RoleManagementException(ErrorCode.INVALID_ROLE)));
  }

  /**
   * Gets role managements.
   *
   * @param siteId the site id
   * @param isUse  the is use
   * @param type   the type
   * @return the role managements
   */
  public Mono<List<RoleManagementResponse>> getRoleManagements(String siteId, String searchText, Boolean isUse,
      String type) {
    return roleManagementDomainService.findBySiteIdSearchTextAndIsUseAndType(siteId, searchText, isUse, type)
        .flatMap(roleManagement ->
            Mono.just(RoleMapper.INSTANCE.roleManagementToResponse(roleManagement)))
        .collectSortedList(Comparator.comparing(RoleManagementResponse::getCreateDate));
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
        .collectSortedList(Comparator.comparing(RoleAccessResponse::getCreateDate));
  }

  /**
   * 사용자 Role을 삭제한다.
   *
   * @param roleManagementId the role management id
   * @param accountId        the account id
   * @param account          the account
   * @return mono
   */
  public Mono<RoleAccessResponse> deleteAccessUserRole(String roleManagementId, String accountId,
      Account account) {
    return adminAccessDomainService.findById(accountId)
        .flatMap(roleAccess -> {
          Set<String> roleList = roleAccess.getRoles();
          log.debug("{}", roleList);
          roleList.remove(roleManagementId);
          roleAccess.setRoles(roleList);
          log.debug("admin => {}", roleAccess);
          return adminAccessDomainService.update(roleAccess, account.getAccountId())
              .flatMap(result -> Mono.just(RoleAccessResponse.builder()
                  .id(accountId)
                  .name(result.getName())
                  .email(result.getEmail())
                  .createDate(result.getCreateDate())
                  .build()));
        });
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

    var getAccountEmails = adminAccountDomainService.findByAdminAccountIds(accounts)
                    .flatMap(adminAccount -> {
                        return adminAccessDomainService.findByAdminAccountId(adminAccount.getId())
                                .flatMap(adminAccess -> {
                                    log.info(adminAccess.getEmail());
                                    adminAccess.getRoles().add(roleManagementId);
                                    return adminAccessDomainService.update(adminAccess, account.getAccountId()).map(AdminAccess::getEmail);
                                })
                                .switchIfEmpty(adminAccessDomainService.save(AdminAccess.builder()
                                        .adminAccountId(adminAccount.getId())
                                        .name(adminAccount.getName())
                                        .email(adminAccount.getEmail())
                                        .isUse(true)
                                        .roles(Set.of(roleManagementId))
                                        .build(), account.getAccountId()).map(AdminAccess::getEmail));
                    })
                   .collectList();
//            adminAccessDomainService.findByAdminAccountIds(accounts)
//        .flatMap(adminAccess -> {
//          log.info(adminAccess.getEmail());
//          adminAccess.getRoles().add(roleManagementId);
//          return adminAccessDomainService.update(adminAccess, account.getAccountId()).map(
//              AdminAccess::getEmail);
//        })
//        .collectList();

    return roleManagementMappingResponse.zipWith(getAccountEmails)
        .map(tuple -> {
          tuple.getT1().setEmailList(tuple.getT2());
          return tuple.getT1();
        });
  }

  @Transactional
  public Mono<RoleMappingAccountResponse> mappingAccountsPuts(List<RoleModeAccountRequest> accounts, String roleManagementId, Account account) {
      var roleManagementMappingResponse = roleManagementDomainService.findById(roleManagementId)
              .switchIfEmpty(Mono.error(new RoleManagementException(ErrorCode.NOT_EXIST_ROLE)))
              .map(roleManagement -> RoleMappingAccountResponse.builder()
                      .id(roleManagementId)
                      .name(roleManagement.getName())
                      .build());

      var getAccountEmails =
              Flux.fromIterable(accounts).flatMap(roleData -> {
                  return adminAccountDomainService.findByAdminAccountId(roleData.getId())
                          .flatMap(adminAccount -> {
                              if (roleData.getFlag().equals(FlagEnum.INSERT)) {
                                  return adminAccessDomainService.findByAdminAccountId(adminAccount.getId())
                                          .flatMap(adminAccess -> {
                                              Set<String> roleList = adminAccess.getRoles(); // my role list

                                              return isRoleRegisterCheck(roleList, roleManagementId)
                                                      .flatMap(res -> {
                                                          if (res.equals(true)) {
                                                              adminAccess.getRoles().add(roleManagementId);
                                                              return adminAccessDomainService.update(adminAccess, account.getAccountId()).map(AdminAccess::getEmail);
                                                          } else {
                                                              return Mono.error(new RoleManagementException(ErrorCode.INVALID_MAX_ROLE));
                                                          }
                                                      });
                                          })
                                          .switchIfEmpty(adminAccessDomainService.save(AdminAccess.builder()
                                                  .adminAccountId(adminAccount.getId())
                                                  .name(adminAccount.getName())
                                                  .email(adminAccount.getEmail())
                                                  .isUse(true)
                                                  .roles(Set.of(roleManagementId))
                                                  .build(), account.getAccountId()).map(AdminAccess::getEmail));
                              } else if (roleData.getFlag().equals(FlagEnum.DELETE)) {
                                  return adminAccessDomainService.findByAdminAccountId(adminAccount.getId())
                                          .flatMap(adminAccess -> {
                                              log.info(adminAccess.getEmail());
                                              Set<String> roleList = adminAccess.getRoles();
                                              log.debug("{}", roleList);
                                              roleList.remove(roleManagementId);
                                              adminAccess.setRoles(roleList);
                                              return adminAccessDomainService.update(adminAccess, account.getAccountId()).map(AdminAccess::getEmail);
                                          });
                              } else {
                                  return Mono.error(new RoleManagementException(ErrorCode.NOT_EXIST_ROLE));
                              }
                          });
              }).collectList();

//            adminAccessDomainService.findByAdminAccountIds(accounts)
//        .flatMap(adminAccess -> {
//          log.info(adminAccess.getEmail());
//          adminAccess.getRoles().add(roleManagementId);
//          return adminAccessDomainService.update(adminAccess, account.getAccountId()).map(
//              AdminAccess::getEmail);
//        })
//        .collectList();

      return roleManagementMappingResponse.zipWith(getAccountEmails)
              .map(tuple -> {
                  tuple.getT1().setEmailList(tuple.getT2());
                  return tuple.getT1();
              });
  }

  private Mono<Boolean> isRoleRegisterCheck(Set<String> roleList, String roleManagementId) {

      return roleManagementDomainService.findById(roleManagementId)
              .flatMap(result -> {
                 return Flux.fromIterable(roleList)
                         .flatMap(res -> {
                             return roleManagementDomainService.findById(res)
                                     .flatMap(check -> {
                                         if (check.getSiteId().equals(result.getSiteId())) { // 동일사이트에 2개의 role은 안됨.
                                             return Mono.just(false);
                                         } else {
                                             return Mono.just(true);
                                         }
                                     });
                         }).collectList();
              }).map(resultCheck -> {
                  if (resultCheck.contains(false)) {
                      return false;
                  } else {
                      return true;
                  }
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
    var allMenuList = getMenuResourceFlux(menuDomainService.findAll(), roleAuthorization)
            .collectSortedList(Comparator.comparing(MenuResourceResponse::getOrder))
            .flatMap(Mono::just);

    return allMenuList.flatMap(list -> {
      roleMappingResourceResponse.setMenuList(list);
      return Mono.just(roleMappingResourceResponse);
    });
  }

  /**
   * Gets resources.
   *
   * @param roleManagementId the role management id
   * @param siteId           the site id
   * @return the resources
   */
  public Mono<RoleMappingResourceResponse> getResources(String roleManagementId, String siteId) {
    var roleMappingResourceResponse = RoleMappingResourceResponse.builder()
        .menuList(new ArrayList<>())
        .roleManagementId(roleManagementId)
        .build();
    var roleAuthorization = roleAuthorizationDomainService.findByRoleManagementId(roleManagementId);
    var allMenuList = getMenuResourceFlux(menuDomainService.findList(siteId, true, ""), roleAuthorization)
        .flatMap(topMenu -> getMenuResourceFlux(menuDomainService.findList(siteId, true, topMenu.getId()), roleAuthorization)
                .flatMap(middleMenu -> getMenuResourceFlux(menuDomainService.findList(siteId, true, middleMenu.getId()), roleAuthorization)
                    .collectSortedList(Comparator.comparing(MenuResourceResponse::getOrder))
                    .flatMap(childResource -> {
                      middleMenu.setChildMenuResources(childResource);
                      return Mono.just(middleMenu);
                    })).collectSortedList(Comparator.comparing(MenuResourceResponse::getOrder))
            .flatMap(middleResource -> {
              topMenu.setChildMenuResources(middleResource);
              return Mono.just(topMenu);
            })).collectSortedList(Comparator.comparing(MenuResourceResponse::getOrder));

    return allMenuList.flatMap(list -> {
      roleMappingResourceResponse.setMenuList(list);
      return Mono.just(roleMappingResourceResponse);
    });
  }

  /**
   * Gets menu resource flux.
   *
   * @param menuFlux              the menu flux
   * @param roleAuthorizationMono the role authorization mono
   * @return the menu resource flux
   */
  private Flux<MenuResourceResponse> getMenuResourceFlux(Flux<Menu> menuFlux, Mono<RoleAuthorization> roleAuthorizationMono) {
    return menuFlux.flatMap(menu -> Mono.just(MenuResourceResponse.builder()
        .id(menu.getId())
        .name(menu.getName())
        .order(menu.getOrder())
        .type(menu.getType())
        .externalLink(menu.getExternalLink())
        .target(menu.getTarget())
        .url(menu.getUrl())
        .parentsMenuId((menu.getParentsMenuId()))
        .visible(false)
        .createDate(menu.getCreateDate())
        .build())
    ).flatMap(menu -> roleAuthorizationMono.flatMap(r -> {
          var visible = r.getAuthorizationResources().stream()
              .anyMatch(a -> a.getMenuId().equals(menu.getId()));
          log.info("v {}", visible);
          menu.setVisible(visible);
          return Mono.just(menu);
        }).switchIfEmpty(Mono.just(menu))
    ).flatMap(menuResource -> programDomainService.findMenuPrograms(menuResource.getId())
        .flatMap(program -> Mono.just(ProgramResourceResponse.builder()
            .id(program.getId())
            .name(program.getName())
            .type(program.getType())
            .kindName(program.getKindName())
            .actionMethod(program.getActionMethod())
            .actionUrl(program.getActionUrl())
            .description(program.getDescription())
            .createDate(program.getCreateDate())
            .isCheck(false)
            .build()))
        .flatMap(program -> roleAuthorizationMono.flatMap(r -> {
              var isCheck = r.getAuthorizationResources().stream()
                  .anyMatch(a -> a.getProgramId().contains(program.getId()));
              log.info("isCheck {}", isCheck);
              program.setIsCheck(isCheck);
              return Mono.just(program);
            }).switchIfEmpty(Mono.just(program))
        )
        .collectSortedList(Comparator.comparing(ProgramResourceResponse::getCreateDate).reversed())
        .flatMap(c -> {
          menuResource.setProgramList(c);
          return Mono.just(menuResource);
        }));
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
      RoleResourceRequest roleResourceRequests,
      String roleManagementId,
      Account account) {
    final var roleAuthorization = RoleAuthorization.builder()
        .roleManagementId(roleManagementId)
        .build();
      return roleAuthorizationDomainService.deleteByRoleManagementId(roleManagementId)
              .then(
                      Flux.fromIterable(roleResourceRequests.getResources())
                                      .flatMap(roleResourceListRequest -> {
                                          return Mono.just(AuthorizationResource.builder()
                                                  .menuId(roleResourceListRequest.getMenuId())
                                                  .visible(true)
                                                  .programId(roleResourceListRequest.getProgramId())
                                                  .build());
                                      })
                                     .collectSortedList(Comparator.comparing(AuthorizationResource::getMenuId))
                                             .flatMap(authorizationResources -> {
                                                 roleAuthorization.setAuthorizationResources(authorizationResources);
                                                 return roleAuthorizationDomainService.save(roleAuthorization, account.getAccountId());
                                             })
                                                     .flatMap(authorization -> Mono.just(authorization.getAuthorizationResources()
                                                             .stream()
                                                             .map(RoleMapper.INSTANCE::resourceToRoleResourceResponse)
                                                             .collect(Collectors.toList()))));


//                      roleResourceRequests.flatMap(
//                              roleResourceRequest -> Mono.just(AuthorizationResource.builder()
//                                      .menuId(roleResourceRequest.getMenuId())
//                                      .visible(true)
//                                      .programId(roleResourceRequest.getProgramId())
//                                      .build()))
//                      .collectSortedList(Comparator.comparing(AuthorizationResource::getMenuId))
//                      .flatMap(authorizationResources -> {
//                          roleAuthorization.setAuthorizationResources(authorizationResources);
//                          return roleAuthorizationDomainService.save(roleAuthorization, account.getAccountId());
//                      }).flatMap(authorization -> Mono.just(authorization.getAuthorizationResources()
//                              .stream()
//                              .map(RoleMapper.INSTANCE::resourceToRoleResourceResponse)
//                              .collect(Collectors.toList()))));
  }
}