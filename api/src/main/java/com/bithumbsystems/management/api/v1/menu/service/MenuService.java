package com.bithumbsystems.management.api.v1.menu.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.management.api.v1.menu.exception.MenuException;
import com.bithumbsystems.management.api.v1.menu.model.mapper.MenuMapper;
import com.bithumbsystems.management.api.v1.menu.model.mapper.ProgramMapper;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuMappingRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuRegisterRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuUpdateRequest;
import com.bithumbsystems.management.api.v1.menu.model.response.MenuDetailResponse;
import com.bithumbsystems.management.api.v1.menu.model.response.MenuListResponse;
import com.bithumbsystems.management.api.v1.menu.model.response.MenuProgramResponse;
import com.bithumbsystems.management.api.v1.menu.model.response.MenuResponse;
import com.bithumbsystems.management.api.v1.menu.model.response.ProgramResponse;
import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import com.bithumbsystems.persistence.mongodb.menu.service.MenuDomainService;
import com.bithumbsystems.persistence.mongodb.menu.service.ProgramDomainService;
import com.bithumbsystems.persistence.mongodb.site.service.SiteDomainService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuService {

  private final MenuDomainService menuDomainService;
  private final SiteDomainService siteDomainService;

  private final ProgramDomainService programDomainService;

  public Mono<List<MenuListResponse>> getMenuList(String siteId, Boolean isUse) {
    log.info("Main: " + Thread.currentThread().getName());

    return menuDomainService.findList(siteId, isUse, null)
        .flatMap(menu -> Mono.just(MenuListResponse.builder()
            .name(menu.getName())
            .id(menu.getId())
            .siteId(menu.getSiteId())
            .isUse(menu.getIsUse())
            .order(menu.getOrder())
            .parentMenuId(menu.getParentsMenuId())
            .childMenu(new ArrayList<>())
            .build())
        ).flatMap(response -> menuDomainService.findList(siteId, isUse, response.getId())
            .map(m -> MenuListResponse.builder()
                .name(m.getName())
                .id(m.getId())
                .siteId(m.getSiteId())
                .isUse(m.getIsUse())
                .order(m.getOrder())
                .parentMenuId(m.getParentsMenuId())
                .childMenu(new ArrayList<>())
                .build())
            .flatMap(
                childResponse -> menuDomainService.findList(siteId, isUse, childResponse.getId())
                    .flatMap(c -> Mono.just(MenuListResponse.builder()
                        .name(c.getName())
                        .id(c.getId())
                        .siteId(c.getSiteId())
                        .isUse(c.getIsUse())
                        .order(c.getOrder())
                        .parentMenuId(c.getParentsMenuId())
                        .childMenu(new ArrayList<>())
                        .build())).collectList()
                    .flatMap(c -> {
                      childResponse.setChildMenu(c);
                      return Mono.just(childResponse);
                    }))
            .collectSortedList(Comparator.comparing(MenuListResponse::getOrder))
            .flatMap(r -> {
              response.setChildMenu(r);
              return Mono.just(response);
            }))
        .collectSortedList(Comparator.comparing(MenuListResponse::getOrder));
  }

  public Mono<MenuResponse> create(String siteId, MenuRegisterRequest menuRegisterRequest,
      Account account) {
    return siteDomainService.existsById(siteId)
        .flatMap(isExist -> {
          if (!isExist) {
            return Mono.error(new MenuException(ErrorCode.NOT_EXIST_SITE));
          }
          Menu menu = MenuMapper.INSTANCE.menuRegisterRequestToMenu(menuRegisterRequest);
          menu.setSiteId(siteId);
          return Mono.from(menuDomainService.save(menu, account.getAccountId())
              .map(MenuMapper.INSTANCE::menuToMenuResponse));
        }).switchIfEmpty(Mono.error(new MenuException(ErrorCode.FAIL_SAVE_MENU)));
  }

    public Mono<MenuDetailResponse> getOne(String siteId, String menuId) {
        log.debug("search key => {}, {}", siteId, menuId);
        return menuDomainService.findBySiteIdAndId(siteId, menuId)
                .flatMap(menu -> {
                    log.debug("search data => {}", menu);
                    if (StringUtils.hasLength(menu.getParentsMenuId())) {
                        return menuDomainService.findBySiteIdAndId(siteId, menu.getParentsMenuId())
                                .flatMap(result -> Mono.just(MenuDetailResponse.builder()
                                        .id(menu.getId())
                                        .name(menu.getName())
                                        .siteId(menu.getSiteId())
                                        .parentsMenuId((menu.getParentsMenuId()))
                                        .parentsMenuName(result.getName())
                                        .order(menu.getOrder())
                                        .isUse(menu.getIsUse())
                                        .url(menu.getUrl())
                                        .type(menu.getType())
                                        .target(menu.getTarget())
                                        .icon(menu.getIcon())
                                        .externalLink(menu.getExternalLink())
                                        .description(menu.getDescription())
                                        .build()
                                ))
                                .switchIfEmpty(Mono.defer(() -> Mono.just(MenuDetailResponse.builder()
                                        .id(menu.getId())
                                        .name(menu.getName())
                                        .siteId(menu.getSiteId())
                                        .parentsMenuId((menu.getParentsMenuId()))
                                        .parentsMenuName("")
                                        .order(menu.getOrder())
                                        .isUse(menu.getIsUse())
                                        .url(menu.getUrl())
                                        .type(menu.getType())
                                        .target(menu.getTarget())
                                        .icon(menu.getIcon())
                                        .externalLink(menu.getExternalLink())
                                        .description(menu.getDescription())
                                        .build() )));
                    } else {
                        return Mono.just(MenuDetailResponse.builder()
                                .id(menu.getId())
                                .name(menu.getName())
                                .siteId(menu.getSiteId())
                                .parentsMenuId((menu.getParentsMenuId()))
                                .parentsMenuName("")
                                .order(menu.getOrder())
                                .isUse(menu.getIsUse())
                                .url(menu.getUrl())
                                .type(menu.getType())
                                .target(menu.getTarget())
                                .icon(menu.getIcon())
                                .externalLink(menu.getExternalLink())
                                .description(menu.getDescription())
                                .build());
                    }
                }).switchIfEmpty(Mono.error(new MenuException(ErrorCode.INVALID_DATA)));

        //.flatMap(menu -> Mono.just(menu)
        //    .map(MenuMapper.INSTANCE::menuToMenuResponse));
    }

  public Mono<MenuResponse> update(String siteId, String menuId,
      MenuUpdateRequest menuUpdateRequest, Account account) {
    return siteDomainService.existsById(siteId)
        .flatMap(isExist -> {
          if (!isExist) {
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
            .log()
            .flatMap(program -> Mono.just(ProgramMapper.INSTANCE.programToProgramResponse(program)))
            .log()
            .collectSortedList(Comparator.comparing(ProgramResponse::getCreateDate)));
  }

  public Mono<List<MenuProgramResponse>> mappingMenuPrograms(String siteId, String menuId,
      MenuMappingRequest menuMappingRequest, Account account) {
    return programDomainService.saveSiteMenuProgram(siteId, menuId, menuMappingRequest.getProgramIds(),
            account.getAccountId())
        .map(siteMenuPrograms -> siteMenuPrograms.stream()
            .map(MenuMapper.INSTANCE::menuProgramToMenuProgramResponse)
            .collect(
                Collectors.toList()));
  }

  public Mono<List<ProgramResponse>> deleteMappingMenuPrograms(String siteId, String menuId, MenuMappingRequest menuMappingRequest) {
    return programDomainService.deleteSiteMenuProgram(siteId, menuId, menuMappingRequest.getProgramIds())
        .then(getPrograms(siteId, menuId));
  }
}
