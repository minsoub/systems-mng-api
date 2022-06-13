package com.bithumbsystems.management.api.v1.menu.controller;

import com.bithumbsystems.management.api.v1.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProgramController {

  private final MenuService menuService;

}
