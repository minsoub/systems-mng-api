package com.bithumbsystems.persistence.mongodb.menu.model.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "menu_program_specifications")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class MenuProgramSpecifications {
  @MongoId(targetType = FieldType.OBJECT_ID)
  private String id;
  private String path;
  private String element;
  private List<ActionProgram> programs;
}

