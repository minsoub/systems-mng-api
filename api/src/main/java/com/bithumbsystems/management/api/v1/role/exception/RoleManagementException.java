package com.bithumbsystems.management.api.v1.role.exception;

import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RoleManagementException extends RuntimeException {
  public RoleManagementException(ErrorCode errorCode) {
    super(String.valueOf(errorCode));
  }
}