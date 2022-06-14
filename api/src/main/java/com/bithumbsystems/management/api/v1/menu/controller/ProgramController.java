package com.bithumbsystems.management.api.v1.menu.controller;

import com.bithumbsystems.management.api.v1.menu.service.ProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProgramController {

  private final ProgramService programService;

//  @GetMapping("/site/{siteId}/programs")
//  public ResponseEntity<Mono<?>> getList(@PathVariable String siteId, @RequestParam Boolean isUse) {
//    return ResponseEntity.ok().body(programService.getOne(siteId, isUse)
//        .map(menuResponse -> new SingleResponse(menuResponse)));
//  }
//
//  @PostMapping("/site/{siteId}/program")
//  public ResponseEntity<Mono<?>> create(@PathVariable String siteId, @RequestBody MenuRegisterRequest menuRegisterRequest,
//      @Parameter(hidden = true) @CurrentUser Account account) {
//    return ResponseEntity.ok().body(programService.create(siteId, menuRegisterRequest, account)
//        .map(menuResponse -> new SingleResponse(menuResponse)));
//  }
//
//  @GetMapping("/site/{siteId}/program/{programId}")
//  public ResponseEntity<Mono<?>> getOne(@PathVariable String siteId, @PathVariable String programId) {
//    return ResponseEntity.ok().body(programService.getOne(siteId, programId)
//        .map(menuResponse -> new SingleResponse(menuResponse)));
//  }
//
//  @PutMapping("/site/{siteId}/program/{programId}")
//  public ResponseEntity<Mono<?>> update(@PathVariable String siteId, @PathVariable String programId,
//      @RequestBody MenuUpdateRequest menuUpdateRequest,
//      @Parameter(hidden = true) @CurrentUser Account account) {
//    return ResponseEntity.ok().body(programService.update(siteId, programId, menuUpdateRequest, account)
//        .map(menuResponse -> new SingleResponse(menuResponse)));
//  }
//
//  @DeleteMapping("/site/{siteId}/program/{programId}")
//  public ResponseEntity<Mono<?>> delete(@PathVariable String siteId, @PathVariable String programId) {
//    return ResponseEntity.ok().body(programService.delete(siteId, programId)
//        .map(menuResponse -> new SingleResponse(menuResponse)));
//  }

}
