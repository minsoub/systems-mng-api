package com.bithumbsystems.management.api.v1.mail.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SiteMailRequest {

  private String siteId;
  private String siteName;
  private String adminUserEmail;
  private Boolean isUse;
  private String serverInfo;
  private String accountId;
  private String accountPassword;

}
