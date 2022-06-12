package com.bithumbsystems.persistence.mongodb.account.model.entity;

import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin_account")
@NoArgsConstructor
@Getter
@Setter
public class AdminAccount {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String phone;
    private String password;
    private String oldPassword;
    private String otpSecretKey;
    private LocalDateTime lastLoginDate;
    private Status status;
    private Boolean isUse;
    private LocalDateTime lastPasswordUpdateDate;

    private LocalDateTime createDate;
    private String createAdminAccountId;
    private LocalDateTime updateDate;
    private String updateAdminAccountId;
    public void setStatusByIsUse(Boolean isUse){
        if (isUse)
            this.status = Status.INIT_REQUEST;
        else
            this.status = Status.DENY_ACCESS;
    }
}