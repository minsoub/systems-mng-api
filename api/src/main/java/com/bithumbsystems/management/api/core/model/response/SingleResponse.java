package com.bithumbsystems.management.api.core.model.response;

import com.bithumbsystems.management.api.core.model.enums.ReturnCode;
import lombok.Getter;

@Getter
public class SingleResponse<T> extends Response {
    private final ReturnCode status;
    private final T data;

    public SingleResponse(T data) {
        this.status = ReturnCode.SUCCESS;
        this.data = data;
    }
}
