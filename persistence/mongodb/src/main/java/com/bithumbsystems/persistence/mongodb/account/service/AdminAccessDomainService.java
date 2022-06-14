package com.bithumbsystems.persistence.mongodb.account.service;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import com.bithumbsystems.persistence.mongodb.account.repository.AdminAccessRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminAccessDomainService {

    private final AdminAccessRepository adminAccessRepository;

    public Flux<AdminAccess> findBySiteId(String siteId) {
        return adminAccessRepository.findBySiteId(siteId);
    }

    public Mono<AdminAccess> findByAdminAccountId(String adminAccountId) {
        return adminAccessRepository.findByAdminAccountId(adminAccountId);
    }

    public Flux<AdminAccess> findByAdminAccountIds(List<String> adminAccountIds) {
        return adminAccessRepository.findByAdminAccountIdIn(adminAccountIds);
    }

    public Flux<AdminAccess> findAll() {
        return adminAccessRepository.findAll();
    }

    public Flux<AdminAccess> findBySearchText(String searchText) {
        return adminAccessRepository.findBySearchText(searchText);
    }
    public Mono<AdminAccess> findByAdminAccountIdAndRoleManagementIdAndSiteId(String adminAccountId, String roleManagementId, String siteId) {
        return adminAccessRepository.findByAdminAccountIdAndRoleManagementIdAndSiteId(adminAccountId, roleManagementId, siteId);
    }

    public Mono<AdminAccess> save(AdminAccess adminAccess, String adminAccountId) {
        adminAccess.setCreateAdminAccountId(adminAccountId);
        adminAccess.setCreateDate(LocalDateTime.now());
        return adminAccessRepository.insert(adminAccess);
    }

    public Mono<AdminAccess> update(AdminAccess adminAccess, String adminAccountId) {
        return adminAccessRepository.findById(adminAccess.getId()).flatMap(before -> {
            adminAccess.setCreateAdminAccountId(before.getCreateAdminAccountId());
            adminAccess.setCreateDate(before.getCreateDate());
            adminAccess.setUpdateAdminAccountId(adminAccountId);
            adminAccess.setUpdateDate(LocalDateTime.now());
            return adminAccessRepository.save(adminAccess);
        });
    }

    public Mono<Void> delete(String adminAccountId) {
        return adminAccessRepository.deleteByAdminAccountId(adminAccountId);
    }
}

