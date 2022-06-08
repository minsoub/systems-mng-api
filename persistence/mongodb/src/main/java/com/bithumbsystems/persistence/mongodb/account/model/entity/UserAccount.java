package com.bithumbsystems.persistence.mongodb.account.model.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user_account")
@AllArgsConstructor
@Getter
@Setter
public class UserAccount {
  @Id
  private String id;
  private String name;
  private String phone;
  private String email;
  private String password;
  private Boolean isUse;
  private LocalDateTime lastLoginDate;
  @CreatedDate
  private LocalDateTime createDate;
  private String createAdminAccountId;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private String updateAdminAccountId;
}
