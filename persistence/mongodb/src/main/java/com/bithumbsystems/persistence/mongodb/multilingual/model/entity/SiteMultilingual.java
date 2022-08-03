package com.bithumbsystems.persistence.mongodb.multilingual.model.entity;

import com.bithumbsystems.persistence.mongodb.multilingual.model.enums.MultilingualType;
import com.bithumbsystems.persistence.mongodb.util.base.entity.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("site_multilingual_info")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class SiteMultilingual extends Date {

  @MongoId
  private String id;
  private String siteId;
  private String siteName;
  private Boolean isUse;
  private String kor;
  private String eng;
  private MultilingualType type;

}
