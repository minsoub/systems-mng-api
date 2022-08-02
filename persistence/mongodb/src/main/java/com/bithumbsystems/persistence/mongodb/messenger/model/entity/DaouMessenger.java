package com.bithumbsystems.persistence.mongodb.messenger.model.entity;

import com.bithumbsystems.persistence.mongodb.util.base.entity.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document("daou_messenger_info")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DaouMessenger extends Date {

  @MongoId
  private String id;
  private String host;
  private String locale;
  private String user;
  private String pass;
  private String loginApi;
  private String sessionApi;
  private String pubsubApi;
  private Boolean isUse;

}
