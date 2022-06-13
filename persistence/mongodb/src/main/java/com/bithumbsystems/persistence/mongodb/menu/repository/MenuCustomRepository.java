package com.bithumbsystems.persistence.mongodb.menu.repository;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MenuCustomRepository {

  Flux<?> findMenuListBySiteId(String siteId, Boolean isUse, Class<?> outputType);

  Flux<Menu> findMenuListBySiteId(String siteId, Boolean isUse, String parentMenuId);
}
