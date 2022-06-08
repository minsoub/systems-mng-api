package com.bithumbsystems.management.api.v1.site.model.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SiteRegisterRequest {
  private String name;
  private Boolean isUse;
  private String description;
  private LocalDateTime validStartDate;
  private LocalDateTime validEndDate;
  private String adminAccountId;
  private String adminAccountEmail;
  private String adminAccountPhone;
}
