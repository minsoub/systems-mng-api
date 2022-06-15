package com.bithumbsystems.persistence.mongodb.menu.model.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "site_menu_program")
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class SiteMenuProgram {
  @Id
  private String id;
  private String menuId;
  private String programId;
  private String siteId;
  private LocalDateTime createDate;
  private String createAdminAccountId;
}
