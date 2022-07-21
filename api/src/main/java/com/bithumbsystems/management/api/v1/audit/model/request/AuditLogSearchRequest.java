package com.bithumbsystems.management.api.v1.audit.model.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuditLogSearchRequest {

  private final String searchText = "";
  private final String mySiteId = "";
  private final LocalDateTime startDate = LocalDateTime.now().minusMonths(3);
  private final LocalDateTime endDate = LocalDateTime.now();
  private final Integer page = 0;
  private final Integer size = 10;
}
