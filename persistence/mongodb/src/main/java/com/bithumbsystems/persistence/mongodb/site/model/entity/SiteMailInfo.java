package com.bithumbsystems.persistence.mongodb.site.model.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "site_mail_info")
@AllArgsConstructor
@Getter
@Setter
public class SiteMailInfo {
  @Id
  private String id;
  private String siteId;
  private String adminUserEmail;
  private String serverInfo;
  private Boolean isUse;
  private String accountId;
  private String accountPassword;
  @CreatedDate
  private LocalDateTime createDate;
  private String createAdminAccountId;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private String updateAdminAccountId;
}
