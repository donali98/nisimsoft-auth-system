package com.nisimsoft.auth_system.dtos.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFilterRequest extends GlobalFilterRequest {
    private String name;
    private String username;
    private String email;
    private boolean isActive;
}
