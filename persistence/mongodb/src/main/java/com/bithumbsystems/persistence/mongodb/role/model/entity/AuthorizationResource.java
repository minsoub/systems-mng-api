package com.bithumbsystems.persistence.mongodb.role.model.entity;

import java.util.List;
import lombok.Data;

@Data
public class AuthorizationResource {
  private String menuId;
  private Boolean visible;
  private List<String> programId;
}
