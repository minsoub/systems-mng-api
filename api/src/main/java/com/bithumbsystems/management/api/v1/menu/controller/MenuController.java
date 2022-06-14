package com.bithumbsystems.management.api.v1.menu.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuRegisterRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuUpdateRequest;
import com.bithumbsystems.management.api.v1.menu.service.MenuService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;

  @PostMapping("/site/{siteId}/menu")
  public ResponseEntity<Mono<?>> create(@PathVariable String siteId, @RequestBody MenuRegisterRequest menuRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(menuService.create(siteId, menuRegisterRequest, account)
        .map(menuResponse -> new SingleResponse(menuResponse)));
  }

  @GetMapping("/site/{siteId}/menu/{menuId}")
  public ResponseEntity<Mono<?>> getOne(@PathVariable String siteId, @PathVariable String menuId) {
    return ResponseEntity.ok().body(menuService.getOne(siteId, menuId)
        .map(menuResponse -> new SingleResponse(menuResponse)));
  }

  @PutMapping("/site/{siteId}/menu/{menuId}")
  public ResponseEntity<Mono<?>> update(@PathVariable String siteId, @PathVariable String menuId,
      @RequestBody MenuUpdateRequest menuUpdateRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok().body(menuService.update(siteId, menuId, menuUpdateRequest, account)
        .map(menuResponse -> new SingleResponse(menuResponse)));
  }

  @GetMapping("/site/{siteId}/menu-list")
  public ResponseEntity<Mono<?>> getMenuList(@PathVariable String siteId,
      @RequestParam(required = false) Boolean isUse) {
    return ResponseEntity.ok().body(menuService.getMenuList(siteId, isUse)
        .map(menuListResponses -> new SingleResponse(menuListResponses)));
  }

//  @GetMapping("/site/{siteId}/menu/{menuId}/programs")
//  public ResponseEntity<Mono<?>> getPrograms(@PathVariable String siteId, @PathVariable String menuId,
//      @Parameter(hidden = true) @CurrentUser Account account) {
//    return ResponseEntity.ok().body(menuService.getPrograms(siteId, menuId, account)
//        .map(menuProgramResponse -> new SingleResponse(menuProgramResponse)));
//  }


}
