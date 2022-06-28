package com.bithumbsystems.persistence.mongodb.menu.model.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "site_menu_program")
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@CompoundIndexes({
    @CompoundIndex(name = "menu_program_site", def = "{'menu_id' : 1, 'program_id': 1, 'site_id': 1}", unique = true)
})
public class SiteMenuProgram {
  @MongoId(targetType = FieldType.STRING)
  private String id;
  private String menuId;
  private String programId;
  private String siteId;
  private LocalDateTime createDate;
  private String createAdminAccountId;
}
