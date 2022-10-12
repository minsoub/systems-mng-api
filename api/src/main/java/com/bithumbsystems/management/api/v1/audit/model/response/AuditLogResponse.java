package com.bithumbsystems.management.api.v1.audit.model.response;

import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AuditLogResponse {

  private String id;
  private String email;
  private String ip;
  private String menuId;
  private String menuName;
  private String programId;
  private String programName;
  private String method;
  private String crud;
  private String uri;
  private String path;
  private String queryParams;
  //private String parameter;
  private String siteId;
  private String siteName;
  private RoleType roleType;
  private Set<String> roles;
  private String referer;
  private String device;
  private String message;
  private LocalDateTime createDate;
}