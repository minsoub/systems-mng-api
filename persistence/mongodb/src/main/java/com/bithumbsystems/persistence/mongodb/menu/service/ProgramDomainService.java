package com.bithumbsystems.persistence.mongodb.menu.service;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Program;
import com.bithumbsystems.persistence.mongodb.menu.repository.ProgramRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProgramDomainService {

  private final ProgramRepository programRepository;

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
}
