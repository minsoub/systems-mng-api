package com.bithumbsystems.persistence.mongodb.account.model.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin_access")
@AllArgsConstructor
@Getter
@Setter
public class AdminAccess {
    @Id
    private String id;
    private String adminAccountId;
    private String name;
    private String email;
    private boolean isUse;
    private String roleManagementId;
    private String siteId;

    @CreatedDate
    private LocalDateTime createDate;
    private String createAdminAccountId;
    @LastModifiedDate
    private LocalDateTime updateDate;
    private String updateAdminAccountId;
}
