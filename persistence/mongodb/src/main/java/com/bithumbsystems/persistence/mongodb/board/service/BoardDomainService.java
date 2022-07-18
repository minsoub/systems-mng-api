package com.bithumbsystems.persistence.mongodb.board.service;

import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import com.bithumbsystems.persistence.mongodb.board.repository.BoardMasterCustomRepository;
import com.bithumbsystems.persistence.mongodb.board.repository.BoardMasterRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardDomainService {

  private final BoardMasterRepository boardMasterRepository;
  private final BoardMasterCustomRepository boardMasterCustomRepository;

  /**
   * 게시판 마스터 등록
   * @param boardMaster 게시판 마스터
   * @return
   */
  public Mono<BoardMaster> createBoardMaster(BoardMaster boardMaster) {
    boardMaster.setIsUse(true);
    boardMaster.setCreateDate(LocalDateTime.now());
    return boardMasterRepository.insert(boardMaster);
  }

  /**
   * 게시판 마스터 목록 조회
   * @param siteId 싸이트 ID
   * @param isUse 사용 여부
   * @return
   */
  public Flux<BoardMaster> getBoardMasters(String siteId, Boolean isUse) {
    return boardMasterCustomRepository.findBySearchCondition(siteId, isUse);
  }

  /**
   * 게시판 마스터 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  public Mono<BoardMaster> getBoardMaster(String boardMasterId) {
    return boardMasterRepository.findById(boardMasterId);
  }

  /**
   * 게시판 마스터 수정
   * @param boardMaster 게시판 마스터
   * @return
   */
  public Mono<BoardMaster> updateBoardMaster(BoardMaster boardMaster) {
    boardMaster.setUpdateDate(LocalDateTime.now());
    return boardMasterRepository.save(boardMaster);
  }

  /**
   * 게시판 마스터 삭제
   * @param boardMaster 게시판 마스터
   * @return
   */
  public Mono<BoardMaster> deleteBoardMaster(BoardMaster boardMaster) {
    boardMaster.setIsUse(false);
    boardMaster.setUpdateDate(LocalDateTime.now());
    return boardMasterRepository.save(boardMaster);
  }
}
