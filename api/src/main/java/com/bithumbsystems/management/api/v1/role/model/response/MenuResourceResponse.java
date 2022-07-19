package com.bithumbsystems.management.api.v1.role.model.response;

import java.time.LocalDateTime;
import java.util.List;

import com.bithumbsystems.persistence.mongodb.menu.model.enums.MenuType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MenuResourceResponse {
  private String id;
  private String name;
  private Boolean visible;
  private Integer order;
  private String parentsMenuId;
  private MenuType type;
  private String url;
  private Boolean target;
  private Boolean externalLink;
  private String siteId;
  private LocalDateTime createDate;
  private List<MenuResourceResponse> childMenuResources;
  private List<ProgramResourceResponse> programList;
}
