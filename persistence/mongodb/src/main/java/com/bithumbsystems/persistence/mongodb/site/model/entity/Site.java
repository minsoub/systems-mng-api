package com.bithumbsystems.persistence.mongodb.site.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "site")
@AllArgsConstructor
@Getter
@Setter
public class Site {
  @Transient
  public static final String SEQUENCE_NAME = "site_sequence";

  @MongoId
  private String id;
  private String name;
  private Boolean isUse;
  private LocalDate validStartDate;
  private LocalDate validEndDate;
  private String description;
  private String adminAccountId;
  private String adminAccountEmail;
  private String adminAccountPhone;

  @CreatedDate
  private LocalDateTime createDate;
  private String createAdminAccountId;
  @LastModifiedDate
  private LocalDateTime updateDate;
  private String updateAdminAccountId;
}
