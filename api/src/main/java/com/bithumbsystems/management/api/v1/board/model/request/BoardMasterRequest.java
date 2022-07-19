package com.bithumbsystems.management.api.v1.board.model.request;

import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster.Auth;
import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster.Sns;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BoardMasterRequest {
  private String id;
  private String siteId;
  private String name;
  private Boolean isUse;
  private String type;
  private Boolean isAllowComment;
  private Boolean isAllowReply;
  private Boolean isAllowAttachFile;
  private Boolean isUseCategory;
  private List<String> categories;
  private String paginationType;
  private Integer countPerPage;
  private Boolean isUseTag;
  private List<String> tags;
  private Sns snsShare;
  private Auth auth;
  private Boolean isUseSecret;
}
