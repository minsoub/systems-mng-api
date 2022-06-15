package com.bithumbsystems.management.api.v1.role.model.response;

import com.bithumbsystems.persistence.mongodb.menu.model.enums.ActionMethod;
import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public
class ProgramResourceResponse {
  private String id;
  private String name;
  private RoleType type;
  private String kindName;
  private ActionMethod actionMethod;
  private String actionUrl;
  private String description;
  private Boolean isCheck;
}