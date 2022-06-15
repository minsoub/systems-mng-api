package com.bithumbsystems.management.api.v1.audit.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuditLogSearchResponse {
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
