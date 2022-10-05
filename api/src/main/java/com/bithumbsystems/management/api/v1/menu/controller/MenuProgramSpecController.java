package com.bithumbsystems.management.api.v1.menu.controller;

import static com.bithumbsystems.persistence.mongodb.common.util.StringUtil.generateUUIDWithOutDash;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.SiteMenuProgram;
import com.bithumbsystems.persistence.mongodb.menu.repository.MenuProgramSpecificationsRepository;
import com.bithumbsystems.persistence.mongodb.menu.service.MenuDomainService;
import com.bithumbsystems.persistence.mongodb.menu.service.ProgramDomainService;
import com.bithumbsystems.persistence.mongodb.role.service.RoleAuthorizationDomainService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MenuProgramSpecController {

  private final MenuProgramSpecificationsRepository menuProgramSpecificationsRepository;
  private final MenuDomainService menuDomainService;
  private final ProgramDomainService programDomainService;
  private final RoleAuthorizationDomainService roleAuthorizationDomainService;

  private static final String PROGRAM_MENU_PREFIX = "PROGRAM_MENU_";

  @GetMapping("menu/mapping")
  public ResponseEntity<?> mappingMenuPrograms() {
    AntPathMatcher pathMatcher = new AntPathMatcher();

    var allPrograms = programDomainService.findAll().collectList();
    var allSpec = menuProgramSpecificationsRepository.findAll().collectList();

    return ResponseEntity.ok(Mono.zip(allPrograms, allSpec).doOnNext(objects -> {
      var programs = objects.getT1();
      var specs = objects.getT2();
      specs.forEach(menuProgramSpecifications -> menuDomainService.findByUrl(
              menuProgramSpecifications.getPath())
          .doOnNext(
              menu -> menuProgramSpecifications.getPrograms().forEach(
                  actionProgram -> programs.stream()
                      .filter(program ->
                          program.getActionMethod().name().equals(actionProgram.getActionMethod())
                              && pathMatcher.match(program.getActionUrl(),
                              actionProgram.getActionUrl()))
                      .forEach(program -> menuDomainService.saveMapping(SiteMenuProgram.builder()
                          .id(PROGRAM_MENU_PREFIX + generateUUIDWithOutDash())
                          .menuId(menu.getId())
                          .programId(program.getId())
                          .siteId(menu.getSiteId())
                          .createAdminAccountId("AutoMapping")
                          .createDate(LocalDateTime.now())
                          .build()).subscribe()))
          ).subscribe());
    }).subscribe());
  }

  @GetMapping("role/init")
  public ResponseEntity<?> initMenuProgramsInRole() {
    return ResponseEntity.ok(
        roleAuthorizationDomainService.findAll().publishOn(Schedulers.boundedElastic())
            .doOnNext(roleAuthorization -> {

              var newAuth = roleAuthorization.getAuthorizationResources().stream()
                  .map(authorizationResource -> {
                    authorizationResource.setProgramId(new ArrayList<>());
                    return authorizationResource;
                  }).collect(Collectors.toList());

              roleAuthorization.setAuthorizationResources(newAuth);
              roleAuthorizationDomainService.update(roleAuthorization).subscribe();
            }).subscribe());
  }

  @GetMapping("role/mapping")
  public ResponseEntity<?> mappingMenuProgramsInRole() {
    return ResponseEntity.ok(
        roleAuthorizationDomainService.findAll().publishOn(Schedulers.boundedElastic())
            .doOnNext(roleAuthorization -> Flux.fromIterable(
                    roleAuthorization.getAuthorizationResources())
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
                .publishOn(Schedulers.boundedElastic()).doOnSuccess(authorizationResources ->
                    roleAuthorizationDomainService.update(roleAuthorization).subscribe())
                .subscribe()).subscribe());
  }
}
