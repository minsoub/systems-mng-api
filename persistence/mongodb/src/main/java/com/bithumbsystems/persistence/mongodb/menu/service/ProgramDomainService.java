package com.bithumbsystems.persistence.mongodb.menu.service;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Program;
import com.bithumbsystems.persistence.mongodb.menu.model.entity.SiteMenuProgram;
import com.bithumbsystems.persistence.mongodb.menu.repository.ProgramRepository;
import com.bithumbsystems.persistence.mongodb.menu.repository.SiteMenuProgramRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class ProgramDomainService {

  private final ProgramRepository programRepository;
  private final SiteMenuProgramRepository siteMenuProgramRepository;

  public Mono<Program> save(Program program, String accountId) {
    program.setCreateDate(LocalDateTime.now());
    program.setCreateAdminAccountId(accountId);
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
  public Mono<Void> saveSiteMenuProgram(String siteId, String menuId, List<String> programIds, String accountId) {
    return siteMenuProgramRepository.deleteBySiteIdAndMenuId(siteId, menuId)
        .publishOn(Schedulers.immediate())
            .doOnSuccess((t) -> {
              final var siteMenuPrograms = programIds
                  .stream()
                  .map(programId -> SiteMenuProgram.builder()
                    .menuId(menuId)
                    .siteId(siteId)
                    .createDate(LocalDateTime.now())
                    .createAdminAccountId(accountId)
                    .programId(programId)
                    .build()
                  ).collect(Collectors.toList());
              siteMenuProgramRepository.saveAll(siteMenuPrograms).subscribe();
            });
  }
}
