package com.nisimsoft.auth_system.dtos.requests;

import com.nisimsoft.auth_system.entities.enums.PermissionTypeEnum;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SavePermissionRequest {

  @NotBlank(message = "El nombre del permiso es obligatorio")
  private String name;

  @NotBlank(message = "El valor del permiso es obligatorio")
  @Pattern(regexp = "^[a-z]+(_[a-z]+)*$", message = "El valor debe estar en formato snake_case, solo letras minúsculas y guiones bajos simples")
  private String value;

  @NotNull(message = "El tipo de permiso es obligatorio")
  private PermissionTypeEnum type;

  @Size(min = 3, max = 50, message = "La URI del programa debe tener entre 3 y 50 caracteres")
  private String programUri;

  @AssertTrue(message = "El campo 'programUri' es obligatorio cuando el tipo de permiso es SYSTEM")
  public boolean isProgramUriValidForSystemType() {
    if (type == PermissionTypeEnum.SYSTEM) {
      return programUri != null && !programUri.isBlank();
    }
    return true;
  }
}
