package com.nisimsoft.auth_system.dtos.responses.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nisimsoft.auth_system.dtos.responses.roles.RoleResponseDTO;
import java.util.List;

@JsonPropertyOrder({"id", "name", "username", "email", "roles"})
public record AssignRoleToUserResponseDTO(
    Long id, String name, String username, String email, List<RoleResponseDTO> roles) {}
