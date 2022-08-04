package com.bithumbsystems.management.api.v1.messenger.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessengerRequest {
  private String host;
  private String locale;
  private String loginApi;
  private String sessionApi;
  private String pubsubApi;
  private Boolean isUse;
  private String user;
  private String pass;
}
