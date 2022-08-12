package com.bithumbsystems.persistence.mongodb.accessip.service;

import com.bithumbsystems.persistence.mongodb.accessip.model.entity.AccessIp;
import com.bithumbsystems.persistence.mongodb.accessip.repository.AccessIpRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessIpDomainService {

  private final AccessIpRepository accessIpRepository;

  public Flux<AccessIp> findAll() {
    return accessIpRepository.findAll();
  }

  public Flux<AccessIp> findBySiteId(String siteId) {
    return accessIpRepository.findAccessIpBySiteId(siteId);
  }

  public Flux<AccessIp> findAccessIpBySiteIdAndAdminAccountIdIn(String siteId, List<String> adminAccountIdList) {
    return accessIpRepository.findAccessIpBySiteIdAndAdminAccountIdIn(siteId, adminAccountIdList);
  }
  public Mono<AccessIp> findById(String id) {
    return accessIpRepository.findById(id);
  }

  public Flux<AccessIp> findByAdminAccountId(String adminAccountId) {
    return accessIpRepository.findAccessIpByAdminAccountId(adminAccountId);
  }

  public Flux<AccessIp> findAccessIpByAdminAccountIdAndSiteId(String adminAccountId, String siteId) {
    return accessIpRepository.findAccessIpByAdminAccountIdAndSiteId(adminAccountId, siteId);
  }

  public Mono<AccessIp> insert(AccessIp accessIp) {
    return accessIpRepository.insert(accessIp);
  }

  public Mono<AccessIp> save(AccessIp accessIp) {
    return accessIpRepository.save(accessIp);
  }

  public Flux<AccessIp> findAccessIpBySearch(String name, String email, String siteId) {
    return accessIpRepository.findAccessIpBySearch(name, email, siteId);
  }
}
