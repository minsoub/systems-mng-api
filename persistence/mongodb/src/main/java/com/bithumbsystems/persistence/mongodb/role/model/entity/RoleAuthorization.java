package com.bithumbsystems.persistence.mongodb.role.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "role_authorization")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoleAuthorization {
  @MongoId(targetType = FieldType.STRING)
  private String id;
  @Indexed
  private String roleManagementId;
  private List<AuthorizationResource> authorizationResources;
  private LocalDateTime createDate;
  private String createAdminAccountId;
}
