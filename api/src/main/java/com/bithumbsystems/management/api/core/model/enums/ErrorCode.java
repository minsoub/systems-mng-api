package com.bithumbsystems.management.api.core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  UNKNOWN_ERROR("F001", "error"),
  INVALID_DATA("F999", "data is null or empty"),
  INVALID_PASSWORD("F505","Invalid password!"),

  INVALID_FILE("F002","file is invalid"),
  FAIL_SAVE_FILE("F003","file save fail"),
  INVALID_TOKEN("F004","Invalid token"),
  NOT_EXIST_ACCOUNT("F404","NOT_EXIST_ACCOUNT"),
  FAIL_ACCOUNT_REGISTER("F005","FAIL_ACCOUNT_REGISTER"),
  FAIL_PASSWORD_UPDATE("F006", "Current Password is not equals"),
  NOT_EXIST_ROLE("R404","NOT_EXIST_ROLE"),
  INVALID_ROLE("R500","INVALID_ROLE"),

  INVALID_MAX_ROLE("R501", "INVALID_MAX_ROLE"),

  NOT_EXIST_SITE("M404","NOT_EXIST_SITE"),
  NOT_EXIST_MENU("M401","NOT_EXIST_MENU"),
  NOT_EXIST_PROGRAM("P401","NOT_EXIST_PROGRAM"),
  FAIL_SAVE_MENU("M402","FAIL_SAVE_MENU"),
  FAIL_SEND_MAIL("M411","FAIL_SEND_MAIL");


  private final String code;

  private final String message;
}
