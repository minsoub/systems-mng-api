package com.bithumbsystems.persistence.mongodb.account.model.entity;

import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin_account")
@AllArgsConstructor
@Getter
@Setter
public class AdminAccount {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String oldPassword;
    private String otpSecretKey;
    private LocalDateTime lastLoginDate;
    private Status status;
    private LocalDateTime lastPasswordUpdateDate;

    @CreatedDate
    private LocalDateTime createDate;
    private String createAdminAccountId;
    @LastModifiedDate
    private LocalDateTime updateDate;
    private String updateAdminAccountId;
    public boolean isEnabled(){
        return status == Status.INIT_COMPLETE || status == Status.NORMAL;
    }
}