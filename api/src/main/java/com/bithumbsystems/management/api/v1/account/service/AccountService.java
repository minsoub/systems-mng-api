package com.bithumbsystems.management.api.v1.account.service;

import static com.bithumbsystems.management.api.core.model.enums.ErrorCode.FAIL_ACCOUNT_REGISTER;
import static com.bithumbsystems.management.api.core.model.enums.ErrorCode.NOT_EXIST_ACCOUNT;
import static com.bithumbsystems.management.api.core.model.enums.ErrorCode.NOT_EXIST_ROLE;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.exception.MailException;
import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.management.api.core.model.enums.MailForm;
import com.bithumbsystems.management.api.core.util.FileUtil;
import com.bithumbsystems.management.api.core.util.message.MailSenderInfo;
import com.bithumbsystems.management.api.core.util.message.MessageService;
import com.bithumbsystems.management.api.v1.account.exception.AccountException;
import com.bithumbsystems.management.api.v1.account.model.request.AccessRegisterRequest;
import com.bithumbsystems.management.api.v1.account.model.request.AccountMngRegisterRequest;
import com.bithumbsystems.management.api.v1.account.model.request.AccountMngUpdateRequest;
import com.bithumbsystems.management.api.v1.account.model.request.AccountRegisterRequest;
import com.bithumbsystems.management.api.v1.account.model.response.AccountDetailResponse;
import com.bithumbsystems.management.api.v1.account.model.response.AccountResponse;
import com.bithumbsystems.management.api.v1.account.model.response.AccountSearchResponse;
import com.bithumbsystems.management.api.v1.account.model.response.DeleteResponse;
import com.bithumbsystems.management.api.v1.role.exception.RoleManagementException;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccessDomainService;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccountDomainService;
import com.bithumbsystems.persistence.mongodb.role.service.RoleManagementDomainService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

  private final MessageService messageService;

  private final PasswordEncoder passwordEncoder;

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
            adminAccess.getCreateDate()
        ), adminAccess.getRoles())))
         .flatMap(accountSearchResponseMap -> roleManagementDomainService.findByRoleInIds(accountSearchResponseMap.getSecond())
                      .flatMap(roleManagement -> {
                        accountSearchResponseMap.getFirst().setRoleManagementName(roleManagement.getName());
                        accountSearchResponseMap.getFirst().setValidStartDate(roleManagement.getValidStartDate());
                        accountSearchResponseMap.getFirst().setValidEndDate(roleManagement.getValidEndDate());

                        return Mono.just(accountSearchResponseMap.getFirst());
                      })
         ).collectSortedList(Comparator.comparing(AccountSearchResponse::getCreateDate));
  }

  /**
   * 통합시스템 관리 - 계정관리 상세 조회
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
                          adminAccount.getIsUse()
                      );
                    }).collectList();
              })
              .log()
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
   * Create access account mono.
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return the mono
   */
  @Transactional
  public Mono<List<AccountResponse>> createAccessAccount(AccessRegisterRequest accountRegisterRequest,
      Account account) {
    return adminAccountDomainService.findByAdminAccountId(
            accountRegisterRequest.getAdminAccountId())
        .switchIfEmpty(Mono.error(new AccountException(NOT_EXIST_ACCOUNT)))
        .flatMap(
            adminAccount -> {
              adminAccount.setUpdateAdminAccountId(account.getAccountId());
              adminAccount.setUpdateDate(LocalDateTime.now());
              adminAccount.setLastPasswordUpdateDate(LocalDateTime.now());
              adminAccount.setStatusByIsUse(accountRegisterRequest.getIsUse());
              adminAccount.setOldPassword(adminAccount.getPassword().trim());
              adminAccount.setPassword(accountRegisterRequest.getPassword().trim());
              Set<String> roles = Set.of(accountRegisterRequest.getRoleManagementId());
              return adminAccountDomainService.update(adminAccount, account.getAccountId()).zipWith(
                  adminAccessDomainService.save(AdminAccess.builder()
                      .adminAccountId(adminAccount.getId())
                      .name(adminAccount.getName().trim())
                      .email(adminAccount.getEmail().trim())
                      .roles(roles)
                      .createDate(LocalDateTime.now())
                      .isUse(accountRegisterRequest.getIsUse())
                      .createAdminAccountId(account.getAccountId())
                      .build(), account.getAccountId())
              );
            }
        ).doOnSuccess((a) -> {
          if (accountRegisterRequest.getIsSendMail()) {
            sendMail(a.getT2().getEmail());
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
              )).switchIfEmpty(Mono.error(new RoleManagementException(NOT_EXIST_ROLE))).collectList();
        }).doOnCancel(() -> Mono.error(new AccountException(FAIL_ACCOUNT_REGISTER)));
  }

  /**
   * 통합 어드민 관리자가 계정을 등록한다.
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return mono
   */
  @Transactional
  public Mono<AccountResponse> createAccount(AccountRegisterRequest accountRegisterRequest,
      Account account) {
    AdminAccount adminAccount = new AdminAccount();
    adminAccount.setName(accountRegisterRequest.getName());
    adminAccount.setPassword(
        passwordEncoder.encode(accountRegisterRequest.getPassword()));     // 패스워드 encode
    adminAccount.setEmail(accountRegisterRequest.getEmail());
    adminAccount.setIsUse(accountRegisterRequest.getIsUse());
    adminAccount.setStatus(accountRegisterRequest.getStatus());
    adminAccount.setOldPassword("");
    adminAccount.setOtpSecretKey("");
    adminAccount.setLastPasswordUpdateDate(null);
    adminAccount.setCreateDate(LocalDateTime.now());
    adminAccount.setCreateAdminAccountId(account.getAccountId());
    return adminAccountDomainService.save(adminAccount, account.getAccountId())
        .flatMap(result -> {
          return createAdminAccess(accountRegisterRequest, account, result, adminAccount.getName(),
              adminAccount.getEmail());
        })
        .flatMap(adminAccess -> {
          return roleManagementDomainService.findById(accountRegisterRequest.getRoleManagementId())
              .map(roleManagement -> AccountResponse.builder()
                  .id(adminAccess.getAdminAccountId())
                  .name(adminAccount.getName())
                  .email(adminAccount.getEmail())
                  .roleManagementName(roleManagement.getName())
                  .status(adminAccount.getStatus())
                  .createDate(adminAccount.getCreateDate())
                  //.lastLoginDate(adminAccount.getLastLoginDate())
                  .build()
              );
        })
        .doOnSuccess((a) -> {
          if (accountRegisterRequest.getIsSendMail()) {
            log.info("send mail");
            sendMail(adminAccount.getEmail());
          }
        }).doOnCancel(() -> Mono.error(new AccountException(FAIL_ACCOUNT_REGISTER)));
  }

  /**
   * 통합 어드민 관리자가 계정을 수정한다.
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
              adminAccount.setPassword(
                  passwordEncoder.encode(accountRegisterRequest.getPassword()));
              adminAccount.setName(accountRegisterRequest.getName());
              adminAccount.setEmail(accountRegisterRequest.getEmail());
              adminAccount.setIsUse(accountRegisterRequest.getIsUse());
              adminAccount.setStatus(accountRegisterRequest.getStatus());

              adminAccount.setUpdateAdminAccountId(account.getAccountId());
              adminAccount.setUpdateDate(LocalDateTime.now());
              adminAccount.setLastPasswordUpdateDate(LocalDateTime.now());
              //adminAccount.setStatusByIsUse(accountRegisterRequest.getIsUse());
              adminAccount.setOldPassword(adminAccount.getPassword().trim());
              adminAccount.setPassword(accountRegisterRequest.getPassword().trim());

              return adminAccountDomainService.update(adminAccount, account.getAccountId()).zipWith(
                  // admin_account_id, role_management_id, site_id로 찾는다.
                  adminAccessDomainService.findByAdminAccountIdAndRoleManagementId(
                          adminAccount.getId(), accountRegisterRequest.getRoleManagementId()
                      ).flatMap(adminAccess -> {  // 수정모드
                        return adminAccessDomainService.update(AdminAccess.builder()
                            .id(adminAccess.getId())
                            .adminAccountId(adminAccount.getId())
                            .name(adminAccount.getName().trim())
                            .email(adminAccount.getEmail().trim())
                            .roles(adminAccess.getRoles())
                            .isUse(accountRegisterRequest.getIsUse())
                            .createAdminAccountId(account.getAccountId())
                            .build(), account.getAccountId());
                      })
                      .switchIfEmpty(
                          Mono.defer(() -> createAdminAccess(accountRegisterRequest, account, adminAccount,
                              adminAccount.getName(),
                              adminAccount.getEmail())
                          )  // 신규 등록

                      )
              );
            }
        ).doOnSuccess((a) -> {
          if (accountRegisterRequest.getIsSendMail()) {
            log.info("send mail");
            sendMail(a.getT1().getEmail());
          }
        }).flatMap(tuple -> {
          AdminAccount adminAccount = tuple.getT1();
          AdminAccess adminAccess = tuple.getT2();
          return roleManagementDomainService.findByRoleInIds(adminAccess.getRoles())
              .flatMap(roleManagement -> Mono.just(AccountResponse.builder()
                  .id(adminAccess.getAdminAccountId())
                  .name(adminAccount.getName())
                  .email(adminAccount.getEmail())
                  .roleManagementName(roleManagement.getName())
                  .status(adminAccount.getStatus())
                  .createDate(adminAccount.getCreateDate())
                  //.lastLoginDate(adminAccount.getLastLoginDate())
                  .build()
              )).collectList();
        }).doOnCancel(() -> Mono.error(new AccountException(FAIL_ACCOUNT_REGISTER)));
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
    Set<String> roles = Set.of(accountRegisterRequest.getRoleManagementId());
    return adminAccessDomainService.save(AdminAccess.builder()
        .adminAccountId(adminAccount.getId())
        .name(name.trim())
        .email(email.trim())
        .roles(roles)
        .createDate(LocalDateTime.now())
        .isUse(accountRegisterRequest.getIsUse())
        .createAdminAccountId(account.getAccountId())
        .build(), account.getAccountId());
  }

  /**
   * 통합시스템 관리 - 계정삭제 (일괄)
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
          final var account = adminAccountDomainService.findByAdminAccountId(adminAccess.getAdminAccountId());
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
                .build()
          ));
        })
        .collectSortedList(Comparator.comparing(AccountResponse::getCreateDate));
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

  /**
   * 통합관리 > 계정관리 : 사용자 등록 (암호화 처리를 해야 된다)
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return the mono
   */
  @Transactional
  public Mono<AdminAccount> createMngAccount(AccountMngRegisterRequest accountRegisterRequest,
      Account account) {

    AdminAccount adminAccount = new AdminAccount();
    adminAccount.setName(accountRegisterRequest.getName());
    adminAccount.setPassword(passwordEncoder.encode(accountRegisterRequest.getPassword()));
    adminAccount.setEmail(accountRegisterRequest.getEmail());
    adminAccount.setIsUse(accountRegisterRequest.getIsUse());
    adminAccount.setStatus(accountRegisterRequest.getStatus());
    adminAccount.setOldPassword("");
    adminAccount.setOtpSecretKey("");
    adminAccount.setLastPasswordUpdateDate(null);
    adminAccount.setCreateDate(LocalDateTime.now());
    adminAccount.setCreateAdminAccountId(account.getAccountId());
    return adminAccountDomainService.save(adminAccount, account.getAccountId());
  }

  /**
   * 사용자 상세 정보를 조회한다.
   *
   * @param adminAccountId the admin account id
   * @return mono
   */
  public Mono<AdminAccount> findByMngAccountId(String adminAccountId) {
    return adminAccountDomainService.findByAdminAccountId(adminAccountId);
  }

  /**
   * 사용자 정보 수정
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
          result.setPassword(passwordEncoder.encode(accountMngUpdateRequest.getPassword()));
          result.setName(accountMngUpdateRequest.getName());
          result.setIsUse(accountMngUpdateRequest.getIsUse());
          result.setLastPasswordUpdateDate(LocalDateTime.now());
          if (accountMngUpdateRequest.getIsUse() == true) {
            result.setStatus(Status.NORMAL);
          }
          return adminAccountDomainService.update(result, account.getAccountId());
        });
  }

  /**
   * 통합관리 - 계정삭제 (일괄)
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
        .flatMap((id) -> {
          return adminAccountDomainService.findByAdminAccountId(id)
              .flatMap((findData) -> {
                count.getAndAdd(1);
                findData.setStatus(Status.DENY_ACCESS);
                findData.setIsUse(false);
                return adminAccountDomainService.update(findData, account.getAccountId());
              });
        })
        .then(Mono.defer(() -> {
          log.debug("count => {}", count.get());
          DeleteResponse res = DeleteResponse.builder()
              .count(Integer.parseInt(String.valueOf(count.get()))).build();
          return Mono.just(res);
        }));
  }

  private void sendMail(String emailAddress) {
    try {
      String html = FileUtil.readResourceFile(MailForm.DEFAULT.getPath());
      log.info("send mail: " + html);

      messageService.send(
          MailSenderInfo.builder()
              .bodyHTML(html)
              .subject(MailForm.DEFAULT.getSubject())
              .emailAddress(emailAddress)
              .build()
      );
    } catch (MessagingException | IOException e) {
      throw new MailException(ErrorCode.FAIL_SEND_MAIL);
    }
  }
}