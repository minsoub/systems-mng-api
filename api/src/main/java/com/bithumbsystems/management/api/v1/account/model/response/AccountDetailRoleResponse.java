package com.bithumbsystems.management.api.v1.account.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailRoleResponse {
    private String siteId;
    private String siteName;
    private String id;
    private String name;
}
