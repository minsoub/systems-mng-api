package com.bithumbsystems.management.api.core.exception;

import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.management.api.core.model.response.ErrorResponse;
import com.bithumbsystems.management.api.v1.file.exception.FileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Mono<?>> serverExceptionHandler(Exception ex) {
    log.error(ex.getMessage(), ex);
    ErrorData errorData = new ErrorData(ErrorCode.UNKNOWN_ERROR);
    return ResponseEntity.internalServerError().body(Mono.just(new ErrorResponse(errorData)));
  }

  @ExceptionHandler(FileException.class)
  public ResponseEntity<Mono<?>> fileExceptionHandler(FileException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  @ExceptionHandler(InvalidTokenException.class)
  public ResponseEntity<Mono<?>> invalidTokenExceptionHandler(InvalidTokenException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }
}