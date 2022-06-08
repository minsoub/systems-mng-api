package com.bithumbsystems.management.api.core.config.resolver;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Account {

  private final String siteId;

  private final String accountId;

  private final String role;
}
