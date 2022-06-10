package com.bithumbsystems.management.api.v1.role.model.request;

import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleManagementUpdateRequest {

  private String name;
  private LocalDateTime validStartDate;
  private LocalDateTime validEndDate;
  private Boolean isUse;
  private String siteId;
  private RoleType type;
}
