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
  FAIL_ACCOUNT_REGISTER("F005","FAIL_ACCOUNT_REGISTER"),
  NOT_EXIST_ROLE("R404","NOT_EXIST_ROLE"),
  NOT_EXIST_SITE("M404","NOT_EXIST_SITE"),
  NOT_EXIST_MENU("M401","NOT_EXIST_MENU"),
  FAIL_SAVE_MENU("M402","FAIL_SAVE_MENU");


  private final String code;

  private final String message;
}
