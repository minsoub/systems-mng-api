package com.bithumbsystems.management.api.v1.site.model.response;

import com.bithumbsystems.persistence.mongodb.site.model.enums.Extension;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class SiteFileInfoResponse {
  private String id;
  private String siteId;
  private String siteName;
  private Integer sizeLimit;
  private Boolean isUse;
  private List<Extension> extensionLimit;
  private LocalDateTime createDate;
}
