package com.bithumbsystems.management.api.v1.menu.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.management.api.v1.menu.exception.MenuException;
import com.bithumbsystems.management.api.v1.menu.exception.ProgramException;
import com.bithumbsystems.management.api.v1.menu.model.mapper.ProgramMapper;
import com.bithumbsystems.management.api.v1.menu.model.request.ProgramRegisterRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.ProgramUpdateRequest;
import com.bithumbsystems.management.api.v1.menu.model.response.ProgramResponse;
import com.bithumbsystems.persistence.mongodb.menu.model.entity.Program;
import com.bithumbsystems.persistence.mongodb.menu.service.ProgramDomainService;
import com.bithumbsystems.persistence.mongodb.site.service.SiteDomainService;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgramService {

  private final SiteDomainService siteDomainService;
  private final ProgramDomainService programDomainService;

  public Mono<List<ProgramResponse>> getList(String siteId, String searchText, Boolean isUse) {
    return programDomainService.findBySearchText(siteId, searchText, isUse)
        .flatMap(program -> Mono.just(ProgramMapper.INSTANCE.programToProgramResponse(program))).collectSortedList(
            Comparator.comparing(ProgramResponse::getCreateDate).reversed());
  }

  public Mono<ProgramResponse> create(String siteId, ProgramRegisterRequest programRegisterRequest, Account account) {
    return siteDomainService.existsById(siteId)
        .flatMap(isExist -> {
          if(!isExist) {
            return Mono.error(new ProgramException(ErrorCode.NOT_EXIST_SITE));
          }
          Program program = ProgramMapper.INSTANCE.programRegisterRequestToProgram(programRegisterRequest);
          program.setSiteId(siteId);
          return Mono.from(programDomainService.save(program, account.getAccountId())
              .map(ProgramMapper.INSTANCE::programToProgramResponse));
        }).switchIfEmpty(Mono.error(new MenuException(ErrorCode.FAIL_SAVE_MENU)));
  }

  public Mono<ProgramResponse> getOne(String siteId, String programId) {
    return programDomainService.findBySiteIdAndId(siteId, programId)
        .flatMap(program -> Mono.just(program)
            .map(ProgramMapper.INSTANCE::programToProgramResponse));
  }

  public Mono<ProgramResponse> update(String siteId, String programId, ProgramUpdateRequest programUpdateRequest, Account account) {
    return siteDomainService.existsById(siteId)
        .flatMap(isExist -> {
              if (!isExist) {
                return Mono.error(new ProgramException(ErrorCode.NOT_EXIST_SITE));
              }
          Program program = ProgramMapper.INSTANCE.programUpdateRequestToProgram(programUpdateRequest);
          program.setId(programId);
          program.setSiteId(siteId);
          return Mono.from(programDomainService.update(program, account.getAccountId())
              .map(ProgramMapper.INSTANCE::programToProgramResponse));
        }).switchIfEmpty(Mono.error(new ProgramException(ErrorCode.NOT_EXIST_PROGRAM)));
  }

  public Mono<Void> delete(String siteId, String programId) {
    return programDomainService.delete(siteId, programId);
  }
}
