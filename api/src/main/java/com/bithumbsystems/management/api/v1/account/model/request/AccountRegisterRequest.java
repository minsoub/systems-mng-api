package com.bithumbsystems.management.api.v1.account.model.request;

import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterRequest {

  private String adminAccountId;
  private String siteId;
  private String email;
  private String name;
  private String password;
  private String roleManagementId;
  private Status status;
  private Boolean isUse;
  private Boolean isSendMail;
}
