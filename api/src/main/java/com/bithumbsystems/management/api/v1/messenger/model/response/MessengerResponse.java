package com.bithumbsystems.management.api.v1.messenger.model.response;

import com.bithumbsystems.management.api.core.util.AES256Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessengerResponse {
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
