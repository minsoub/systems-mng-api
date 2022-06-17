package com.bithumbsystems.persistence.mongodb.role.model.entity;

import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "role_management")
@Getter
@Setter
@ToString
public class RoleManagement {
  @MongoId
  private String id;
  private String name;
  private RoleType type;
  private Boolean isUse;
  private LocalDate validStartDate;
  private LocalDate validEndDate;
  @Indexed
  private String siteId;
  private LocalDateTime createDate;
  private String createAdminAccountId;
  private LocalDateTime updateDate;
  private String updateAdminAccountId;

  public RoleManagement(String id, String name, RoleType type, Boolean isUse,
      LocalDate validStartDate, LocalDate validEndDate, String siteId) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.isUse = isUse;
    this.validStartDate = validStartDate;
    this.validEndDate = validEndDate;
    this.siteId = siteId;
    //this.validDate = validStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "~"+validEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }
}
