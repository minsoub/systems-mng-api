package com.bithumbsystems.management.api.v1.role.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleAccessResponse {
    private String id;
    private String email;
    private String name;
}
