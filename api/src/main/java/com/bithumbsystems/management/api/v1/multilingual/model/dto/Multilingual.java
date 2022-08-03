package com.bithumbsystems.management.api.v1.multilingual.model.dto;

import com.bithumbsystems.persistence.mongodb.multilingual.model.enums.MultilingualType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class Multilingual {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @ToString
  public static class MultilingualRequest {
    private String id;
    private String siteId;
    private String siteName;
    private Boolean isUse;
    private String kor;
    private String eng;
    private MultilingualType type;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MultilingualResponse {
    private String id;
    private String siteId;
    private String siteName;
    private Boolean isUse;
    private String kor;
    private String eng;
    private MultilingualType type;
  }

}
