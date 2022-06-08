package com.bithumbsystems.persistence.mongodb.account.service;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import com.bithumbsystems.persistence.mongodb.account.repository.AdminAccessRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminAccessDomainService {

    private final AdminAccessRepository adminAccessRepository;

    public Flux<AdminAccess> findBySiteId(String siteId) {
        return adminAccessRepository.findBySiteId(siteId);
    }

    public Mono<AdminAccess> findByAdminAccountId(String adminAccountId) {
        return adminAccessRepository.findByAdminAccountId(adminAccountId);
    }

    public Flux<AdminAccess> findAll() {
        return adminAccessRepository.findAll();
    }

    public Flux<AdminAccess> findBySearchText(String searchText) {
        return adminAccessRepository.findBySearchText(searchText);
    }

    public Mono<AdminAccess> save(AdminAccess adminAccess, String adminAccountId) {
        adminAccess.setCreateAdminAccountId(adminAccountId);
        adminAccess.setCreateDate(LocalDateTime.now());
        return adminAccessRepository.insert(adminAccess);
    }

    public Mono<Void> delete(String adminAccountId) {
        return adminAccessRepository.deleteByAdminAccountId(adminAccountId);
    }
}

