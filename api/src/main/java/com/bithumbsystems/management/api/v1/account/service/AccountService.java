package com.bithumbsystems.management.api.v1.account.service;

import static com.bithumbsystems.management.api.core.model.enums.ErrorCode.FAIL_ACCOUNT_REGISTER;
import static com.bithumbsystems.management.api.core.model.enums.ErrorCode.NOT_EXIST_ACCOUNT;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.util.MailService;
import com.bithumbsystems.management.api.v1.account.exception.AccountException;
import com.bithumbsystems.management.api.v1.account.model.request.AccountRegisterRequest;
import com.bithumbsystems.management.api.v1.account.model.response.AccountResponse;
import com.bithumbsystems.management.api.v1.account.model.response.AccountSearchResponse;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccessDomainService;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccountDomainService;
import com.bithumbsystems.persistence.mongodb.role.service.RoleManagementDomainService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

  private final MailService mailService;

  /**
   * Search mono.
   *
   * @param searchText the search text
   * @return the mono
   */
  public Mono<List<AccountSearchResponse>> search(String searchText) {
    return adminAccountDomainService.findBySearchText(searchText)
        .flatMap(adminAccount -> adminAccessDomainService.findByAdminAccountId(adminAccount.getId())
            .flatMap(adminAccess -> roleManagementDomainService.findById(adminAccess.getRoleManagementId())
                .map(roleManagement -> new AccountSearchResponse(
                    adminAccount.getId(),
                    adminAccount.getName(),
                    adminAccount.getEmail(),
                    adminAccount.getLastLoginDate(),
                    adminAccount.getStatus(),
                    roleManagement.getName(),
                    roleManagement.getValidEndDate()
                ))).switchIfEmpty(Mono.just(new AccountSearchResponse(
                    adminAccount.getId(),
                    adminAccount.getName(),
                    adminAccount.getEmail(),
                    adminAccount.getLastLoginDate(),
                    adminAccount.getStatus()
                )
            )))
        .collectList();
  }

  /**
   * Create access account mono.
   *
   * @param accountRegisterRequest the account register request
   * @param account                the account
   * @return the mono
   */
  @Transactional
  public Mono<AccountResponse> createAccessAccount(AccountRegisterRequest accountRegisterRequest, Account account) {
    return adminAccountDomainService.findByAdminAccountId(accountRegisterRequest.getAdminAccountId())
        .switchIfEmpty(Mono.error(new AccountException(NOT_EXIST_ACCOUNT)))
        .flatMap(
        adminAccount -> {
          adminAccount.setUpdateAdminAccountId(account.getAccountId());
          adminAccount.setUpdateDate(LocalDateTime.now());
          adminAccount.setLastPasswordUpdateDate(LocalDateTime.now());
          adminAccount.setStatusByIsUse(accountRegisterRequest.getIsUse());
          adminAccount.setOldPassword(adminAccount.getPassword().trim());
          adminAccount.setPassword(accountRegisterRequest.getPassword().trim());

          return adminAccountDomainService.update(adminAccount, account.getAccountId()).zipWith(
              adminAccessDomainService.save(AdminAccess.builder()
                  .adminAccountId(adminAccount.getId())
                  .name(adminAccount.getName().trim())
                  .email(adminAccount.getEmail().trim())
                  .roleManagementId(accountRegisterRequest.getRoleManagementId())
                  .createDate(LocalDateTime.now())
                  .isUse(accountRegisterRequest.getIsUse())
                  .createAdminAccountId(account.getAccountId())
                  .build(), account.getAccountId())
          );
        }
    ).doOnSuccess((a) -> {
      if(accountRegisterRequest.getIsSendMail()) {
        log.info("send mail");
        mailService.send(a.getT1().getEmail());
      }
    }).flatMap(tuple -> {
      AdminAccount adminAccount = tuple.getT1();
      AdminAccess adminAccess = tuple.getT2();
        return roleManagementDomainService.findById(adminAccess.getRoleManagementId())
            .map(roleManagement -> AccountResponse.builder()
                .email(adminAccount.getEmail())
                .roleManagementName(roleManagement.getName())
                .lastLoginDate(adminAccount.getLastLoginDate())
                .name(adminAccount.getName())
                .status(adminAccount.getStatus())
                .build()
        );
    }).doOnCancel(() -> Mono.error(new AccountException(FAIL_ACCOUNT_REGISTER)));
  }

  /**
   * All list mono.
   *
   * @return the mono
   */
  public Mono<List<AccountResponse>> allList() {
    return adminAccessDomainService.findAll()
        .flatMap(adminAccess -> {
          var account = adminAccountDomainService.findByAdminAccountId(adminAccess.getAdminAccountId());
          var role = roleManagementDomainService.findById(adminAccess.getRoleManagementId());
          return account.zipWith(role)
              .map(tuple2 -> AccountResponse.builder()
                  .status(tuple2.getT1().getStatus())
                  .roleManagementName(tuple2.getT2().getName())
                  .lastLoginDate(tuple2.getT1().getLastLoginDate())
                  .email(tuple2.getT1().getEmail())
                  .name(tuple2.getT1().getName())
                  .build());
        })
        .collectList();
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
}