package com.bithumbsystems.management.api.core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  UNKNOWN_ERROR("F001", "error"),
  INVALID_FILE("F002","file is invalid"),
  FAIL_SAVE_FILE("F003","file save fail"),
  INVALID_TOKEN("F004","Invalid token");

  private final String code;

  private final String message;
}
