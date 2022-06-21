package com.bithumbsystems.persistence.mongodb.account.service;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccess;
import com.bithumbsystems.persistence.mongodb.account.repository.AdminAccessRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Admin access domain service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminAccessDomainService {

    private final AdminAccessRepository adminAccessRepository;

    private static final String PREFIX = "ACCESS_";

    /**
     * Find by admin account id mono.
     *
     * @param adminAccountId the admin account id
     * @return the mono
     */
    public Mono<AdminAccess> findByAdminAccountId(String adminAccountId) {
        return adminAccessRepository.findByAdminAccountId(adminAccountId);
    }

    /**
     * Find by admin account ids flux.
     *
     * @param adminAccountIds the admin account ids
     * @return the flux
     */
    public Flux<AdminAccess> findByAdminAccountIds(List<String> adminAccountIds) {
        return adminAccessRepository.findByAdminAccountIdIn(adminAccountIds);
    }

    /**
     * Find all flux.
     *
     * @return the flux
     */
    public Flux<AdminAccess> findAll() {
        return adminAccessRepository.findAll();
    }

    /**
     * Find by search text flux.
     *
     * @param searchText the search text
     * @return the flux
     */
    public Flux<AdminAccess> findBySearchText(String searchText) {
        return adminAccessRepository.findBySearchText(searchText);
    }

    /**
     * Find by admin account id and role management id mono.
     *
     * @param adminAccountId   the admin account id
     * @param roleManagementId the role management id
     * @return the mono
     */
    public Mono<AdminAccess> findByAdminAccountIdAndRoleManagementId(String adminAccountId, String roleManagementId) {
        return adminAccessRepository.findByAdminAccountIdAndRolesContaining(adminAccountId, roleManagementId);
    }

    /**
     * Save mono.
     *
     * @param adminAccess    the admin access
     * @param adminAccountId the admin account id
     * @return the mono
     */
    public Mono<AdminAccess> save(AdminAccess adminAccess, String adminAccountId) {
        adminAccess.setCreateAdminAccountId(adminAccountId);
        adminAccess.setCreateDate(LocalDateTime.now());
        adminAccess.setId(PREFIX + Instant.now().toEpochMilli());
        return adminAccessRepository.insert(adminAccess);
    }

    /**
     * 사용자 접근 테이블에서 Role과 일치하는 사용자 리스트를 조회한다.
     *
     * @param roleManagementId the role management id
     * @return flux
     */
    public Flux<AdminAccess> findByRoleManagementId(String roleManagementId) {
        return adminAccessRepository.findByRolesContains(roleManagementId);
    }

    /**
     * Update mono.
     *
     * @param adminAccess    the admin access
     * @param adminAccountId the admin account id
     * @return the mono
     */
    public Mono<AdminAccess> update(AdminAccess adminAccess, String adminAccountId) {
        return adminAccessRepository.findById(adminAccess.getId()).flatMap(before -> {
            adminAccess.setCreateAdminAccountId(before.getCreateAdminAccountId());
            adminAccess.setCreateDate(before.getCreateDate());
            adminAccess.setUpdateAdminAccountId(adminAccountId);
            adminAccess.setUpdateDate(LocalDateTime.now());
            return adminAccessRepository.save(adminAccess);
        });
    }

    /**
     * Delete mono.
     *
     * @param adminAccountId the admin account id
     * @return the mono
     */
    public Mono<Void> delete(String adminAccountId) {
        return adminAccessRepository.deleteByAdminAccountId(adminAccountId);
    }
}

