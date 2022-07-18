package com.bithumbsystems.management.api.v1.board.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.board.model.request.BoardMasterRequest;
import com.bithumbsystems.management.api.v1.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
  private final BoardService boardService;

  /**
   * 게시판 유형 조회
   * @return
   */
  @GetMapping(value = "/board-types")
  @Operation(summary = "게시판 유형 조회", description = "통합 관리 > 통합 게시판 관리 > 게시판 생성: 게시판 유형 조회", tags = "통합 관리 > 통합 게시판 관리")
  public ResponseEntity<Mono<?>> getBoardTypes() {
    return ResponseEntity.ok().body(boardService.getBoardTypes()
        .collectList()
        .map(MultiResponse::new));
  }

  /**
   * 페이징 유형 조회
   * @return
   */
  @GetMapping(value = "/pagination-types")
  @Operation(summary = "페이징 유형 조회", description = "통합 관리 > 통합 게시판 관리 > 게시판 생성: 페이징 유형 조회", tags = "통합 관리 > 통합 게시판 관리")
  public ResponseEntity<Mono<?>> getPaginationTypes() {
    return ResponseEntity.ok().body(boardService.getPaginationTypes()
        .collectList()
        .map(MultiResponse::new));
  }

  /**
   * 게시판 마스터 등록
   * @return
   */
  @PostMapping
  @Operation(summary = "게시판 마스터 등록", description = "통합 관리 > 통합 게시판 관리 > 게시판 생성: 게시판 마스터 등록", tags = "통합 관리 > 통합 게시판 관리")
  public ResponseEntity<Mono<?>> createBoardMaster(@RequestBody BoardMasterRequest boardMasterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(boardService.createBoardMaster(boardMasterRequest, account)
        .map(SingleResponse::new));
  }

  /**
   * 게시판 마스터 목록 조회
   * @param siteId 싸이트 ID
   * @param isUse 사용 여부
   * @return
   */
  @GetMapping
  @Operation(summary = "게시판 마스터 목록 조회", description = "통합 관리 > 통합 게시판 관리 > 게시판 관리: 게시판 마스터 목록 조회", tags = "통합 관리 > 통합 게시판 관리")
  public ResponseEntity<Mono<?>> getBoardMasters(@RequestParam(value = "site_id") String siteId, @RequestParam(value = "is_use") Boolean isUse) {
    return ResponseEntity.ok().body(boardService.getBoardMasters(siteId, isUse)
        .collectList()
        .map(MultiResponse::new));
  }

  /**
   * 게시판 마스터 조회
   * @param boardMasterId 게시판 ID
   * @return
   */
  @GetMapping("/{boardMasterId}")
  @Operation(summary = "게시판 마스터 정보 조회", description = "통합 관리 > 통합 게시판 관리 > 게시판 생성: 게시판 마스터 조회", tags = "통합 관리 > 통합 게시판 관리")
  public ResponseEntity<Mono<?>> getBoardMaster(@PathVariable String boardMasterId) {
    return ResponseEntity.ok().body(boardService.getBoardMaster(boardMasterId)
        .map(SingleResponse::new));
  }

  /**
   * 게시판 마스터 수정
   * @param boardMasterId 게시판 ID
   * @param boardMasterRequest 게시판 정보
   * @return
   */
  @PutMapping("/{boardMasterId}")
  @Operation(summary = "게시판 마스터 수정", description = "통합 관리 > 통합 게시판 관리 > 게시판 생성: 게시판 마스터 수정", tags = "통합 관리 > 통합 게시판 관리")
  public ResponseEntity<Mono<?>> updateBoardMaster(@PathVariable String boardMasterId,
      @RequestBody BoardMasterRequest boardMasterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(boardService.updateBoardMaster(boardMasterRequest, account)
        .map(SingleResponse::new));
  }

  /**
   * 게시판 마스터 삭제
   * @param boardMasterId 게시판 ID
   * @return
   */
  @DeleteMapping("/{boardMasterId}")
  @Operation(summary = "게시판 마스터 삭제", description = "통합 관리 > 통합 게시판 관리 > 게시판 생성: 게시판 마스터 삭제", tags = "통합 관리 > 통합 게시판 관리")
  public ResponseEntity<Mono<?>> deleteBoardMaster(@PathVariable String boardMasterId,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(boardService.deleteBoardMaster(boardMasterId, account).then(
        Mono.just(new SingleResponse()))
    );
  }
}
