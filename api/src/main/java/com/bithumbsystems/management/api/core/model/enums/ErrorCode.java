package com.bithumbsystems.management.api.core.model.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {
  INVALID_FILE("file is invalid"),
  FAIL_SAVE_FILE("file save fail");

  private final String message;
}
