package com.nisimsoft.auth_system.dtos.requests;

import com.nisimsoft.auth_system.entities.enums.PermissionTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PermissionFilterRequest extends GlobalFilterRequest {
    private PermissionTypeEnum type;
}