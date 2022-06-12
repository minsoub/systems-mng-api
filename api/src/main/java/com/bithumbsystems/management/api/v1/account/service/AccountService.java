package com.bithumbsystems.management.api.v1.account.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.util.AES256Util;
import com.bithumbsystems.management.api.core.util.MailService;
import com.bithumbsystems.management.api.v1.account.exception.AccountException;
import com.bithumbsystems.management.api.v1.account.model.request.AccountMngRegisterRequest;
import com.bithumbsystems.management.api.v1.account.model.request.AccountMngUpdateRequest;
import com.bithumbsystems.management.api.v1.account.model.request.AccountRegisterRequest;
import com.bithumbsystems.management.api.v1.account.model.response.AccountDetailResponse;
import com.bithumbsystems.management.api.v1.account.model.response.AccountResponse;
import com.bithumbsystems.management.api.v1.account.model.response.AccountSearchResponse;
import com.bithumbsystems.management.api.v1.account.model.response.DeleteResponse;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccessDomainService;
import com.bithumbsystems.persistence.mongodb.account.service.AdminAccountDomainService;
import com.bithumbsystems.persistence.mongodb.role.service.RoleManagementDomainService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.bithumbsystems.management.api.core.model.enums.ErrorCode.*;

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

  private final PasswordEncoder passwordEncoder;

  /**
   * Search mono.
   *
   * @param searchText the search text
   * @return the mono
   */
  public Mono<List<AccountSearchResponse>> search(String searchText, Boolean isUse) {
    return adminAccountDomainService.findBySearchText(searchText, isUse)
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
     * 통합시스템 관리 - 계정관리 상세 조회
     * @param accountId
     * @return
     */
    public Mono<AccountDetailResponse> detailData(String accountId) {
        return adminAccountDomainService.findByAdminAccountId(accountId)
                .flatMap(adminAccount -> {
                    return adminAccessDomainService.findByAdminAccountId(adminAccount.getId())
                            .flatMap(adminAccess -> {
                                return roleManagementDomainService.findById(adminAccess.getRoleManagementId())
                                        .map(roleManagement -> {
                                            return new AccountDetailResponse(
                                                    adminAccess.getSiteId(),
                                                    adminAccount.getId(),
                                                    adminAccount.getName(),
                                                    adminAccount.getEmail(),
                                                    adminAccount.getCreateDate(),
                                                    adminAccount.getStatus(),
                                                    roleManagement.getId(),
                                                    roleManagement.getName(),
                                                    adminAccount.getIsUse()
                                            );
                                        });
                            })
                            .switchIfEmpty(Mono.just(new AccountDetailResponse(
                                    "",
                                    adminAccount.getId(),
                                    adminAccount.getName(),
                                    adminAccount.getEmail(),
                                    adminAccount.getCreateDate(),
                                    adminAccount.getStatus(),
                                    "", "",
                                    adminAccount.getIsUse()
                            )));
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
                //.lastLoginDate(adminAccount.getLastLoginDate())
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

    /**
     * 통합관리 > 계정관리 : 사용자 등록 (암호화 처리를 해야 된다)
     *
     * @param accountRegisterRequest the account register request
     * @param account                the account
     * @return the mono
     */
    @Transactional
    public Mono<AdminAccount> createMngAccount(AccountMngRegisterRequest accountRegisterRequest, Account account) {

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
     * @param adminAccountId
     * @return
     */
    public Mono<AdminAccount> findByMngAccountId(String adminAccountId) {
        return adminAccountDomainService.findByAdminAccountId(adminAccountId);
    }

    /**
     * 사용자 정보 수정
     * @param accountMngUpdateRequest
     * @param adminAccountId
     * @param account
     * @return
     */
    public Mono<AdminAccount> updateMngAccount(AccountMngUpdateRequest accountMngUpdateRequest, String adminAccountId, Account account) {
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
     * @param adminAccountIdList
     * @param account
     * @return
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
                    DeleteResponse res = DeleteResponse.builder().count(Integer.parseInt(String.valueOf(count.get()))).build();
                    return Mono.just(res);
                }));
    }
}