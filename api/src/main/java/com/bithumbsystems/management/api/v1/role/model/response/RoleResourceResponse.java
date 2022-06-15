package com.bithumbsystems.management.api.v1.role.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResourceResponse {
  private String menuId;
  private List<String> programId;
}
