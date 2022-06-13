package com.bithumbsystems.management.api.v1.site.exception;

import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class SiteException extends RuntimeException {
  private final ErrorCode errorCode;

  public SiteException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}