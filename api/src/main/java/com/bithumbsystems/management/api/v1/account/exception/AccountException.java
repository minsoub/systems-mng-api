package com.bithumbsystems.management.api.v1.account.exception;

import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AccountException extends RuntimeException {
  public AccountException(ErrorCode errorCode) {
    super(String.valueOf(errorCode));
  }
}