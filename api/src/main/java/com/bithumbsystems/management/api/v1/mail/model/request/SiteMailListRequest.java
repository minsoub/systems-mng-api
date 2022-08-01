package com.bithumbsystems.management.api.v1.mail.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SiteMailListRequest {

  @Schema(name = "siteId")
  private String siteId;
  @Schema(name = "isUse")
  private Boolean isUse;

}
