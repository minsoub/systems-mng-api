package com.bithumbsystems.management.api.v1.account.model.response;

import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountSearchResponse {

  private String id;
  private String name;
  private String email;
  private LocalDateTime lastLoginDate;
  private Status status;
  private String roleManagementName;
  private LocalDate validStartDate;
  private LocalDate validEndDate;
  private LocalDateTime createDate;

  public AccountSearchResponse(String id, String name, String email, LocalDateTime lastLoginDate,
      Status status, LocalDateTime createDate) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.lastLoginDate = lastLoginDate;
    this.status = status;
    this.createDate = createDate;
  }
}
