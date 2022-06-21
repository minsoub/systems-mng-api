package com.bithumbsystems.persistence.mongodb.menu.model.entity;

import com.bithumbsystems.persistence.mongodb.menu.model.enums.MenuType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "menu")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Menu {
  @MongoId(targetType = FieldType.STRING)
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
  @Indexed
  private String siteId;
  private LocalDateTime createDate;
  private String createAdminAccountId;
  private LocalDateTime updateDate;
  private String updateAdminAccountId;
}
