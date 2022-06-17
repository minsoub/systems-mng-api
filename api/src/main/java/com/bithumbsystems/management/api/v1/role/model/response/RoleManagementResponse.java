package com.bithumbsystems.management.api.v1.role.model.response;

import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleManagementResponse {
  private String id;
  private String name;
  private String validStartDate;
  private String validEndDate;
  private Boolean isUse;
  private String siteId;
  private RoleType type;
  private LocalDateTime createDate;
}
