package com.bithumbsystems.management.api.v1.board.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardMasterListResponse {
  private Integer no;
  private String id;
  private String siteId;
  private String siteName;
  private String name;
  private String type;
  private String typeName;
  private Boolean isUse;
  private LocalDateTime createDate;
}
