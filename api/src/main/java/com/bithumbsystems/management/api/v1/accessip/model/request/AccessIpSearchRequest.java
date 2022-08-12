package com.bithumbsystems.management.api.v1.accessip.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccessIpSearchRequest {

  private String email;
  private String name;

}
