package com.bithumbsystems.management.api.v1.account.service;

import static com.bithumbsystems.management.api.core.model.enums.ErrorCode.FAIL_ACCOUNT_REGISTER;
import static com.bithumbsystems.management.api.core.model.enums.ErrorCode.FAIL_PASSWORD_UPDATE;
import static com.bithumbsystems.management.api.core.model.enums.ErrorCode.INVALID_PASSWORD;
import static com.bithumbsystems.management.api.core.model.enums.ErrorCode.NOT_EXIST_ACCOUNT;
import static com.bithumbsystems.management.api.core.model.enums.ErrorCode.NOT_EXIST_ROLE;

import com.bithumbsystems.management.api.core.cipher.RsaCipherService;
import com.bithumbsystems.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.model.enums.MailForm;
import com.bithumbsystems.management.api.core.model.response.OtpResponse;
import com.bithumbsystems.management.api.core.util.AES256Util;
import com.bithumbsystems.management.api.core.util.MaskingUtil;
import com.bithumbsystems.management.api.core.util.OtpUtil;
import com.bithumbsystems.management.api.core.util.message.MessageService;
import com.bithumbsystems.management.api.v1.account.exception.AccountException;
import com.bithumbsystems.management.api.v1.account.model.request.*;
import com.bithumbsystems.management.api.v1.account.model.response.*;
import com.bithumbsystems.management.api.v1.role.exception.RoleManagementException;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccessDomainService;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccountDomainService;
import com.bithumbsystems.persistence.mongodb.role.service.RoleManagementDomainService;
import com.bithumbsystems.persistence.mongodb.rsacipherinfo.entity.RsaCipherInfo;
import com.bithumbsystems.persistence.mongodb.rsacipherinfo.service.RsaCipherInfoDomainService;
import com.bithumbsystems.persistence.mongodb.site.service.SiteDomainService;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Account service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

  private final AdminAccountDomainService adminAccountDomainService;

  private final AdminAccessDomainService adminAccessDomainService;

  private final RoleManagementDomainService roleManagementDomainService;

  private final SiteDomainService siteDomainService;

  private final MessageService messageService;

  private final PasswordEncoder passwordEncoder;
  private final AwsProperties properties;

    private final RsaCipherInfoDomainService rsaCipherInfoDomainService;

    private final RsaCipherService rsaCipherService;
    private static String RSA_CIPHER_KEY = "rsa-cipher-key";

  /**
   * Search mono.
   *
   * @param searchText the search text
   * @param isUse      the is use
   * @return the mono
   */
  public Mono<List<AccountSearchResponse>> search(String searchText, Boolean isUse) {
    return adminAccountDomainService.findBySearchText(searchText, isUse)
        .flatMap(adminAccount -> adminAccessDomainService.findByAdminAccountId(adminAccount.getId())
            .map(adminAccess -> Pair.of(new AccountSearchResponse(
                adminAccount.getId(),
                adminAccount.getName(),
                adminAccount.getEmail(),
                adminAccount.getLastLoginDate(),
                adminAccount.getStatus(),
                adminAccess.getCreateDate(),
                adminAccount.getValidStartDate(),
                adminAccount.getValidEndDate()
            ), adminAccess.getRoles())))
        .flatMap(accountSearchResponseMap -> roleManagementDomainService.findByRoleInIds(
                accountSearchResponseMap.getSecond())
            .flatMap(roleManagement -> {
              accountSearchResponseMap.getFirst().setRoleManagementName(roleManagement.getName());
              accountSearchResponseMap.getFirst()
                  .setRoleValidStartDate(roleManagement.getValidStartDate());
              accountSearchResponseMap.getFirst()
                  .setRoleValidEndDate(roleManagement.getValidEndDate());

              return Mono.just(accountSearchResponseMap.getFirst());
            })
            .switchIfEmpty(Mono.just(accountSearchResponseMap.getFirst()))
        ).collectSortedList(Comparator.comparing(AccountSearchResponse::getCreateDate).reversed());
  }

  /**
   * Search mono.
   *
   * @param searchText the search text
   * @param isUse      the is use
   * @return the mono
   */
  public Mono<List<AccountSearchResponse>> userSearch(String searchText, Boolean isUse) {
    return adminAccountDomainService.findBySearchText(searchText, isUse)
        .flatMap(adminAccount -> Mono.just(
            new AccountSearchResponse(
                adminAccount.getId(),
                adminAccount.getName(),
                adminAccount.getEmail(),
                adminAccount.getLastLoginDate(),
                adminAccount.getStatus(),
                adminAccount.getCreateDate(),
                adminAccount.getValidStartDate(),
                adminAccount.getValidEndDate())
        ))
        .collectSortedList(Comparator.comparing(AccountSearchResponse::getCreateDate).reversed());
  }

  /**
   * ??????????????? ?????? - ???????????? ?????? ??????
   *
   * @param accountId the account id
   * @return mono
   */
  public Mono<List<AccountDetailResponse>> detailData(String accountId) {
    log.debug("detailData => {}", accountId);
    return adminAccountDomainService.findByAdminAccountId(accountId)
        .flatMap(adminAccount -> {
          log.info("adminAccount => {}", adminAccount);
          return adminAccessDomainService.findByAdminAccountId(adminAccount.getId())
              .flatMap(adminAccess -> {
                log.info("adminAccess => {}", adminAccount);
                return roleManagementDomainService.findByRoleInIds(adminAccess.getRoles())
                    .map(roleManagement -> {
                      log.info("Role data => {}", roleManagement);
                      return new AccountDetailResponse(
                          roleManagement.getSiteId(),
                          adminAccount.getId(),
                          adminAccount.getName(),
                          adminAccount.getEmail(),
                          adminAccount.getCreateDate(),
                          adminAccount.getStatus(),
                          roleManagement.getId(),
                          roleManagement.getName(),
                          adminAccount.getIsUse(),
                          adminAccount.getValidStartDate(),
                          adminAccount.getValidEndDate()
                      );
                    }).collectList();
              })
              .switchIfEmpty(Mono.defer(() -> {
                log.info("defer called => {}", adminAccount);
                return Mono.just(List.of(new AccountDetailResponse(
                        "",
                        adminAccount.getId(),
                        adminAccount.getName(),
                        adminAccount.getEmail(),
                        adminAccount.getCreateDate(),
                        adminAccount.getStatus(),
                        "", "",
                        adminAccount.getIsUse()
                    )
                ));
              }));
        })
        .onErrorResume((error) -> {
          error.printStackTrace();
          throw new AccountException(NOT_EXIST_ACCOUNT);
        });
  }

  /**
   * ??????????????? ?????? - ???????????? ?????? ?????? (Role List ??????)
   *
   * @param accountId the account id
   * @return mono
   */
  public Mono<List<AccountDetailRoleResponse>> detailDataRoleList(String accountId) {
    log.debug("detailRoleData => {}", accountId);
    return adminAccessDomainService.findByAdminAccountId(accountId)
        .flatMap(adminAccess -> {
          log.info("adminAccess => {}", adminAccess);
          return roleManagementDomainService.findByRoleInIds(adminAccess.getRoles())
              .flatMap(roleManagement -> {
                log.info("Role data => {}", roleManagement);
                return siteDomainService.findById(roleManagement.getSiteId())
                    .flatMap(siteInfo -> {
                      return Mono.just(new AccountDetailRoleResponse(
                          roleManagement.getSiteId(),
                          siteInfo.getName(),
                          roleManagement.getId(),
                          roleManagement.getName()
                      ));
                    });
              }).collectList();
        })
        .onErrorResume((error) -> {
          error.printStackTrace();
          throw new AccountException(NOT_EXIST_ACCOUNT);
        });
  }

  /**
   * Create access account mono.
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return the mono
   */
  @Transactional
  public Mono<List<AccountResponse>> createAccessAccount(
      AccessRegisterRequest accountRegisterRequest,
      Account account) {
    return adminAccountDomainService.findByAdminAccountId(
            accountRegisterRequest.getAdminAccountId())
        .switchIfEmpty(Mono.error(new AccountException(NOT_EXIST_ACCOUNT)))
        .flatMap(
            adminAccount -> {
              if(!isValidPassword(accountRegisterRequest.getPassword())) {
                return Mono.error(new AccountException(INVALID_PASSWORD));
              }
              adminAccount.setUpdateAdminAccountId(account.getAccountId());
              adminAccount.setUpdateDate(LocalDateTime.now());
              adminAccount.setLastPasswordUpdateDate(LocalDateTime.now());
              adminAccount.setStatusByIsUse(accountRegisterRequest.getIsUse());
              adminAccount.setOldPassword(adminAccount.getPassword().trim());
              adminAccount.setValidStartDate(accountRegisterRequest.getValidStartDate());
              adminAccount.setValidEndDate(accountRegisterRequest.getValidEndDate());
              adminAccount.setPassword(
                  passwordEncoder.encode(accountRegisterRequest.getPassword().trim()));
              return adminAccountDomainService.update(adminAccount, account.getAccountId()).zipWith(
                  adminAccessDomainService.save(AdminAccess.builder()
                      .adminAccountId(adminAccount.getId())
                      .name(adminAccount.getName().trim())
                      .email(adminAccount.getEmail().trim())
                      .roles(accountRegisterRequest.getRoles())
                      .createDate(LocalDateTime.now())
                      .isUse(accountRegisterRequest.getIsUse())
                      .createAdminAccountId(account.getAccountId())
                      .build(), account.getAccountId())
              );
            }
        ).doOnSuccess((a) -> {
          if (accountRegisterRequest.getIsSendMail()) {
            messageService.sendMail(a.getT2().getEmail(), accountRegisterRequest.getPassword(),
                MailForm.DEFAULT);
          }
        }).flatMap(tuple -> {
          AdminAccount adminAccount = tuple.getT1();
          AdminAccess adminAccess = tuple.getT2();
          return roleManagementDomainService.findByRoleInIds(adminAccess.getRoles())
              .flatMap(roleManagement -> Mono.just(AccountResponse.builder()
                  .id(adminAccount.getId())
                  .email(adminAccount.getEmail())
                  .roleManagementName(roleManagement.getName())
                  //.lastLoginDate(adminAccount.getLastLoginDate())
                  .name(adminAccount.getName())
                  .status(adminAccount.getStatus())
                  .build()
              )).switchIfEmpty(Mono.error(new RoleManagementException(NOT_EXIST_ROLE)))
              .collectList();
        }).doOnCancel(() -> Mono.error(new AccountException(FAIL_ACCOUNT_REGISTER)));
  }

    /**
     * ????????? ???????????? ????????? ?????? ????????? ????????????.
     * - ???????????? ????????? ??????.
     * @param accountUpdateRequest the account Update request
     * @param adminAccountId         the admin account id
     * @param account                the account
     * @return mono
     */
    @Transactional
    public Mono<AccessUpdateResponse> updateAccessAccount(AccessUpdateRequest accountUpdateRequest,
                                                           String adminAccountId, Account account) {


        return adminAccountDomainService.findByAdminAccountId(adminAccountId)
                .switchIfEmpty(Mono.error(new AccountException(NOT_EXIST_ACCOUNT)))
                .flatMap(
                        adminAccount -> {
                            return getRsaPrivateKey()
                                    .flatMap(privateKey -> {
                                        OtpResponse otpResponse = OtpUtil.generate(rsaCipherService.decryptRSA(accountUpdateRequest.getEmail(), privateKey),  null);
                                        adminAccount.setName(rsaCipherService.decryptRSA(accountUpdateRequest.getName(), privateKey));
                                        adminAccount.setEmail(rsaCipherService.decryptRSA(accountUpdateRequest.getEmail(), privateKey));
                                        adminAccount.setIsUse(accountUpdateRequest.getIsUse());
                                        adminAccount.setStatus(accountUpdateRequest.getStatus());
                                        if(accountUpdateRequest.getStatus().equals(Status.INIT_OTP_COMPLETE)){
                                            // OTP ????????? ?????? ??? OTP ??? ?????? ??? ?????? ???????????? ???????????? ????????? ???????????? ??????.
                                            adminAccount.setOtpSecretKey(otpResponse.getEncodeKey());
                                        }
                                        if(accountUpdateRequest.getStatus().equals(Status.NORMAL)){
                                            adminAccount.setLoginFailCount(0L);
                                        }
                                        adminAccount.setUpdateAdminAccountId(account.getAccountId());
                                        adminAccount.setUpdateDate(LocalDateTime.now());
                                        adminAccount.setValidStartDate(accountUpdateRequest.getValidStartDate());
                                        adminAccount.setValidEndDate(accountUpdateRequest.getValidEndDate());
                                        return adminAccountDomainService.update(adminAccount, account.getAccountId())
                                                .flatMap(result -> {
                                                    return Mono.just(AccessUpdateResponse.builder()
                                                            .id(result.getId())
                                                            .name(result.getName())
                                                            .email(result.getEmail())
                                                            .status(result.getStatus())
                                                            .validStartDate(result.getValidStartDate())
                                                            .validEndDate(result.getValidEndDate()).build());
                                                }).doOnSuccess((a) -> {
                                                    if(accountUpdateRequest.getStatus().equals(Status.INIT_OTP_COMPLETE)){
                                                        // otp ?????? ??????
                                                        log.info("send mail");
                                                        messageService.sendMail(accountUpdateRequest.getEmail(), otpResponse.getUrl());
                                                    }
                                                }).doOnCancel(() -> Mono.error(new AccountException(FAIL_ACCOUNT_REGISTER)));
                                    });
                        }
                );
    }

  /**
   * ?????? ????????? ???????????? ????????? ????????????.
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return mono
   */
  @Transactional
  public Mono<AccountResponse> createAccount(AccountRegisterRequest accountRegisterRequest,
      Account account) {

      return getRsaPrivateKey()
              .flatMap(privateKey -> {
                  if (!isValidPassword(rsaCipherService.decryptRSA(accountRegisterRequest.getPassword(), privateKey))) {
                      return Mono.error(new AccountException(INVALID_PASSWORD));
                  }
                  AdminAccount adminAccount = new AdminAccount();
                  adminAccount.setId(UUID.randomUUID().toString());
                  adminAccount.setName(rsaCipherService.decryptRSA(accountRegisterRequest.getName(), privateKey));
                  adminAccount.setPassword(passwordEncoder.encode(rsaCipherService.decryptRSA(accountRegisterRequest.getPassword(), privateKey)));     // ???????????? encode
                  adminAccount.setEmail(rsaCipherService.decryptRSA(accountRegisterRequest.getEmail(), privateKey));
                  adminAccount.setIsUse(accountRegisterRequest.getIsUse());
                  adminAccount.setStatus(accountRegisterRequest.getStatus());
                  adminAccount.setOldPassword(null);
                  adminAccount.setOtpSecretKey(null);
                  adminAccount.setLastPasswordUpdateDate(null);
                  adminAccount.setCreateDate(LocalDateTime.now());
                  adminAccount.setCreateAdminAccountId(account.getAccountId());
                  adminAccount.setValidStartDate(accountRegisterRequest.getValidStartDate());
                  adminAccount.setValidEndDate(accountRegisterRequest.getValidEndDate());
                  return adminAccountDomainService.save(adminAccount, account.getAccountId())
                          .flatMap(result -> {
                              return createAdminAccess(accountRegisterRequest, account, result, adminAccount.getName(),
                                      adminAccount.getEmail());
                          })
                          .flatMap(adminAccess -> {
                              return roleManagementDomainService.findByRoleInIds(accountRegisterRequest.getRoles())
                                      .flatMap(roleManagement -> Mono.just(roleManagement.getName())).collectList()
                                      .flatMap(roleNames -> Mono.just(AccountResponse.builder()
                                              .id(adminAccess.getAdminAccountId())
                                              .name(adminAccount.getName())
                                              .email(adminAccount.getEmail())
                                              .roleManagementName(roleNames.toString())
                                              .status(adminAccount.getStatus())
                                              .createDate(adminAccount.getCreateDate())
                                              //.lastLoginDate(adminAccount.getLastLoginDate())
                                              .build()
                                      ));
                          })
                          .doOnSuccess((a) -> {
                              if (accountRegisterRequest.getIsSendMail()) { // ?????? ???????????? ??????
                                  log.info("send mail");
                                  messageService.sendMail(adminAccount.getEmail(), accountRegisterRequest.getPassword(),
                                          MailForm.DEFAULT);
                              }
                          }).doOnCancel(() -> Mono.error(new AccountException(FAIL_ACCOUNT_REGISTER)));
              });


  }
    /**
     * ?????? ????????? ???????????? ????????? ????????????. (?????? ?????? ????????????)
     *
     * @param accountRegisterRequest the account register request
     * @param adminAccountId         the admin account id
     * @param account                the account
     * @return mono
     */
    @Transactional
    public Mono<List<AccountResponse>> updateAccount(AccountRegisterRequest accountRegisterRequest,
                                                     String adminAccountId, Account account) {
        return adminAccountDomainService.findByAdminAccountId(adminAccountId)
                .switchIfEmpty(Mono.error(new AccountException(NOT_EXIST_ACCOUNT)))
                .flatMap(
                        adminAccount -> {
                            return getRsaPrivateKey()
                                    .flatMap(privateKey -> {
                                        OtpResponse otpResponse = OtpUtil.generate(rsaCipherService.decryptRSA(accountRegisterRequest.getEmail(), privateKey), null);

                                        if (StringUtils.hasLength(accountRegisterRequest.getPassword())) {
                                            if(!isValidPassword(rsaCipherService.decryptRSA(accountRegisterRequest.getPassword(), privateKey))) {
                                                return Mono.error(new AccountException(INVALID_PASSWORD));
                                            }
                                            adminAccount.setPassword(passwordEncoder.encode(rsaCipherService.decryptRSA(accountRegisterRequest.getPassword(), privateKey)));
                                            adminAccount.setLastPasswordUpdateDate(LocalDateTime.now());
                                            adminAccount.setOldPassword(adminAccount.getPassword().trim());
                                        }
                                        adminAccount.setName(rsaCipherService.decryptRSA(accountRegisterRequest.getName(), privateKey));
                                        adminAccount.setEmail(rsaCipherService.decryptRSA(accountRegisterRequest.getEmail(), privateKey));
                                        adminAccount.setIsUse(accountRegisterRequest.getIsUse());
                                        adminAccount.setStatus(accountRegisterRequest.getStatus());
                                        if(accountRegisterRequest.getStatus().equals(Status.INIT_OTP_COMPLETE)){
                                            adminAccount.setOtpSecretKey(otpResponse.getEncodeKey());
                                        }
                                        if(accountRegisterRequest.getStatus().equals(Status.NORMAL)){
                                            adminAccount.setLoginFailCount(0L);
                                        }
                                        adminAccount.setUpdateAdminAccountId(account.getAccountId());
                                        adminAccount.setUpdateDate(LocalDateTime.now());
                                        adminAccount.setValidStartDate(accountRegisterRequest.getValidStartDate());
                                        adminAccount.setValidEndDate(accountRegisterRequest.getValidEndDate());
                                        return adminAccountDomainService.update(adminAccount, account.getAccountId()).zipWith(
                                                // admin_account_id, role_management_id, site_id??? ?????????.
                                                adminAccessDomainService.findByAdminAccountId(adminAccount.getId())
                                                        .flatMap(adminAccess -> {  // ????????????

                                                            return adminAccessDomainService.update(AdminAccess.builder()
                                                                    .id(adminAccess.getId())
                                                                    .adminAccountId(adminAccount.getId())
                                                                    .name(adminAccount.getName().trim())
                                                                    .email(adminAccount.getEmail().trim())
                                                                    .roles(accountRegisterRequest.getRoles())
                                                                    .isUse(accountRegisterRequest.getIsUse())
                                                                    .createAdminAccountId(account.getAccountId())
                                                                    .build(), account.getAccountId());
                                                        })
                                                        .switchIfEmpty(
                                                                Mono.defer(
                                                                        () -> createAdminAccess(accountRegisterRequest, account, adminAccount,
                                                                                adminAccount.getName(),
                                                                                adminAccount.getEmail())
                                                                )  // ?????? ??????
                                                        )
                                        ).doOnSuccess((a) -> {
                                            if(accountRegisterRequest.getStatus().equals(Status.INIT_OTP_COMPLETE)){
                                                // otp ?????? ??????
                                                log.info("send mail");
                                                messageService.sendMail(rsaCipherService.decryptRSA(accountRegisterRequest.getEmail(), privateKey), otpResponse.getUrl());
                                            }
                                        }).flatMap(tuple -> {
                                            AdminAccount acm = tuple.getT1();
                                            log.info("acm => {}", acm);
                                            AdminAccess adminAccess = tuple.getT2();
                                            return roleManagementDomainService.findByRoleInIds(adminAccess.getRoles())
                                                    .flatMap(roleManagement -> Mono.just(AccountResponse.builder()
                                                            .id(adminAccess.getAdminAccountId())
                                                            .name(acm.getName())
                                                            .email(acm.getEmail())
                                                            .roleManagementName(roleManagement.getName())
                                                            .status(acm.getStatus())
                                                            .createDate(acm.getCreateDate())
                                                            //.lastLoginDate(adminAccount.getLastLoginDate())
                                                            .build()
                                                    )).collectList();
                                        }).doOnCancel(() -> Mono.error(new AccountException(FAIL_ACCOUNT_REGISTER)));
                                    });
                        }
                );
    }
  /**
   * ??????????????? ????????????.
   *
   * @param accountUpdatePasswordRequest
   * @param account
   * @return
   */
  public Mono<AccountResponse> updateAccountPassword(
      AccountUpdatePasswordRequest accountUpdatePasswordRequest, Account account) {

      return getRsaPrivateKey()
              .flatMap(privateKey -> {
                  return adminAccountDomainService.findByAdminAccountId(account.getAccountId()) //    findByEmail(rsaCipherService.decryptRSA(accountUpdatePasswordRequest.getEmail(), privateKey))
                          .flatMap(result -> {
                              var currentPassword = rsaCipherService.decryptRSA( accountUpdatePasswordRequest.getCurrentPassword(), privateKey);
                              var newPassword = rsaCipherService.decryptRSA(accountUpdatePasswordRequest.getNewPassword(), privateKey);

                              if (!passwordEncoder.matches(currentPassword, result.getPassword())) {
                                  return Mono.error(new AccountException(FAIL_PASSWORD_UPDATE));
                              }
                              if(!isValidPassword(newPassword)) {
                                  return Mono.error(new AccountException(INVALID_PASSWORD));
                              }
                              result.setPassword(passwordEncoder.encode(newPassword));
                              result.setOldPassword(passwordEncoder.encode(currentPassword));
                              result.setUpdateDate(LocalDateTime.now());
                              result.setUpdateAdminAccountId(account.getAccountId());
                              return adminAccountDomainService.update(result, account.getAccountId())
                                      .flatMap(r -> Mono.just(AccountResponse.builder()
                                              .id(result.getId())
                                              .name(result.getName())
                                              .email(result.getEmail())
                                              .build()));
                          }).switchIfEmpty(Mono.error(new AccountException(NOT_EXIST_ACCOUNT)));
              });

  }



  /**
   * ?????? ????????? ???????????? ?????? Role??? ????????????.
   *
   * @param accountRegisterRequest the account register request
   * @param adminAccountId         the admin account id
   * @param account                the account
   * @return mono
   */
  @Transactional
  public Mono<AccountResponse> updateAccountRole(AccountRoleRequest accountRegisterRequest,
      String adminAccountId, Account account) {
    return adminAccessDomainService.findByAdminAccountId(adminAccountId)
        .flatMap(adminAccess -> {  // ????????????
          String[] roles = accountRegisterRequest.getRoleManagementId().split(",");
          log.debug("roles => {}", roles);
          adminAccess.clearRole();
          for (String role : roles) {
            adminAccess.addRole(role);
          }
          log.debug("adminAccess => {}", adminAccess);
          return adminAccessDomainService.update(adminAccess, account.getAccountId())
              .flatMap(result -> Mono.just(AccountResponse.builder()
                  .id(result.getId())
                  .build()));
        })
        .switchIfEmpty(adminAccountDomainService.findByAdminAccountId(adminAccountId)
            .flatMap(res -> {
              String[] roleList = accountRegisterRequest.getRoleManagementId().split(",");
              Set<String> roles = Set.of(String.join("", roleList));
              return adminAccessDomainService.save(AdminAccess.builder()
                      .adminAccountId(res.getId())
                      .name(res.getName())
                      .email(res.getEmail())
                      .roles(roles)
                      .createDate(LocalDateTime.now())
                      .isUse(true)
                      .createAdminAccountId(account.getAccountId()).build(), account.getAccountId())
                  .flatMap(ress -> Mono.just(AccountResponse.builder().id(adminAccountId).build()));
            }));
  }

  /**
   * ?????? ????????? ???????????? ?????? Role List??? ????????????.
   *
   * @param accountRegisterRequest the account register request
   * @param adminAccountId         the admin account id
   * @param account                the account
   * @return mono
   */
  @Transactional
  public Mono<AccountResponse> updateAccountRoles(AccountRolesRequest accountRegisterRequest,
      String adminAccountId, Account account) {
    return adminAccessDomainService.findByAdminAccountId(adminAccountId)
        .flatMap(adminAccess -> {  // ????????????
          List<String> roles = accountRegisterRequest.getRoleManagementId();
          log.debug("roles => {}", roles);
          adminAccess.clearRole();
          for (String role : roles) {
            adminAccess.addRole(role);
          }
          log.debug("adminAccess => {}", adminAccess);
          return adminAccessDomainService.update(adminAccess, account.getAccountId())
              .flatMap(result -> Mono.just(AccountResponse.builder()
                  .id(result.getId())
                  .build()));
        })
        .switchIfEmpty(adminAccountDomainService.findByAdminAccountId(adminAccountId)
            .flatMap(res -> {
              List<String> roleList = accountRegisterRequest.getRoleManagementId();
              Set<String> roles = Set.of(roleList.stream().collect(Collectors.joining()));
              return adminAccessDomainService.save(AdminAccess.builder()
                      .adminAccountId(res.getId())
                      .name(res.getName())
                      .email(res.getEmail())
                      .roles(roles)
                      .createDate(LocalDateTime.now())
                      .isUse(true)
                      .createAdminAccountId(account.getAccountId()).build(), account.getAccountId())
                  .flatMap(ress -> Mono.just(AccountResponse.builder().id(adminAccountId).build()));
            }));
  }

  /**
   * Create admin access mono.
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @param adminAccount           the admin account
   * @param name                   the name
   * @param email                  the email
   * @return the mono
   */
  public Mono<? extends AdminAccess> createAdminAccess(
      AccountRegisterRequest accountRegisterRequest, Account account, AdminAccount adminAccount,
      String name, String email) {
    return adminAccessDomainService.save(AdminAccess.builder()
        .adminAccountId(adminAccount.getId())
        .name(name.trim())
        .email(email.trim())
        .roles(accountRegisterRequest.getRoles())
        .createDate(LocalDateTime.now())
        .isUse(accountRegisterRequest.getIsUse())
        .createAdminAccountId(account.getAccountId())
        .build(), account.getAccountId());
  }

  /**
   * ??????????????? ?????? - ???????????? (??????)
   *
   * @param adminAccountIdList the admin account id list
   * @param account            the account
   * @return mono
   */
  public Mono<DeleteResponse> deleteAccountList(String adminAccountIdList, Account account) {
    log.debug("delete List => {}", adminAccountIdList);
    String[] idList = adminAccountIdList.split("::");
    log.debug("idList size => {}", idList.length);
    AtomicInteger count = new AtomicInteger(0);

    return Flux.fromArray(idList)
        .flatMap((id) -> {
          return adminAccountDomainService.findByAdminAccountId(id)
              .flatMap((findData) -> {
                count.getAndAdd(1);
                findData.setStatus(Status.DENY_ACCESS);
                findData.setIsUse(false);
                return adminAccountDomainService.update(findData, account.getAccountId())
                    .flatMap(adminAccount -> {
                      return adminAccessDomainService.findByAdminAccountId(findData.getId())
                          .flatMap(list -> {
                            list.setUse(false);
                            return adminAccessDomainService.update(list, account.getAccountId());
                          });
                    });
              });
        })
        .then(Mono.defer(() -> {
          log.debug("count => {}", count.get());
          DeleteResponse res = DeleteResponse.builder()
              .count(Integer.parseInt(String.valueOf(count.get()))).build();
          return Mono.just(res);
        }));
  }

  /**
   * All list mono.
   *
   * @return the mono
   */
  public Mono<List<AccountResponse>> allList() {
    return adminAccessDomainService.findAll()
        .flatMap(adminAccess -> {
          final var account = adminAccountDomainService.findByAdminAccountId(
              adminAccess.getAdminAccountId());
          final var role = roleManagementDomainService.findByRoleInIds(adminAccess.getRoles());

          return role.flatMap(roleManagement -> account.map(adminAccount ->
              AccountResponse.builder()
                  .id(adminAccount.getId())
                  .status(adminAccount.getStatus())
                  .roleManagementName(roleManagement.getName())
                  .lastLoginDate(adminAccount.getLastLoginDate())
                  .email(adminAccount.getEmail())
                  .name(adminAccount.getName())
                  .createDate(adminAccount.getCreateDate())
                  .validStartDate(adminAccount.getValidStartDate())
                  .validEndDate(adminAccount.getValidEndDate())
                  .build()
          ));
        })
        .collectSortedList(Comparator.comparing(AccountResponse::getCreateDate).reversed());
  }

  /**
   * Delete access mono.
   *
   * @param adminAccountId the admin account id
   * @return the mono
   */
  public Mono<Void> deleteAccess(String adminAccountId) {
    return adminAccessDomainService.delete(adminAccountId);
  }

//  /**
//   * ???????????? > Search mono. ???????????? ???????????? ????????? ?????? ?????? ?????? ?????? ?????? ?????? ??????.
//   *
//   * @param searchText the search text
//   * @param isUse      the is use
//   * @return the mono
//   */
//  public Mono<List<AccountSearchResponse>> searchMngNotDupAccount(String searchText,
//      Boolean isUse) {
//    log.debug("searchMngAccount called");
//    return adminAccountDomainService.findBySearchText(searchText, isUse)
//        .flatMap(adminAccount -> {
//              return adminAccessDomainService.findByAdminAccountId(adminAccount.getId())
//                  .switchIfEmpty(Mono.defer(() -> {
//                        AdminAccess access = AdminAccess.builder().build();
//                        return Mono.just(access);
//                      })
//                  )
//                  .map(adminAccess -> {
//                    log.debug("adminAccess search => {}", adminAccess);
//                    return Pair.of(new AccountSearchResponse(
//                        adminAccount.getId(),
//                        adminAccount.getName(),
//                        adminAccount.getEmail(),
//                        adminAccount.getLastLoginDate(),
//                        adminAccount.getStatus(),
//                        adminAccess.getCreateDate() != null ? adminAccess.getCreateDate()
//                            : adminAccount.getCreateDate()
//                    ), adminAccess.getRoles() == null ? "" : adminAccess.getRoles());
//                  });
//            }
//        )
//        .flatMap(accountSearchResponseMap -> {
//              if (!accountSearchResponseMap.getSecond().equals("")) {
//                return roleManagementDomainService.findByRoleInIds(
//                        (Set<String>) accountSearchResponseMap.getSecond())
//                    .flatMap(roleManagement -> {
//                      accountSearchResponseMap.getFirst()
//                          .setRoleManagementName(roleManagement.getName());
//                      accountSearchResponseMap.getFirst()
//                          .setRoleValidStartDate(roleManagement.getValidStartDate());
//                      accountSearchResponseMap.getFirst()
//                          .setRoleValidEndDate(roleManagement.getValidEndDate());
//
//                      return Mono.just(accountSearchResponseMap.getFirst());
//                    });
//              } else {
//                log.debug("accountSearchResponseMap second is null");
//                return Mono.just(accountSearchResponseMap.getFirst());
//              }
//            }
//        )
//        .collectSortedList(Comparator.comparing(AccountSearchResponse::getCreateDate));
//  }

  /**
   * ???????????? > Search mono. ???????????? ???????????? ????????? ?????? ?????? ?????? ?????? ?????? ?????? ??????. Role??? ????????? ????????? ,??? ???????????? ?????????.
   *
   * @param searchText the search text
   * @param isUse      the is use
   * @return the mono
   */
  public Mono<List<AccountSearchResponse>> searchMngAccount(String searchText, Boolean isUse) {
    log.debug("searchMngAccount called");
    return adminAccountDomainService.findBySearchText(searchText, isUse)
        .flatMap(adminAccount -> {
              return adminAccessDomainService.findByAdminAccountId(adminAccount.getId())
                  .switchIfEmpty(Mono.defer(() -> {
                        AdminAccess access = AdminAccess.builder().build();
                        return Mono.just(access);
                      })
                  )
                  .map(adminAccess -> {
                    log.debug("adminAccess search => {}", adminAccess);
                    return Pair.of(new AccountSearchResponse(
                        adminAccount.getId(),
                        MaskingUtil.getNameMask(adminAccount.getName()),
                        MaskingUtil.getEmailMask(adminAccount.getEmail()),
                        adminAccount.getLastLoginDate(),
                        adminAccount.getStatus(),
                        adminAccess.getCreateDate() != null ? adminAccess.getCreateDate()
                            : adminAccount.getCreateDate(),
                        adminAccount.getValidStartDate(),
                        adminAccount.getValidEndDate()
                    ), adminAccess.getRoles() == null ? "" : adminAccess.getRoles());
                  });
            }
        )
        .flatMap(accountSearchResponseMap -> {
              if (!accountSearchResponseMap.getSecond().equals("")) {
                Mono<AccountSearchResponse> res = Mono.just(accountSearchResponseMap.getFirst());

                var roleList = roleManagementDomainService.findByRoleInIds(
                        (Set<String>) accountSearchResponseMap.getSecond())
                    .map(roleManagement -> roleManagement.getName()).collectList();

                return res.zipWith(roleList)
                    .map(tuple -> {
                      tuple.getT1().setRoleManagementName(
                          tuple.getT2().stream().map(String::valueOf)
                              .collect(Collectors.joining(",")));
                      return tuple.getT1();
                    });
              } else {
                log.debug("accountSearchResponseMap second is null");
                return Mono.just(accountSearchResponseMap.getFirst());
              }
            }
        )
        .collectSortedList(Comparator.comparing(AccountSearchResponse::getCreateDate).reversed());
  }

  /**
   * ???????????? > ???????????? : ????????? ?????? (????????? ????????? ?????? ??????)
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return the mono
   */
  @Transactional
  public Mono<AdminAccount> createMngAccount(AccountMngRegisterRequest accountRegisterRequest,
      Account account) {

        return getRsaPrivateKey()
                .flatMap(privateKey -> {
                    if(!isValidPassword(rsaCipherService.decryptRSA(accountRegisterRequest.getPassword(), privateKey))) {
                        return Mono.error(new AccountException(INVALID_PASSWORD));
                    }
                    AdminAccount adminAccount = new AdminAccount();
                    adminAccount.setId(UUID.randomUUID().toString());
                    adminAccount.setName(rsaCipherService.decryptRSA(accountRegisterRequest.getName(), privateKey ));
                    adminAccount.setPassword(passwordEncoder.encode(rsaCipherService.decryptRSA(accountRegisterRequest.getPassword(), privateKey)));
                    adminAccount.setEmail(rsaCipherService.decryptRSA(accountRegisterRequest.getEmail(), privateKey));
                    adminAccount.setIsUse(accountRegisterRequest.getIsUse());
                    adminAccount.setStatus(accountRegisterRequest.getStatus());
                    adminAccount.setOldPassword(null);
                    adminAccount.setOtpSecretKey(null);
                    adminAccount.setLastPasswordUpdateDate(null);
                    adminAccount.setCreateDate(LocalDateTime.now());
                    adminAccount.setCreateAdminAccountId(account.getAccountId());
                    adminAccount.setValidStartDate(accountRegisterRequest.getValidStartDate());
                    adminAccount.setValidEndDate(accountRegisterRequest.getValidEndDate());
                    return adminAccountDomainService.save(adminAccount, account.getAccountId());
                });
  }

  /**
   * ????????? ?????? ????????? ????????????.
   *
   * @param adminAccountId the admin account id
   * @return mono
   */
  public Mono<AdminAccount> findByMngAccountId(String adminAccountId) {
    return adminAccountDomainService.findByAdminAccountId(adminAccountId);
  }

  /**
   * ????????? ?????? ??????
   *
   * @param accountMngUpdateRequest the account mng update request
   * @param adminAccountId          the admin account id
   * @param account                 the account
   * @return mono
   */
  public Mono<AdminAccount> updateMngAccount(AccountMngUpdateRequest accountMngUpdateRequest,
      String adminAccountId, Account account) {
    return adminAccountDomainService.findByAdminAccountId(adminAccountId)
        .flatMap(result -> {
            return getRsaPrivateKey()
                    .flatMap(privateKey -> {
                        if ((StringUtils.hasLength(accountMngUpdateRequest.getPassword()))) {
                            if(!isValidPassword(rsaCipherService.decryptRSA(accountMngUpdateRequest.getPassword(), privateKey))) {
                                return Mono.error(new AccountException(INVALID_PASSWORD));
                            }
                            result.setPassword(passwordEncoder.encode(rsaCipherService.decryptRSA(accountMngUpdateRequest.getPassword(), privateKey)));
                            result.setLastPasswordUpdateDate(LocalDateTime.now());
                        }
                        // ??????????????? ??? ??????????????? ????????? ??? ??????.
                        result.setName(rsaCipherService.decryptRSA(accountMngUpdateRequest.getName(), privateKey));
                        result.setIsUse(accountMngUpdateRequest.getIsUse());
                        if (Boolean.TRUE.equals(accountMngUpdateRequest.getIsUse())) {
                            result.setStatus(Status.NORMAL);
                        }
                        result.setValidStartDate(accountMngUpdateRequest.getValidStartDate());
                        result.setValidEndDate(accountMngUpdateRequest.getValidEndDate());
                        return adminAccountDomainService.update(result, account.getAccountId());
                    });

        });
  }

  /**
   * ???????????? - ???????????? (??????)
   *
   * @param adminAccountIdList the admin account id list
   * @param account            the account
   * @return mono
   */
  public Mono<DeleteResponse> deleteMngAccountList(String adminAccountIdList, Account account) {
    log.debug("delete List => {}", adminAccountIdList);
    String[] idList = adminAccountIdList.split("::");
    log.debug("idList size => {}", idList.length);
    AtomicInteger count = new AtomicInteger(0);

    return Flux.fromArray(idList)
        .flatMap(id -> adminAccountDomainService.findByAdminAccountId(id)
            .flatMap(findData -> {
              count.getAndAdd(1);
              findData.setStatus(Status.DENY_ACCESS);
              findData.setIsUse(false);
              return adminAccountDomainService.update(findData, account.getAccountId());
            }))
        .then(Mono.defer(() -> {
          log.debug("count => {}", count.get());
          DeleteResponse res = DeleteResponse.builder()
              .count(Integer.parseInt(String.valueOf(count.get()))).build();
          return Mono.just(res);
        }));
  }

    /**
     * return RSA Private Key
     * @return
     */
    private Mono<String> getRsaPrivateKey() {
        return rsaCipherInfoDomainService.findById(RSA_CIPHER_KEY)
                .map(RsaCipherInfo::getServerPrivateKey);
    }

  private static boolean isValidPassword(String password) {
    var regex = "^.*(?=^.{8,64}$)(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^*]).*$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(password);
    return matcher.matches();
  }
}