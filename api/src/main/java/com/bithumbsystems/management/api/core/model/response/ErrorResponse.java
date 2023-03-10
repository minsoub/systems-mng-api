package com.bithumbsystems.management.api.core.model.response;

import com.bithumbsystems.management.api.core.exception.ErrorData;
import com.bithumbsystems.management.api.core.model.enums.ReturnCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private final ReturnCode result;
  private final ErrorData error;

  public ErrorResponse(ErrorData data) {
    this.result = ReturnCode.FAIL;
    this.error = data;
  }
}