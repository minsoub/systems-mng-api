package com.bithumbsystems.management.api.v1.audit.model.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuditLogSearchRequest {

  private final String searchText;
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;
  private final Integer page;
  private final Integer size;
}
