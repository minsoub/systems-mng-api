package com.bithumbsystems.persistence.mongodb.role.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "role_authorization")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoleAuthorization {
  @Id
  private String id;
  private String roleManagementId;
  private List<AuthorizationResource> authorizationResources;
  private LocalDateTime createDate;
  private String createAdminAccountId;
}
