package com.bithumbsystems.persistence.mongodb.audit.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "audit_log")
@AllArgsConstructor
@Getter
public class AuditLog {

  @MongoId
  private Long seq;
  private String adminAccountId;
  private String adminAccountName;
  private String ip;
  private String menuId;
  private String menuName;
  private String crud;
  private String siteMenuProgramId;
  private String siteMenuProgramName;
  private String url;
  private String parameter;
  private String createDate;
  private String siteId;
  private String siteName;
  private String roleManagementId;
  private String roleManagementName;
  private String referer;
  private String device;
  private String result;
  private String message;
}
