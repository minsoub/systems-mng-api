package com.bithumbsystems.management.api.v1.menu.controller;

import com.bithumbsystems.management.api.v1.menu.service.MenuProgramSpecService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MenuProgramSpecController {

  private final MenuProgramSpecService menuProgramSpecService;

  @GetMapping("auth/mapping/init")
  @Operation(summary = "권한 X 프로그램 초기화", description = "권한 X 프로그램 매핑 데이터 전체 삭제", tags = "통합 시스템 관리> 메뉴관리")
  public ResponseEntity<?> initMenuProgramsInRole() {
    return ResponseEntity.ok(menuProgramSpecService.initMenuProgramsInRole());
  }

  @GetMapping("auth/mapping")
  @Operation(summary = "권한 X 프로그램 매핑", description = "권한 X 프로그램 전체 매핑", tags = "통합 시스템 관리> 메뉴관리")
  public ResponseEntity<?> mappingMenuProgramsInRole() {
    return ResponseEntity.ok(menuProgramSpecService.mappingMenuProgramsInRole());
  }

  @GetMapping("menu/mapping")
  @Operation(summary = "메뉴 X 프로그램 매핑", description = "메뉴 X 프로그램 전체 매핑", tags = "통합 시스템 관리> 메뉴관리")
  public ResponseEntity<?> menuProgramMapping() {
    return ResponseEntity.ok(menuProgramSpecService.menuProgramMapping());
  }

  @GetMapping("menu/mapping/init")
  @Operation(summary = "메뉴 X 프로그램 초기화", description = "메뉴 X 프로그램 매핑 데이터 전체 삭제", tags = "통합 시스템 관리> 메뉴관리")
  public ResponseEntity<?> menuProgramDeleteAllMapping() {
    return ResponseEntity.ok(menuProgramSpecService.menuProgramDeleteAllMapping());
  }

}
