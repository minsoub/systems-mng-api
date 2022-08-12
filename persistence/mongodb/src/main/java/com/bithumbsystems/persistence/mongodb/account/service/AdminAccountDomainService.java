package com.bithumbsystems.persistence.mongodb.account.service;

import static com.bithumbsystems.persistence.mongodb.common.util.StringUtil.generateUUIDWithOutDash;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.account.repository.AdminAccountRepository;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Admin account domain service.
 */
@Service
@RequiredArgsConstructor
public class AdminAccountDomainService {

    private final AdminAccountRepository adminAccountRepository;

    private static final String PREFIX = "ACCOUNT_";

    /**
     * Find by email mono.
     *
     * @param email the email
     * @return the mono
     */
    public Mono<AdminAccount> findByEmail(String email) {
        return adminAccountRepository.findByEmail(email);
    }

    /**
     * Find by admin account id mono.
     *
     * @param adminAccountId the admin account id
     * @return the mono
     */
    public Mono<AdminAccount> findByAdminAccountId(String adminAccountId) {
        return adminAccountRepository.findById(adminAccountId);
    }

    public Flux<AdminAccount> findByAdminAccountIds(List<String> adminAccountIds) {
        return adminAccountRepository.findByIdIn(adminAccountIds);
    }

    /**
     * Find by search text flux.
     *
     * @param searchText the search text
     * @param isUse      the is use
     * @return the flux
     */
    public Flux<AdminAccount> findBySearchText(String searchText, Boolean isUse) {
        return adminAccountRepository.findBySearchText(searchText, isUse);
    }

    /**
     * Update mono.
     *
     * @param adminAccount   the admin account
     * @param adminAccountId the admin account id
     * @return the mono
     */
    public Mono<AdminAccount> update(AdminAccount adminAccount, String adminAccountId) {
        return adminAccountRepository.findById(adminAccount.getId()).flatMap(before -> {
            adminAccount.setCreateAdminAccountId(before.getCreateAdminAccountId());
            adminAccount.setCreateDate(before.getCreateDate());
            adminAccount.setUpdateAdminAccountId(adminAccountId);
            adminAccount.setUpdateDate(LocalDateTime.now());
            return adminAccountRepository.save(adminAccount);
        });
    }

    /**
     * 사용자 계정 등록 - 통합관리
     *
     * @param adminAccount   the admin account
     * @param adminAccountId the admin account id
     * @return mono
     */
    public Mono<AdminAccount> save(AdminAccount adminAccount, String adminAccountId) {
        adminAccount.setUpdateAdminAccountId(adminAccountId);
        adminAccount.setUpdateDate(LocalDateTime.now());
        adminAccount.setId(PREFIX + generateUUIDWithOutDash());
        return adminAccountRepository.insert(adminAccount);
    }

    public Flux<AdminAccount> findBySearchNameOrEmail(String name, String email) {
        return adminAccountRepository.findBySearchNameOrEmail(name, email);
    }

}

