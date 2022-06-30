package com.bithumbsystems.management.api.v1.menu.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuDeleteRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuMappingRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuRegisterRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuUpdateRequest;
import com.bithumbsystems.management.api.v1.menu.service.MenuService;
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

/**
 * The type Menu controller.
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;

  /**
   * Create response entity.
   *
   * @param siteId              the site id
   * @param menuRegisterRequest the menu register request
   * @param account             the account
   * @return the response entity
   */
  @PostMapping("/site/{siteId}/menu")
  @Operation(summary = "메뉴 생성", description = "사이트 관리> 메뉴관리: 메뉴 생성", tags = "사이트 관리> 메뉴관리")
  public ResponseEntity<Mono<?>> create(@PathVariable String siteId,
      @RequestBody MenuRegisterRequest menuRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(menuService.create(siteId, menuRegisterRequest, account)
        .map(SingleResponse::new));
  }

  /**
   * Gets one.
   *
   * @param siteId the site id
   * @param menuId the menu id
   * @return the one
   */
  @GetMapping("/site/{siteId}/menu/{menuId}")
  @Operation(summary = "메뉴 조회", description = "사이트 관리> 메뉴관리: 메뉴 조회", tags = "사이트 관리> 메뉴관리")
  public ResponseEntity<Mono<?>> getOne(@PathVariable String siteId, @PathVariable String menuId) {
    return ResponseEntity.ok().body(menuService.getOne(siteId, menuId)
        .map(SingleResponse::new));
  }

  /**
   * Update response entity.
   *
   * @param siteId            the site id
   * @param menuId            the menu id
   * @param menuUpdateRequest the menu update request
   * @param account           the account
   * @return the response entity
   */
  @PutMapping("/site/{siteId}/menu/{menuId}")
  @Operation(summary = "메뉴 수정", description = "사이트 관리> 메뉴관리: 메뉴 수정", tags = "사이트 관리> 메뉴관리")
  public ResponseEntity<Mono<?>> update(@PathVariable String siteId, @PathVariable String menuId,
      @RequestBody MenuUpdateRequest menuUpdateRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(menuService.update(siteId, menuId, menuUpdateRequest, account)
        .map(SingleResponse::new));
  }

  /**
   * Update response entity.
   *
   * @param siteId            the site id
   * @param menuDeleteRequest the menu delete request
   * @param account           the account
   * @return the response entity
   */
  @DeleteMapping("/site/{siteId}/menu")
  @Operation(summary = "메뉴 삭제", description = "사이트 관리> 메뉴관리: 메뉴 삭제", tags = "사이트 관리> 메뉴관리")
  public ResponseEntity<Mono<?>> delete(@PathVariable String siteId,
      @RequestBody MenuDeleteRequest menuDeleteRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(menuService.delete(siteId, menuDeleteRequest, account)
        .map(SingleResponse::new));
  }

  /**
   * Gets menu list.
   *
   * @param siteId the site id
   * @param isUse  the is use
   * @return the menu list
   */
  @GetMapping("/site/{siteId}/menu-list")
  @Operation(summary = "메뉴 목록 조회", description = "사이트 관리> 메뉴관리: 메뉴 목록 조회", tags = "사이트 관리> 메뉴관리")
  public ResponseEntity<Mono<?>> getMenuList(@PathVariable String siteId,
      @RequestParam(required = false) Boolean isUse) {
    return ResponseEntity.ok().body(menuService.getMenuList(siteId, isUse)
        .map(SingleResponse::new));
  }

  /**
   * 메뉴와 연결된 프로그램 목록
   *
   * @param siteId the site id
   * @param menuId the menu id
   * @return the programs
   */
  @GetMapping("/site/{siteId}/menu/{menuId}/programs")
  @Operation(summary = "메뉴와 연결된 프로그램 목록", description = "사이트 관리> 메뉴관리: 프로그램 연결 조회", tags = "사이트 관리> 메뉴관리")
  public ResponseEntity<Mono<?>> getPrograms(@PathVariable String siteId,
      @PathVariable String menuId) {
    return ResponseEntity.ok().body(menuService.getPrograms(siteId, menuId)
        .map(SingleResponse::new));
  }

  /**
   * 메뉴와 프로그램 연결
   *
   * @param siteId             the site id
   * @param menuId             the menu id
   * @param menuMappingRequest the menu mapping request
   * @param account            the account
   * @return the programs
   */
  @PutMapping("/site/{siteId}/menu/{menuId}/programs")
  @Operation(summary = "메뉴와 프로그램 연결" , description = "사이트 관리> 메뉴관리: 메뉴에 속한 프로그램 연결 작업", tags = "사이트 관리> 메뉴관리")
  public ResponseEntity<Mono<?>> mappingMenuPrograms(@PathVariable String siteId,
      @PathVariable String menuId,
      @RequestBody MenuMappingRequest menuMappingRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok()
        .body(menuService.mappingMenuPrograms(siteId, menuId, menuMappingRequest, account)
            .map(SingleResponse::new));
  }

  /**
   * 메뉴와 프로그램 삭제
   *
   * @param siteId             the site id
   * @param menuId             the menu id
   * @param menuMappingRequest the menu mapping request
   * @return the programs
   */
  @DeleteMapping("/site/{siteId}/menu/{menuId}/programs")
  @Operation(summary = "메뉴와 연결된 프로그램 삭제" , description = "사이트 관리> 메뉴관리: 메뉴에 속한 프로그램 연결 삭제", tags = "사이트 관리> 메뉴관리")
  public ResponseEntity<Mono<?>> deleteMappingMenuPrograms(@PathVariable String siteId,
      @PathVariable String menuId,
      @RequestBody MenuMappingRequest menuMappingRequest) {
    return ResponseEntity.ok()
        .body(menuService.deleteMappingMenuPrograms(siteId, menuId, menuMappingRequest)
            .map(SingleResponse::new));
  }

}
