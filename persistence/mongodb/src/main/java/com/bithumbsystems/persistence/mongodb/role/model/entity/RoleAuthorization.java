package com.bithumbsystems.persistence.mongodb.role.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "role_authorization")
@AllArgsConstructor
@Getter
@Setter
public class RoleAuthorization {
  @Id
  private String id;
  private String role_management_id;
  private List<AuthorizationResource> authorizationResources;
  private String siteId;
  @CreatedDate
  private LocalDateTime createDate;
  private String createAdminAccountId;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private String updateAdminAccountId;
}
