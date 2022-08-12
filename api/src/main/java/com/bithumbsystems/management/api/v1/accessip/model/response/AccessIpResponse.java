package com.bithumbsystems.management.api.v1.accessip.model.response;

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
  private String validStartDate;
  private String validEndDate;
  private String allowIp;
  private String roleId;
  private String siteId;
  private Boolean isUse;
}

