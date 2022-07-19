package com.bithumbsystems.management.api.v1.board.service;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.model.enums.EnumMapperValue;
import com.bithumbsystems.management.api.v1.board.mapper.BoardMasterMapper;
import com.bithumbsystems.management.api.v1.board.model.enums.AuthType;
import com.bithumbsystems.management.api.v1.board.model.enums.BoardType;
import com.bithumbsystems.management.api.v1.board.model.enums.PaginationType;
import com.bithumbsystems.management.api.v1.board.model.request.BoardMasterRequest;
import com.bithumbsystems.management.api.v1.board.model.response.BoardMasterListResponse;
import com.bithumbsystems.management.api.v1.board.model.response.BoardMasterResponse;
import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import com.bithumbsystems.persistence.mongodb.board.service.BoardDomainService;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardDomainService boardDomainService;

  /**
   * 게시판 유형 조회
   * @return
   */
  public Flux<Object> getBoardTypes() {
    return Flux.just(Stream.of(BoardType.values())
        .map(EnumMapperValue::new)
        .collect(Collectors.toList()));
  }

  /**
   * 페이징 유형 조회
   * @return
   */
  public Flux<Object> getPaginationTypes() {
    return Flux.just(Stream.of(PaginationType.values())
        .map(EnumMapperValue::new)
        .collect(Collectors.toList()));
  }

  /**
   * 권한 유형 조회
   * @return
   */
  public Flux<Object> getAuthTypes() {
    return Flux.just(Stream.of(AuthType.values())
        .map(EnumMapperValue::new)
        .collect(Collectors.toList()));
  }

  /**
   * 게시판 마스터 등록
   * @param boardMasterRequest 게시판 마스터
   * @param account 계정
   * @return
   */
  public Mono<BoardMasterResponse> createBoardMaster(BoardMasterRequest boardMasterRequest, Account account) {
    BoardMaster boardMaster = BoardMasterMapper.INSTANCE.toEntity(boardMasterRequest, boardMasterRequest.getSnsShare(), boardMasterRequest.getAuth());
    boardMaster.setCreateAccountId(account.getAccountId());
    return boardDomainService.createBoardMaster(boardMaster)
        .map(BoardMasterMapper.INSTANCE::toDto);
  }

  /**
   * 게시판 마스터 목록 조회
   * @param siteId 싸이트 ID
   * @param isUse 사용 여부
   * @return
   */
  public Flux<BoardMasterListResponse> getBoardMasters(String siteId, Boolean isUse) {
    return boardDomainService.getBoardMasters(siteId, isUse).map(BoardMasterMapper.INSTANCE::toDtoForList);
  }

  /**
   * 게시판 마스터 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  public Mono<BoardMasterResponse> getBoardMaster(String boardMasterId) {
    return boardDomainService.getBoardMaster(boardMasterId).map(BoardMasterMapper.INSTANCE::toDto);
  }

  /**
   * 게시판 마스터 수정
   * @param boardMasterRequest 게시판 마스터
   * @param account 계정
   * @return
   */
  public Mono<BoardMaster> updateBoardMaster(BoardMasterRequest boardMasterRequest, Account account) {
    BoardMaster boardMaster = BoardMasterMapper.INSTANCE.toEntity(boardMasterRequest, boardMasterRequest.getSnsShare(), boardMasterRequest.getAuth());
    boardMaster.setUpdateAccountId(account.getAccountId());
    return boardDomainService.updateBoardMaster(boardMaster);
  }

  /**
   * 게시판 마스터 삭제
   * @param boardMasterId 게시판 ID
   * @param account 계정
   * @return
   */
  public Mono<BoardMasterResponse> deleteBoardMaster(String boardMasterId, Account account) {
    return boardDomainService.getBoardMaster(boardMasterId)
        .flatMap(boardMaster -> {
          boardMaster.setUpdateAccountId(account.getAccountId());
          return boardDomainService.deleteBoardMaster(boardMaster);
        })
        .map(BoardMasterMapper.INSTANCE::toDto);
  }
}
