package com.bithumbsystems.persistence.mongodb.mail.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("site_mail_info")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SiteMail extends Date{

  @MongoId
  @Field(targetType = FieldType.OBJECT_ID)
  private String id;
  private String siteId;
  private String siteName;
  private String adminUserEmail;
  private Boolean isUse;
  private String serverInfo;
  private String accountId;
  private String accountPassword;

}
