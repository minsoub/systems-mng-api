package com.bithumbsystems.management.api.v1.multilingual.model.request;

import com.bithumbsystems.persistence.mongodb.multilingual.model.enums.MultilingualType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MultilingualRequest {
  private String id;
  private String siteId;
  private String siteName;
  private Boolean isUse;
  private String kor;
  private String eng;
  private MultilingualType type;
}
