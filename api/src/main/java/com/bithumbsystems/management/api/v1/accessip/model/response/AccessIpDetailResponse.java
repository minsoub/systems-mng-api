package com.bithumbsystems.management.api.v1.accessip.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessIpDetailResponse {

  private String id;
  private String adminAccountId;
  private String allowIp;
  private String validStartDate;
  private String validEndDate;
  private Boolean isUse;


}
