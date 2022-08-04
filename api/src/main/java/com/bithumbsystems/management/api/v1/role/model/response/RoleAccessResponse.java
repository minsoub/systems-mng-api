package com.bithumbsystems.management.api.v1.role.model.response;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoleAccessResponse {
    private String id;
    private String email;
    private String name;
    private Set<String> roles;
    private LocalDateTime createDate;
}
