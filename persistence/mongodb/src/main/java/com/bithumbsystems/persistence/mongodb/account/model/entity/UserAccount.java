package com.bithumbsystems.persistence.mongodb.account.model.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "user_account")
@AllArgsConstructor
@Getter
@Setter
public class UserAccount {
  @MongoId(value = FieldType.STRING, targetType = FieldType.STRING)
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
