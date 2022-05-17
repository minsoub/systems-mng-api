package com.bithumbsystems.management.api.v1.file.exception;

import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileException extends RuntimeException {
    public FileException(ErrorCode errorCode) {
        super(String.valueOf(errorCode));
    }
}