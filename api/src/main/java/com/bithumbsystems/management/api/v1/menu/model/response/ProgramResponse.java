package com.bithumbsystems.management.api.v1.menu.model.response;

import com.bithumbsystems.persistence.mongodb.menu.model.enums.ActionMethod;
import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProgramResponse {
  private String id;
  private String name;
  private RoleType type;
  private String kindName;
  private ActionMethod actionMethod;
  private String actionUrl;
  private Boolean isUse;
  private String description;
  private String siteId;
  private LocalDateTime createDate;
}
