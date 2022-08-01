package com.bithumbsystems.management.api.v1.mail.exception;

import com.bithumbsystems.management.api.core.model.enums.ErrorCode;

public class SiteMailException extends RuntimeException {

  private final ErrorCode errorCode;

  public SiteMailException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

}
