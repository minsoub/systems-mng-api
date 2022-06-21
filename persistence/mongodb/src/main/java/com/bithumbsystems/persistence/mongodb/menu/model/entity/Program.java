package com.bithumbsystems.persistence.mongodb.menu.model.entity;

import com.bithumbsystems.persistence.mongodb.menu.model.enums.ActionMethod;
import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "program")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Program {
  @MongoId(targetType = FieldType.STRING)
  private String id;
  private String name;
  private RoleType type;
  private String kindName;
  private ActionMethod actionMethod;
  private String actionUrl;
  private Boolean isUse;
  private String description;
  @Indexed
  private String siteId;
  private LocalDateTime createDate;
  private String createAdminAccountId;
  private LocalDateTime updateDate;
  private String updateAdminAccountId;
}
