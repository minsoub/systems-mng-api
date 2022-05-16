package com.bithumbsystems.management.api.core.config.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MongoProperty {

  String mongodbUrl;
  String mongodbUser;
  String mongodbPassword;
  String mongodbPort;
  String mongodbName;
}