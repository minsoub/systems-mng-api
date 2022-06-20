package com.bithumbsystems.persistence.mongodb.account.model.entity;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "admin_access")
@Getter
@Setter
@Builder
public class AdminAccess {
    @MongoId
    private String id;
    @Indexed(unique = true)
    private String adminAccountId;
    private String name;
    @Indexed(unique = true)
    private String email;
    private boolean isUse;
    private Set<String> roles;
//    private String siteId;

    private LocalDateTime createDate;
    private String createAdminAccountId;
    private LocalDateTime updateDate;
    private String updateAdminAccountId;

    public void addRole(String roleManagementId) {
        this.roles.add(roleManagementId);
    }
    public void clearRole() {
        this.roles.clear();
    }
}
