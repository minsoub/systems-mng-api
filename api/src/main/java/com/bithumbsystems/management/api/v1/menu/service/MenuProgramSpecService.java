package com.bithumbsystems.management.api.v1.menu.service;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.SiteMenuProgram;
import com.bithumbsystems.persistence.mongodb.menu.service.MenuDomainService;
import com.bithumbsystems.persistence.mongodb.menu.service.ProgramDomainService;
import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleAuthorization;
import com.bithumbsystems.persistence.mongodb.role.service.RoleAuthorizationDomainService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuProgramSpecService {

  private final ProgramDomainService programDomainService;
  private final MenuDomainService menuDomainService;
  private final RoleAuthorizationDomainService roleAuthorizationDomainService;

  public Mono<Void> menuProgramDeleteAllMapping() {
    return menuDomainService.deleteAllMapping();
  }

  public Disposable menuProgramMapping() {
    AntPathMatcher pathMatcher = new AntPathMatcher();

    var allPrograms = programDomainService.findAll().collectList();
    var allSpec = programDomainService.findMenuProgramSpecificationsAll();
    return Mono.zip(allPrograms, allSpec).doOnNext(objects -> {
      var programs = objects.getT1();
      var spec = objects.getT2();
      spec.forEach(menuProgramSpecification -> menuDomainService.findByUrl(
              menuProgramSpecification.getPath())
          .doOnNext(
              menu -> menuProgramSpecification.getPrograms().forEach(
                  actionProgram -> programs.stream()
                      .filter(program ->
                          program.getActionMethod().name()
                              .equals(actionProgram.getActionMethod())
                              && pathMatcher.match(program.getActionUrl(),
                              actionProgram.getActionUrl()))
                      .forEach(
                          program -> menuDomainService.saveMapping(SiteMenuProgram.builder()
                              .menuId(menu.getId())
                              .programId(program.getId())
                              .siteId(menu.getSiteId())
                              .createAdminAccountId("AutoMapping")
                              .createDate(LocalDateTime.now())
                              .build()).subscribe()))
          ).subscribe());
    }).subscribe();
  }

  public Mono<List<RoleAuthorization>> mappingMenuProgramsInRole() {
    return roleAuthorizationDomainService.findAll()
        .publishOn(Schedulers.boundedElastic())
        .doOnNext(roleAuthorization ->
            Flux.fromIterable(roleAuthorization.getAuthorizationResources())
                .flatMap(authorizationResource ->
                    menuDomainService.findSiteMenuProgramByMenuId(authorizationResource.getMenuId())
                        .map(SiteMenuProgram::getProgramId).collectList()
                        .map(programs -> {
                          log.info(programs.toString());
                          authorizationResource.setProgramId(programs);
                          return authorizationResource;
                        })
                ).collectList()
                .doOnNext(roleAuthorization::setAuthorizationResources)
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess(authorizationResources -> roleAuthorizationDomainService.update(
                    roleAuthorization).subscribe()).subscribe()
        ).collectList();
  }

  public Flux<RoleAuthorization> initMenuProgramsInRole() {
    return roleAuthorizationDomainService.findAll().publishOn(Schedulers.boundedElastic())
        .doOnNext(roleAuthorization -> {

          var newAuth = roleAuthorization.getAuthorizationResources().stream()
              .peek(authorizationResource -> authorizationResource.setProgramId(new ArrayList<>()))
              .collect(Collectors.toList());

          roleAuthorization.setAuthorizationResources(newAuth);
          roleAuthorizationDomainService.update(roleAuthorization).subscribe();
        });
  }
}
