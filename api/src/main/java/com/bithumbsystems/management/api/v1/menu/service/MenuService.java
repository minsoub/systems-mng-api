package com.bithumbsystems.management.api.v1.menu.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.management.api.v1.menu.exception.MenuException;
import com.bithumbsystems.management.api.v1.menu.model.mapper.MenuMapper;
import com.bithumbsystems.management.api.v1.menu.model.mapper.ProgramMapper;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuRegisterRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuUpdateRequest;
import com.bithumbsystems.management.api.v1.menu.model.response.MenuListResponse;
import com.bithumbsystems.management.api.v1.menu.model.response.MenuResponse;
import com.bithumbsystems.management.api.v1.menu.model.response.ProgramResponse;
import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import com.bithumbsystems.persistence.mongodb.menu.service.MenuDomainService;
import com.bithumbsystems.persistence.mongodb.menu.service.ProgramDomainService;
import com.bithumbsystems.persistence.mongodb.site.service.SiteDomainService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

  private final MenuDomainService menuDomainService;
  private final SiteDomainService siteDomainService;

  private final ProgramDomainService programDomainService;

  public Mono<List<MenuListResponse>> getMenuList(String siteId, Boolean isUse) {
    return menuDomainService.findList(siteId, isUse, null)
        .flatMap(menu -> Mono.just(MenuListResponse.builder()
          .name(menu.getName())
          .id(menu.getId())
          .siteId(menu.getSiteId())
          .isUse(menu.getIsUse())
          .order(menu.getOrder())
          .parent_menu_id(menu.getParentsMenuId())
          .childMenu(new ArrayList<>())
          .build())
        ).flatMap(response -> menuDomainService.findList(siteId, isUse, response.getId())
            .publishOn(Schedulers.boundedElastic())
            .map(m -> {
              var childResponse = MenuListResponse.builder()
                  .name(m.getName())
                  .id(m.getId())
                  .siteId(m.getSiteId())
                  .isUse(m.getIsUse())
                  .order(m.getOrder())
                  .parent_menu_id(m.getParentsMenuId())
                  .childMenu(new ArrayList<>())
                  .build();

              menuDomainService.findList(siteId, isUse, childResponse.getId())
                  .flatMap(c -> Mono.just(MenuListResponse.builder()
                      .name(c.getName())
                      .id(c.getId())
                      .siteId(c.getSiteId())
                      .isUse(c.getIsUse())
                      .order(c.getOrder())
                      .parent_menu_id(m.getParentsMenuId())
                      .childMenu(new ArrayList<>())
                      .build())).collectList()
                  .flatMap(c -> {
                    childResponse.setChildMenu(c);
                    return Mono.just(childResponse);
                  }).subscribe();
              return childResponse;
            }).collectList()
            .flatMap(r -> {
              response.setChildMenu(r);
              return Mono.just(response);
            }))
        .collectList();
  }

  public Mono<MenuResponse> create(String siteId, MenuRegisterRequest menuRegisterRequest, Account account) {
    return siteDomainService.existsById(siteId)
        .flatMap(isExist -> {
          if(!isExist) {
            return Mono.error(new MenuException(ErrorCode.NOT_EXIST_SITE));
          }
          Menu menu = MenuMapper.INSTANCE.menuRegisterRequestToMenu(menuRegisterRequest);
          menu.setSiteId(siteId);
          return Mono.from(menuDomainService.save(menu, account.getAccountId())
              .map(MenuMapper.INSTANCE::menuToMenuResponse));
        }).switchIfEmpty(Mono.error(new MenuException(ErrorCode.FAIL_SAVE_MENU)));
  }

  public Mono<MenuResponse> getOne(String siteId, String menuId) {
    return menuDomainService.findBySiteIdAndId(siteId, menuId)
        .flatMap(menu -> Mono.just(menu)
            .map(MenuMapper.INSTANCE::menuToMenuResponse));
  }

  public Mono<MenuResponse> update(String siteId, String menuId, MenuUpdateRequest menuUpdateRequest, Account account) {
    return siteDomainService.existsById(siteId)
        .flatMap(isExist -> {
          if(!isExist) {
            return Mono.error(new MenuException(ErrorCode.NOT_EXIST_SITE));
          }
          Menu menu = MenuMapper.INSTANCE.menuUpdateRequestToMenu(menuUpdateRequest);
          menu.setId(menuId);
          menu.setSiteId(siteId);
          return Mono.from(menuDomainService.update(menu, account.getAccountId())
              .map(MenuMapper.INSTANCE::menuToMenuResponse));
        }).switchIfEmpty(Mono.error(new MenuException(ErrorCode.NOT_EXIST_MENU)));
  }

  public Mono<List<ProgramResponse>> getPrograms(String siteId, String menuId) {
    return menuDomainService.findBySiteIdAndId(siteId, menuId)
        .flatMap(menu -> programDomainService.findMenuPrograms(siteId, menu.getId())
            .flatMap(program -> Mono.just(ProgramMapper.INSTANCE.programToProgramResponse(program)))
            .collectList());
  }

  public Mono<List<ProgramResponse>> mappingMenuPrograms(String siteId, String menuId, List<String> programIds, Account account) {
    return programDomainService.saveSiteMenuProgram(siteId, menuId, programIds, account.getAccountId())
        .then(getPrograms(siteId,menuId));
  }
}
