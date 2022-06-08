package com.bithumbsystems.management.api.v1.account.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountSearchResponse {

  private String adminAccountId;
  private String adminAccountName;
  private String adminAccountEmail;

}
