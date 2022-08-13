package com.bithumbsystems.management.api.v1.accessip.model.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessIpRequest {
  private String adminAccountId;
  private String siteId;
  private String roleId;
  private LocalDate validStartDate;
  private LocalDate validEndDate;
  private String allowIp;
}
