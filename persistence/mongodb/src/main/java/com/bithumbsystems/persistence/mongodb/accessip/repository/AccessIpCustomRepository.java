package com.bithumbsystems.persistence.mongodb.accessip.repository;

import com.bithumbsystems.persistence.mongodb.accessip.model.entity.AccessIp;
import reactor.core.publisher.Flux;

public interface AccessIpCustomRepository {
  Flux<AccessIp> findAccessIpBySearch(String name, String email, String siteId);
}
