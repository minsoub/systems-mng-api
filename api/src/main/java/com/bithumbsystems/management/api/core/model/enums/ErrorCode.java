package com.bithumbsystems.management.api.core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  UNKNOWN_ERROR("F001", "error"),
  INVALID_FILE("F002","file is invalid"),
  FAIL_SAVE_FILE("F003","file save fail"),
  INVALID_TOKEN("F004","Invalid token"),
  NOT_EXIST_ACCOUNT("F404","NOT_EXIST_ACCOUNT"),

  FAIL_ACCOUNT_REGISTER("F005","FAIL_ACCOUNT_REGISTER");

  private final String code;

  private final String message;
}
