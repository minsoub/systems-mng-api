package com.bithumbsystems.management.api.v1.account.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterRequest {

  private String adminAccountId;
  private String password;
  private String roleManagementId;
  private Boolean isUse;
  private Boolean isSendMail;
}
