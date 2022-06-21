package com.bithumbsystems.persistence.mongodb.site.model.entity;

import com.bithumbsystems.persistence.mongodb.site.model.enums.Extension;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "site_file_info")
@AllArgsConstructor
@Getter
@Setter
public class SiteFileInfo {
  @MongoId(targetType = FieldType.STRING)
  private String id;
  @Indexed
  private String siteId;
  private Integer sizeLimit;
  private Boolean isUse;
  private List<Extension> extensionLimit;
  @CreatedDate
  private LocalDateTime createDate;
  private String createAdminAccountId;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private String updateAdminAccountId;
}
