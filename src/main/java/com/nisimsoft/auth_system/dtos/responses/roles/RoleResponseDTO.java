package com.nisimsoft.auth_system.dtos.responses.roles;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "description" })
public record RoleResponseDTO(
        Long id,
        String name,
        String description) {

}
