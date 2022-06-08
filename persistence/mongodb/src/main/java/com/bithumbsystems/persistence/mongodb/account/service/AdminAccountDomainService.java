package com.bithumbsystems.persistence.mongodb.account.service;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.account.repository.AdminAccountRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
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

    public Flux<AdminAccount> findBySearchText(String searchText) {
        return adminAccountRepository.findBySearchText(searchText);
    }

    public Mono<AdminAccount> update(AdminAccount adminAccount, String adminAccountId) {
        adminAccount.setUpdateAdminAccountId(adminAccountId);
        adminAccount.setUpdateDate(LocalDateTime.now());
        return adminAccountRepository.save(adminAccount);
    }
}

