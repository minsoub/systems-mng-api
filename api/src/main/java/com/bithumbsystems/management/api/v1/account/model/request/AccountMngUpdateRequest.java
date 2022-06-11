package com.bithumbsystems.management.api.v1.account.model.request;

import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMngUpdateRequest {
    private String id;
    private String email;
    private String name;
    private String password;
    private Status status;
    private Boolean isUse;
}
