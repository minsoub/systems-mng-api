package com.bithumbsystems.management.api.v1.multilingual.model.response;

import com.bithumbsystems.persistence.mongodb.multilingual.model.enums.MultilingualType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultilingualResponse {
  private String id;
  private String siteId;
  private String siteName;
  private Boolean isUse;
  private String kor;
  private String eng;
  private MultilingualType type;
}
