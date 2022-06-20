package com.bithumbsystems.management.api.v1.account.model.response;

import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailRoleResponse {
    private String siteId;
    private String siteName;
    private String id;
    private String name;
}
