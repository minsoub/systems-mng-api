package com.bithumbsystems.persistence.mongodb.menu.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ActionProgram {
  private String actionMethod;
  private String actionUrl;
}
