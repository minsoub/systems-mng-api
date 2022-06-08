package com.bithumbsystems.persistence.mongodb.menu.model.entity;

import com.bithumbsystems.persistence.mongodb.menu.model.enums.MenuType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "menu")
@AllArgsConstructor
@Getter
@Setter
public class Menu {
  @Id
  private String id;
  private String name;
  private Boolean isUse;
  private String url;
  private MenuType type;
  private Boolean target;
  private String icon;
  private Boolean externalLink;
  private Integer order;
  private String parentsMenuId;
  private String description;
  private String siteId;
  @CreatedDate
  private LocalDateTime createDate;
  private String createAdminAccountId;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private String updateAdminAccountId;

  @Transient
  private List<SiteMenuProgram> siteMenuProgram;
}
