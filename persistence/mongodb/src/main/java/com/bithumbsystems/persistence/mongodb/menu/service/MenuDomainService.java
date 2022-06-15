package com.bithumbsystems.persistence.mongodb.menu.service;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import com.bithumbsystems.persistence.mongodb.menu.repository.MenuRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MenuDomainService {

  private final MenuRepository menuRepository;

  public Mono<Menu> save(Menu menu, String accountId) {
    menu.setCreateDate(LocalDateTime.now());
    menu.setCreateAdminAccountId(accountId);
    return menuRepository.insert(menu);
  }

  public Mono<Menu> update(Menu menu, String accountId) {
    return menuRepository.findById(menu.getId()).flatMap(before -> {
      menu.setCreateDate(before.getCreateDate());
      menu.setCreateAdminAccountId(before.getCreateAdminAccountId());
      menu.setUpdateAdminAccountId(accountId);
      menu.setUpdateDate(LocalDateTime.now());
      return menuRepository.save(menu);
    });
  }

  public Flux<?> findListBySiteId(String siteId, Boolean isUse, Class<?> outputType) {
    return menuRepository.findMenuListBySiteId(siteId, isUse, outputType);
  }

  public Flux<Menu> findList(String siteId, Boolean isUse, String parentsMenuId) {
    return menuRepository.findMenuListBySiteId(siteId, isUse, parentsMenuId);
  }

  public Flux<Menu> findAll() {
    return menuRepository.findByIsUseIsTrue();
  }

  public Mono<Menu> findBySiteIdAndId(String siteId, String menuId) {
    return menuRepository.findBySiteIdAndId(siteId, menuId);
  }
}
