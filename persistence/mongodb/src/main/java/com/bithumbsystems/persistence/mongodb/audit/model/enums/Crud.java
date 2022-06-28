package com.bithumbsystems.persistence.mongodb.audit.model.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum Crud {
  POST("C"), GET("R"), PUT("U"), DELETE("D");

  @Getter
  private String crud;

  Crud(String crud) {
    this.crud = crud;
  }
}
