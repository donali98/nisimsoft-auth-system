package com.nisimsoft.auth_system.dtos.responses.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "username", "email", "status" })
public record EnableDisableUserResponseDTO(
        Long id,
        String name,
        String username,
        String email,
        Boolean status) {
}
