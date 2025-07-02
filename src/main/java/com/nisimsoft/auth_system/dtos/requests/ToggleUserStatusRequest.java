package com.nisimsoft.auth_system.dtos.requests;

import com.nisimsoft.auth_system.annotations.ExistsInDatabase;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ToggleUserStatusRequest {
    @ExistsInDatabase(message = "El usuario con el ID proporcionado no existe", entityName = "com.nisimsoft.auth_system.entities.User", table = "ns_users", column = "id")
    private Long id;

    @NotNull(message = "El estado es obligatorio")
    private Boolean isActive;
}
