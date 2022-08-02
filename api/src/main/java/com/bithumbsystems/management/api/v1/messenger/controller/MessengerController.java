package com.bithumbsystems.management.api.v1.messenger.controller;


import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.messenger.model.dto.Messenger.MessengerRequest;
import com.bithumbsystems.management.api.v1.messenger.service.MessengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/messenger")
public class MessengerController {

  private final MessengerService messengerService;

  /**
   * 다우메신저 연동 정보 조회
   *
   * @return response entity
   */
  @GetMapping
  @Operation(summary = "다우메신저 연동 정보 조회", description = "다우메신저 연동 정보 조회", tags = "통합 관리 > 연동 관리 > 다우 메신저 관리")
  public ResponseEntity<?> getMessengerList() {
    return ResponseEntity.ok().body(messengerService.getMessengerList().map(MultiResponse::new));
  }

  /**
   * 다우메신저 연동 정보 상세조회
   *
   * @param id the daou messenger id
   * @return response entity
   */
  @GetMapping("/{daouMessengerId}")
  @Operation(summary = "다우메신저 연동 정보 상세조회", description = "다우메신저 연동 정보 상세조회", tags = "통합 관리 > 연동 관리 > 다우 메신저 관리")
  public ResponseEntity<?> getMessenger(@PathVariable("daouMessengerId") String id) {
    return ResponseEntity.ok().body(messengerService.getMessenger(id).map(SingleResponse::new));
  }

  /**
   * 다우메신저 연동 정보 등록
   *
   * @param request the daou messenger request
   * @param account the account
   * @return response entity
   */
  @PostMapping
  @Operation(summary = "다우메신저 연동 정보 등록", description = "다우메신저 연동 정보 등록", tags = "통합 관리 > 연동 관리 > 다우 메신저 관리")
  public ResponseEntity<?> createMessenger(@RequestBody MessengerRequest request,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(messengerService.createMessenger(request, account).map(SingleResponse::new));
  }

  /**
   * 다우메신저 연동 정보 수정
   *
   * @param id the daou messenger id
   * @param request the daou messenger request
   * @param account the account
   * @return response entity
   */
  @PutMapping("/{daouMessengerId}")
  @Operation(summary = "다우메신저 연동 정보 수정", description = "다우메신저 연동 정보 수정", tags = "통합 관리 > 연동 관리 > 다우 메신저 관리")
  public ResponseEntity<?> updateMessenger(@PathVariable("daouMessengerId") String id,
      @RequestBody MessengerRequest request,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(messengerService.updateMessenger(id, request, account).map(SingleResponse::new));
  }

}
