package com.bithumbsystems.management.api.v1.accessip.model.response;

import java.time.LocalDate;
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
  private LocalDate validStartDate;
  private LocalDate validEndDate;
  private Boolean isUse;


}
