package com.nisimsoft.auth_system.dtos.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleFilterRequest extends GlobalFilterRequest {
    private String description;
    private String name;
}
