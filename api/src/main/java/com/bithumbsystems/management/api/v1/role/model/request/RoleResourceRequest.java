package com.bithumbsystems.management.api.v1.role.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResourceRequest {
  private List<RoleResourceListRequest> resources;
//  private String menuId;
//  private List<String> programId;
}
