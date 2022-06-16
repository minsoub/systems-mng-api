package com.bithumbsystems.management.api.v1.menu.model.response;

import com.bithumbsystems.persistence.mongodb.menu.model.enums.MenuType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MenuDetailResponse {

    private String id;
    private String name;
    private String siteId;
    private String parentsMenuId;
    private String parentsMenuName;
    private Boolean isUse;
    private String url;
    private MenuType type;
    private Boolean target;
    private String icon;
    private Boolean externalLink;
    private Integer order;
    private String description;
}
