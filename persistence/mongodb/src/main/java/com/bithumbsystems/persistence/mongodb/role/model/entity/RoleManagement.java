package com.bithumbsystems.persistence.mongodb.role.model.entity;

import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "role_management")
@AllArgsConstructor
@Getter
@Setter
public class RoleManagement {
  @Id
  private String id;
  private String name;
  private RoleType type;
  private Boolean isUse;
  private LocalDateTime validStartDate;
  private LocalDateTime validEndDate;
  private String siteId;
  @CreatedDate
  private LocalDateTime createDate;
  private String createAdminAccountId;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private String updateAdminAccountId;
}
