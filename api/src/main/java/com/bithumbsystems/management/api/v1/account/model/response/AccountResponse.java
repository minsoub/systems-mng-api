package com.bithumbsystems.management.api.v1.account.model.response;

import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponse {

  private String id;
  private String email;
  private String roleManagementName;
  private LocalDateTime lastLoginDate;
  private String name;
  private Status status;
  private LocalDateTime createDate;
}
