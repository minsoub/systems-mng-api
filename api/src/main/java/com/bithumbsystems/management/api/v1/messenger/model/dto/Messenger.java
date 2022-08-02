package com.bithumbsystems.management.api.v1.messenger.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Messenger {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MessengerRequest {
    private String host;
    private String locale;
    private String user;
    private String pass;
    private String loginApi;
    private String sessionApi;
    private String pubsubApi;
    private Boolean isUse;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MessengerResponse {
    private String host;
    private String locale;
    private String user;
    private String pass;
    private String loginApi;
    private String sessionApi;
    private String pubsubApi;
    private Boolean isUse;
  }
}
