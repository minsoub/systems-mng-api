package com.bithumbsystems.management.api.v1.account.model.request;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessRegisterRequest {
    private String adminAccountId;
    private String password;
    private Set<String> roles;
    private Boolean isUse;
    private Boolean isSendMail;
}
