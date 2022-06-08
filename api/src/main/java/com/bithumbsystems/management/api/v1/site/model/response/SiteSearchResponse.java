package com.bithumbsystems.management.api.v1.site.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SiteSearchResponse {
  private String siteId;
  private String siteName;
  private Boolean isUse;
  private LocalDateTime validStartDate;
  private LocalDateTime validEndDate;
  private String description;
  private String adminAccountId;
  private String adminAccountEmail;
  private String adminAccountPhone;
  private LocalDateTime createDate;
}
