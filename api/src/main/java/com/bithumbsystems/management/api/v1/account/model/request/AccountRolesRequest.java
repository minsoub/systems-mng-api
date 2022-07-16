package com.bithumbsystems.management.api.v1.account.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRolesRequest {
    private String adminAccountId;
    private List<String> roleManagementId;
}
