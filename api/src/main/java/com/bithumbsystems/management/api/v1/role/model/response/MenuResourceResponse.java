package com.bithumbsystems.management.api.v1.role.model.response;

import java.time.LocalDateTime;
import java.util.List;
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
  private LocalDateTime createDate;
  private List<MenuResourceResponse> childMenuResources;
  private List<ProgramResourceResponse> programList;
}
