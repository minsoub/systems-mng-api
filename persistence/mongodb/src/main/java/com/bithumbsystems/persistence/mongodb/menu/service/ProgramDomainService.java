package com.bithumbsystems.persistence.mongodb.menu.service;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Program;
import com.bithumbsystems.persistence.mongodb.menu.model.entity.SiteMenuProgram;
import com.bithumbsystems.persistence.mongodb.menu.repository.ProgramRepository;
import com.bithumbsystems.persistence.mongodb.menu.repository.SiteMenuProgramRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProgramDomainService {

  private final ProgramRepository programRepository;
  private final SiteMenuProgramRepository siteMenuProgramRepository;
  private static final String PROGRAM_PREFIX = "PROGRAM_";
  private static final String PROGRAM_MENU_PREFIX = "PROGRAM_MENU_";

  public Mono<Program> save(Program program, String accountId) {
    program.setCreateDate(LocalDateTime.now());
    program.setCreateAdminAccountId(accountId);
    program.setId(PROGRAM_PREFIX + Instant.now().toEpochMilli());
    return programRepository.insert(program);
  }

  public Mono<Program> update(Program program, String accountId) {
    return programRepository.findById(program.getId()).flatMap(before -> {
      program.setCreateDate(before.getCreateDate());
      program.setCreateAdminAccountId(before.getCreateAdminAccountId());
      program.setUpdateAdminAccountId(accountId);
      program.setUpdateDate(LocalDateTime.now());
      return programRepository.save(program);
    });
  }

  public Mono<Void> delete(String siteId, String programId) {
    return programRepository.deleteBySiteIdAndId(siteId, programId);
  }

  public Flux<Program> findListBySiteIdAndIsUse(String siteId, Boolean isUse) {
    return programRepository.findBySiteIdAndIsUse(siteId, isUse);
  }

  public Mono<Program> findBySiteIdAndId(String siteId, String programId) {
    return programRepository.findBySiteIdAndId(siteId, programId);
  }

  public Flux<Program> findBySearchText(String siteId, String searchText, Boolean isUse) {
    return programRepository.findBySearchText(siteId, searchText, isUse);
  }

  public Flux<Program> findMenuPrograms(String siteId, String menuId) {
    return siteMenuProgramRepository.findBySiteIdAndMenuId(siteId, menuId)
        .flatMap(siteMenuProgram -> programRepository.findById(siteMenuProgram.getProgramId()));
  }

  public Flux<Program> findMenuPrograms(String menuId) {
    return siteMenuProgramRepository.findByMenuId(menuId)
        .flatMap(siteMenuProgram -> programRepository.findById(siteMenuProgram.getProgramId()));
  }

  @Transactional
  public Mono<List<SiteMenuProgram>> saveSiteMenuProgram(String siteId, String menuId,
      List<String> programIds, String accountId) {
    return siteMenuProgramRepository.saveAll(programIds
            .stream()
            .map(programId -> SiteMenuProgram.builder()
                .id(PROGRAM_MENU_PREFIX + Instant.now().toEpochMilli())
                .menuId(menuId)
                .siteId(siteId)
                .createDate(LocalDateTime.now())
                .createAdminAccountId(accountId)
                .programId(programId)
                .build()
            ).collect(Collectors.toList())).collectList();
  }

  public Mono<Void> deleteSiteMenuProgram(String siteId, String menuId,
      List<String> programIds) {
    return siteMenuProgramRepository.deleteBySiteIdAndMenuIdAndProgramIdIn(siteId, menuId, programIds);
  }
  public Mono<Void> deleteSiteMenuProgram(String siteId, String menuId) {
    return siteMenuProgramRepository.deleteBySiteIdAndMenuId(siteId, menuId);
  }

  public Flux<Program> findAllUrls(String method) {
    return programRepository.findAllUrls(method);
  }
}
