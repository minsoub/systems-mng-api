package com.bithumbsystems.management.api.v1.multilingual.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.multilingual.model.request.MultilingualRequest;
import com.bithumbsystems.management.api.v1.multilingual.service.MultilingualService;
import com.bithumbsystems.persistence.mongodb.multilingual.model.enums.MultilingualType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/multilingual")
public class MultilingualController {

  private final MultilingualService multilingualService;

  /**
   * 다국어 연동 정보 조회
   *
   * @return response entity
   */
  @GetMapping
  @Operation(summary = "다국어 연동 정보 조회", description = "다국어 연동 정보 조회", tags = "통합 관리 > 연동 관리 > 다국어 관리")
  public ResponseEntity<?> getMultilingualList() {
    return ResponseEntity.ok()
        .body(multilingualService.getMultilingualList().map(MultiResponse::new));
  }

  /**
   * 다국어 연동 정보 상세조회
   *
   * @param id the daou messenger id
   * @return response entity
   */
  @GetMapping("{multilingualId}")
  @Operation(summary = "다국어 연동 정보 상세조회", description = "다국어 연동 정보 상세조회", tags = "통합 관리 > 연동 관리 > 다국어 관리")
  public ResponseEntity<?> getMultilingual(@PathVariable("multilingualId") String id) {
    return ResponseEntity.ok()
        .body(multilingualService.getMultilingual(id).map(SingleResponse::new));
  }

  /**
   * 다국어 연동 정보 등록
   *
   * @param request the multilingual request
   * @param account the account
   * @return response entity
   */
  @PostMapping
  @Operation(summary = "다국어 연동 정보 등록", description = "다국어 연동 정보 등록", tags = "통합 관리 > 연동 관리 > 다국어 관리")
  public ResponseEntity<?> createMultilingual(@RequestBody MultilingualRequest request,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok()
        .body(multilingualService.createMultilingual(request, account).map(SingleResponse::new));
  }

  /**
   * 다국어 연동 정보 리스트 등록
   *
   * @param requestList the multilingual request list
   * @param account the account
   * @return response entity
   */
  @PostMapping("/list")
  @Operation(summary = "다국어 연동 정보 리스트 등록", description = "다국어 연동 정보 리스트 등록", tags = "통합 관리 > 연동 관리 > 다국어 관리")
  public ResponseEntity<?> createMultilingualList(
      @RequestBody List<MultilingualRequest> requestList,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        multilingualService.createMultilingualList(requestList, account).map(MultiResponse::new));
  }

  /**
   * 다국어 연동 정보 수정
   *
   * @param id the daou messenger id
   * @param request the multilingual request
   * @param account the account
   * @return response entity
   */
  @PutMapping("/{multilingualId}")
  @Operation(summary = "다국어 연동 정보 수정", description = "다국어 연동 정보 수정", tags = "통합 관리 > 연동 관리 > 다국어 관리")
  public ResponseEntity<?> updateMultilingual(@PathVariable("multilingualId") String id,
      @RequestBody MultilingualRequest request,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(
        multilingualService.updateMultilingual(id, request, account).map(SingleResponse::new));
  }

  /**
   * 다국어 연동 타입 enum 조회
   *
   * @return response entity
   */
  @GetMapping("/location/list")
  @Operation(summary = "다국어 연동 타입 enum 조회", description = "다국어 연동 타입 enum 조회", tags = "통합 관리 > 연동 관리 > 다국어 관리")
  public ResponseEntity<?> getLocationList() {
    return ResponseEntity.ok().body(
        Mono.just(new MultiResponse<>(
            Arrays.stream(MultilingualType.values()).collect(Collectors.toList()))));
  }
}
