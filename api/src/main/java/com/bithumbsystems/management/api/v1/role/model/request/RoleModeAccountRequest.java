package com.bithumbsystems.management.api.v1.role.model.request;

import com.bithumbsystems.management.api.v1.role.model.enums.FlagEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleModeAccountRequest {
    private FlagEnum flag;
    private String id;
}
