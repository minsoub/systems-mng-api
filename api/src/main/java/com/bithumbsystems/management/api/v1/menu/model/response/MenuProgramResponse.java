package com.bithumbsystems.management.api.v1.menu.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuProgramResponse {

  private String menuId;
  private String programId;
  private String siteId;
  private LocalDateTime createDate;
}
