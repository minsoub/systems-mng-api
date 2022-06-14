package com.bithumbsystems.management.api.core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MailForm {
  DEFAULT("subject", "mail/default.html");

  private final String subject;

  private final String path;
}
