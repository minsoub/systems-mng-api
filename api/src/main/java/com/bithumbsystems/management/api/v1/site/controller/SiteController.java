package com.bithumbsystems.management.api.v1.site.controller;

import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.management.api.v1.site.model.request.SiteRegisterRequest;
import com.bithumbsystems.management.api.v1.site.service.SiteService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The type Site controller.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class SiteController {

  private final SiteService siteService;

  /**
   * List response entity.
   *
   * @param searchText the search text
   * @param page       the page
   * @param size       the size
   * @return the response entity
   */
  @GetMapping("/sites")
  public ResponseEntity<Mono<?>> list(@RequestParam(required = false, defaultValue = "") String searchText,
      @RequestParam(required = false, defaultValue = "0") int page,
      @RequestParam(required = false, defaultValue = "10") int size) {
    return ResponseEntity.ok(siteService.findBySearchText(searchText, PageRequest.of(page,size)));
  }

  /**
   * Create response entity.
   *
   * @param siteRegisterRequest the site register request
   * @return the response entity
   */
  @PostMapping("/site")
  public ResponseEntity<Mono<?>> create(@RequestBody SiteRegisterRequest siteRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok(siteService.create(siteRegisterRequest, account));
  }

  @GetMapping("/site/{siteId}")
  public ResponseEntity<Mono<?>> getOne(@PathVariable String siteId) {
    return ResponseEntity.ok(siteService.getOne(siteId));
  }

  @PutMapping("/site/{siteId}")
  public ResponseEntity<Mono<?>> update(@PathVariable String siteId,
      @RequestBody SiteRegisterRequest siteRegisterRequest,
      @Parameter(hidden = true) @CurrentUser Account account) {
    return ResponseEntity.ok(siteService.update(siteId, siteRegisterRequest, account));
  }
}
