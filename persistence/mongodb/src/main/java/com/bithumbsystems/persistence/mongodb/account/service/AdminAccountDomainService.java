package com.bithumbsystems.persistence.mongodb.account.service;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.account.repository.AdminAccountRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminAccountDomainService {

    private final AdminAccountRepository adminAccountRepository;

    public Mono<AdminAccount> findByEmail(String email) {
        return adminAccountRepository.findByEmail(email);
    }

    public Mono<AdminAccount> findByAdminAccountId(String adminAccountId) {
        return adminAccountRepository.findById(adminAccountId);
    }

    public Flux<AdminAccount> findBySearchText(String searchText, Boolean isUse) {
        return adminAccountRepository.findBySearchText(searchText, isUse);
    }

    public Mono<AdminAccount> update(AdminAccount adminAccount, String adminAccountId) {
        adminAccount.setUpdateAdminAccountId(adminAccountId);
        adminAccount.setUpdateDate(LocalDateTime.now());
        return adminAccountRepository.save(adminAccount);
    }

    /**
     * 사용자 계정 등록 - 통합관리
     * @param adminAccount
     * @param adminAccountId
     * @return
     */
    public Mono<AdminAccount> save(AdminAccount adminAccount, String adminAccountId) {
        adminAccount.setUpdateAdminAccountId(adminAccountId);
        adminAccount.setUpdateDate(LocalDateTime.now());
        return adminAccountRepository.insert(adminAccount);
    }
}

