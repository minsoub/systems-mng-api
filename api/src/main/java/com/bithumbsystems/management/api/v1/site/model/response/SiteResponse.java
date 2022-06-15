package com.bithumbsystems.management.api.v1.site.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SiteResponse {
  private String id;
  private String name;
  private Boolean isUse;
  private String description;
  private LocalDate validStartDate;
  private LocalDate validEndDate;
  private String adminAccountId;
  private String adminAccountEmail;
  private String adminAccountPhone;
  private LocalDateTime createDate;
}
