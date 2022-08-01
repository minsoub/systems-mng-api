package com.bithumbsystems.management.api.v1.mail.model.response;

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
public class SiteMailResponse {

  private String id;
  private String siteId;
  private String siteName;
  private Boolean isUse;
  private String serverInfo;
  private String adminUserEmail;
  private String accountId;
  private String accountPassword;

}
