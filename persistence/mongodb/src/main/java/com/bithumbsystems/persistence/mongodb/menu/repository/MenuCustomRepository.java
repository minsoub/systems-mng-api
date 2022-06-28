package com.bithumbsystems.persistence.mongodb.menu.repository;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import reactor.core.publisher.Flux;

public interface MenuCustomRepository {

  Flux<Menu> findAllUrls();

  Flux<Menu> findMenuListBySiteId(String siteId, Boolean isUse, String parentMenuId);
}
