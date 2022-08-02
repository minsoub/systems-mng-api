package com.bithumbsystems.persistence.mongodb.util.base.entity;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
public class Date {

  @CreatedDate
  private LocalDateTime createDate;
  @CreatedBy
  private String createAdminAccountId;
  @LastModifiedDate
  private LocalDateTime updateDate;
  @LastModifiedBy
  private String updateAdminAccountId;
}
