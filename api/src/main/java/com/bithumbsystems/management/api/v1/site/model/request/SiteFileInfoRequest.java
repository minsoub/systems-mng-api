package com.bithumbsystems.management.api.v1.site.model.request;

import com.bithumbsystems.persistence.mongodb.site.model.enums.Extension;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SiteFileInfoRequest {
  private Integer sizeLimit;
  private Boolean isUse;
  private List<Extension> extensionLimit;
}
