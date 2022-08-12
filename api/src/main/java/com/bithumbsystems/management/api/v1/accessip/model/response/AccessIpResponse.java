package com.bithumbsystems.management.api.v1.accessip.model.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccessIpResponse {
  private String id;
  private String adminAccountId;
  private LocalDate validStartDate;
  private LocalDate validEndDate;
  private String allowIp;
  private String roleId;
  private String siteId;
  private Boolean isUse;
}

