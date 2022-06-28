package com.bithumbsystems.management.api.v1.account.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdatePasswordRequest {
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
