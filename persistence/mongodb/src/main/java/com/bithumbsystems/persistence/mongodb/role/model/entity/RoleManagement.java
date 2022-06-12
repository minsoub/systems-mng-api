package com.bithumbsystems.persistence.mongodb.role.model.entity;

import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "role_management")
@Getter
@Setter
@ToString
public class RoleManagement {
  @Id
  private String id;
  private String name;
  private RoleType type;
  private Boolean isUse;
  private LocalDateTime validStartDate;
  private LocalDateTime validEndDate;
  private String siteId;
  private LocalDateTime createDate;
  private String createAdminAccountId;
  private LocalDateTime updateDate;
  private String updateAdminAccountId;

  public RoleManagement(String id, String name, RoleType type, Boolean isUse,
      LocalDateTime validStartDate, LocalDateTime validEndDate, String siteId) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.isUse = isUse;
    this.validStartDate = validStartDate;
    this.validEndDate = validEndDate;
    this.siteId = siteId;
  }
}
