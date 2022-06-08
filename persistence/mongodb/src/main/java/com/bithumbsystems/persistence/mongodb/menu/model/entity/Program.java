package com.bithumbsystems.persistence.mongodb.menu.model.entity;

import com.bithumbsystems.persistence.mongodb.menu.model.enums.ActionMethod;
import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "program")
@AllArgsConstructor
@Getter
@Setter
public class Program {
  @Id
  private String id;
  private String name;
  private RoleType type;
  private String kindName;
  private ActionMethod actionMethod;
  private String actionUrl;
  private Boolean isUse;
  private String description;
  private String siteId;
  @CreatedDate
  private LocalDateTime createDate;
  private String createAdminAccountId;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private String updateAdminAccountId;
}
