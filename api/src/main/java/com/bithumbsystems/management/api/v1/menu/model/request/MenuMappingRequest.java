package com.bithumbsystems.management.api.v1.menu.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MenuMappingRequest {
  List<String> programIds;
}
