package com.bithumbsystems.management.api.v1.accessip.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessIpListResponse {

  private String id;
  private String adminAccountId;
  private String siteName;
  private String email;
  private String name;
  private String allowIp;
  private String roleId;
  private List<String> allowIpList;

}
