package com.bithumbsystems.persistence.mongodb.menu.service;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import com.bithumbsystems.persistence.mongodb.menu.repository.MenuRepository;
import com.bithumbsystems.persistence.mongodb.menu.repository.ProgramRepository;
import com.bithumbsystems.persistence.mongodb.menu.repository.SiteMenuProgramRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MenuDomainService {

  private final MenuRepository menuRepository;
  private final ProgramRepository programRepository;
  private final SiteMenuProgramRepository siteMenuProgramRepository;

  public Mono<Menu> save(Menu menu, String accountId) {
    menu.setCreateDate(LocalDateTime.now());
    menu.setCreateAdminAccountId(accountId);
    return menuRepository.insert(menu);
  }

  public Mono<Menu> update(Menu menu, String accountId) {
    menu.setUpdateAdminAccountId(accountId);
    menu.setUpdateDate(LocalDateTime.now());
    return menuRepository.save(menu);
  }

  public Mono<Menu> findByIdAndSiteId(String siteId, String menuId) {
    return menuRepository.findBySiteIdAndId(siteId, menuId);
  }

  public Flux<?> findListBySiteId(String siteId, Boolean isUse, Class<?> outputType) {
    return menuRepository.findMenuListBySiteId(siteId, isUse, outputType);
  }

  public Flux<Menu> findList(String siteId, Boolean isUse, String parentsMenuId) {
    return menuRepository.findMenuListBySiteId(siteId, isUse, parentsMenuId);
  }

}
