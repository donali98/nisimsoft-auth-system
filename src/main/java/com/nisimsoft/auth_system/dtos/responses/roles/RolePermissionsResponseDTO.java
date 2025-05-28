package com.nisimsoft.auth_system.dtos.responses.roles;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonPropertyOrder({"id", "name", "description", "permissions"})
public record RolePermissionsResponseDTO(
    Long id, String name, String description, List<PermissionResponseDTO> roles) {}
