package com.bithumbsystems.management.api.v1.menu.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MenuListResponse {

  private String id;
  private String name;
  private Boolean isUse;
  private String siteId;
  private Integer order;
  private List<MenuListResponse> childMenu;
}
