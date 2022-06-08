package com.bithumbsystems.persistence.mongodb.site.model.entity;

import com.bithumbsystems.persistence.mongodb.site.model.enums.Extension;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "site_file_info")
@AllArgsConstructor
@Getter
@Setter
public class SiteFileInfo {
  @Id
  private String id;
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
