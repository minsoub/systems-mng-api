package com.bithumbsystems.management.api.v1.menu.model.response;

import java.util.List;

import com.bithumbsystems.persistence.mongodb.menu.model.enums.MenuType;
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
  private String parentMenuId;
  private MenuType type;
  private String url;
  private boolean target;
  private boolean externalLink;
  private List<MenuListResponse> childMenu;
}
