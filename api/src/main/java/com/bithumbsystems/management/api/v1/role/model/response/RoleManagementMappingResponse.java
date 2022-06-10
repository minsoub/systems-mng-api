package com.bithumbsystems.management.api.v1.role.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoleManagementMappingResponse {
  private String id;
  private String name;
  private List<String> emailList;
}
