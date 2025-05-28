package com.nisimsoft.auth_system.dtos.responses.program;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonPropertyOrder({"id", "name", "uri", "icon", "pinned", "children"})
public record ProgramResponseDTO(
    Long id,
    String name,
    String uri,
    String icon,
    Boolean pinned,
    List<ProgramResponseDTO> children) {}
