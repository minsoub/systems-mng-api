package com.bithumbsystems.management.api.v1.accessip.model.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
  @Schema(example = "2022.02.02")
  private String validStartDate;
  @Schema(example = "2022.02.02")
  private String validEndDate;
  private String allowIp;
}
