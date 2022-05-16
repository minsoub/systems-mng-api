package com.bithumbsystems.management.api.core.model.response;

import com.bithumbsystems.management.api.core.model.enums.ReturnCode;
import java.util.List;
import lombok.Getter;

@Getter
public class MultiResponse<T> extends Response {
    private final ReturnCode status;
    private final List<T> data;

    MultiResponse(List<T> data) {
        this.status = ReturnCode.SUCCESS;
        this.data = data;
    }
}
