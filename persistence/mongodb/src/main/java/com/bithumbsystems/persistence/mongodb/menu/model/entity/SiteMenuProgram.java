package com.bithumbsystems.persistence.mongodb.menu.model.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "site_menu_program")
@AllArgsConstructor
@Getter
@Setter
public class SiteMenuProgram {
  @Id
  private String id;
  private String menuId;
  private String programId;
  private String siteId;
  @CreatedDate
  private LocalDateTime createDate;
  private String createAdminAccountId;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private String updateAdminAccountId;
}
