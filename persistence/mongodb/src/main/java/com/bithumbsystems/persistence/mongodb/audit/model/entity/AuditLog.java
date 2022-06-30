package com.bithumbsystems.persistence.mongodb.audit.model.entity;

import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "audit_log")
@AllArgsConstructor
@Data
@Builder
public class AuditLog {

  @Transient
  public static final String SEQUENCE_NAME = "audit_log_sequence";

  @MongoId(targetType = FieldType.STRING)
  private Long seq;

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
  private String parameter;
  private String mySiteId;
  private String siteId;
  private String siteName;
  private RoleType roleType;
  private Set<String> roles;
  private String referer;
  private String device;
  private String message;
  private LocalDateTime createDate;
}
