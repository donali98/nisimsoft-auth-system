package com.nisimsoft.auth_system.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class SaveOrUpdateProgramWithRolesRequest {
  private Long id = null;

  @NotBlank(message = "El nombre del programa es obligatorio")
  private String name;

  private String uri = null;

  private Long parentId = null;

  private String icon = null;

  private Boolean pinned = false;

  @NotNull(message = "La lista de id de programas no puede ser nula")
  @Size(min = 0, message = "La lista de id de programas debe contener al menos un id o estar vacía")
  private List<@Positive(message = "Los IDs de permisos deben ser positivos") Long> children;

  @NotNull(message = "La lista de roles no puede ser nula")
  @Size(min = 0, message = "La lista de id de roles debe contener al menos un id o estar vacía")
  private List<@Positive(message = "Los IDs de roles deben ser positivos") Long> roles;
}
