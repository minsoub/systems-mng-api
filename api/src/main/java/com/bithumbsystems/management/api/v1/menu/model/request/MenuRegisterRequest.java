package com.bithumbsystems.management.api.v1.menu.model.request;

import com.bithumbsystems.persistence.mongodb.menu.model.enums.MenuType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MenuRegisterRequest {

  private String id;
  private String name;
  private String parentsMenuId;
  private Boolean isUse;
  private String url;
  private MenuType type;
  private Boolean target;
  private String icon;
  private Boolean externalLink;
  private Integer order;
  private String description;
}
