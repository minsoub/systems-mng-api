package com.bithumbsystems.management.api.v1.messenger.model.dto;

import com.bithumbsystems.management.api.core.util.AES256Util;
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
    private String loginApi;
    private String sessionApi;
    private String pubsubApi;
    private Boolean isUse;
    private String user;
    private String pass;
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

    public void decryptUserInfo(String key) {
      this.user = AES256Util.decryptAES(key, this.getUser());
      this.pass = AES256Util.decryptAES(key, this.getPass());
    }
  }
}
