package com.bithumbsystems.management.api.v1.role.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMappingAccountsRequest {
    List<RoleModeAccountRequest> accounts;
}
