package com.bithumbsystems.management.api.v1.account.model.request;

import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterRequest {

  private String id;
  private String siteId;
  private String email;
  private String name;
  private String password;
  private Set<String> roles;
  private Status status;
  private Boolean isUse;
  private Boolean isSendMail;
  private LocalDate validStartDate;
  private LocalDate validEndDate;
}
