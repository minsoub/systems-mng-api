package com.bithumbsystems.persistence.mongodb.menu.service;

import static com.bithumbsystems.persistence.mongodb.common.util.StringUtil.generateUUIDWithOutDash;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import com.bithumbsystems.persistence.mongodb.menu.model.entity.SiteMenuProgram;
import com.bithumbsystems.persistence.mongodb.menu.model.enums.MenuType;
import com.bithumbsystems.persistence.mongodb.menu.repository.MenuRepository;
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

  private final SiteMenuProgramRepository siteMenuProgramRepository;
  private static final String PREFIX = "MENU_";
  private static final String PROGRAM_MENU_PREFIX = "PROGRAM_MENU_";

  public Mono<Menu> save(Menu menu, String accountId) {
    menu.setCreateDate(LocalDateTime.now());
    menu.setCreateAdminAccountId(accountId);
    menu.setId(PREFIX + generateUUIDWithOutDash());
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

  public Mono<Menu> delete(String menuId, String accountId) {
    return menuRepository.findById(menuId).flatMap(before -> {
      before.setUpdateAdminAccountId(accountId);
      before.setUpdateDate(LocalDateTime.now());
      before.setIsUse(false);
      return menuRepository.save(before);
    });
  }

  public Flux<Menu> findList(String siteId, Boolean isUse, String parentsMenuId) {
    return menuRepository.findMenuListBySiteId(siteId, isUse, parentsMenuId);
  }

  public Flux<Menu> findAllUrls() {
    return menuRepository.findAllUrls();
  }

  public Flux<Menu> findAll() {
    return menuRepository.findByIsUseIsTrue();
  }

  public Mono<Menu> findBySiteIdAndId(String siteId, String menuId) {
    return menuRepository.findBySiteIdAndId(siteId, menuId);
  }

  public Flux<Menu> findMenuByProgramId(String programId) {
    return siteMenuProgramRepository.findByProgramId(programId)
        .flatMap(siteMenuProgram -> menuRepository.findById(siteMenuProgram.getMenuId()));
  }

  public Flux<Menu> findByUrl(String path) {
    return menuRepository.findByUrlAndType(path, MenuType.ITEM.name());
  }

  public Mono<SiteMenuProgram> saveMapping(SiteMenuProgram siteMenuProgram) {
    siteMenuProgram.setId(PROGRAM_MENU_PREFIX + generateUUIDWithOutDash());
    return siteMenuProgramRepository.save(siteMenuProgram);
  }

  public Mono<Void> deleteAllMapping() {
    return siteMenuProgramRepository.deleteAll();
  }

  public Flux<SiteMenuProgram> findSiteMenuProgramByMenuId(String menuId) {
    return siteMenuProgramRepository.findByMenuId(menuId);
  }

}
