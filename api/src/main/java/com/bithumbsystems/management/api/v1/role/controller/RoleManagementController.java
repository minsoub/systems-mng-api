package com.bithumbsystems.management.api.v1.role.controller;

import com.bithumbsystems.management.api.v1.role.model.response.RoleManagementResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleManagementController {

  @GetMapping
  public ResponseEntity<Mono<?>> getRoleManagements() {
      return  ResponseEntity.ok().body(Mono.just(new RoleManagementResponse()));
  }
}
