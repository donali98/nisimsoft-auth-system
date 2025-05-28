package com.nisimsoft.auth_system.dtos.requests;

import com.nisimsoft.auth_system.annotations.ExistsInDatabase;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class AssignRolesToUserRequest {

  @Min(value = 1, message = "El id del usaurio debe ser mayor que 0")
  @ExistsInDatabase(table = "ns_users", column = "id", entityName = "usuario")
  private Long id;

  @NotNull(message = "La lista de roles no puede ser nula")
  @NotEmpty(message = "Debe proporcionar al menos un id de rol")
  @Size(min = 1, message = "Debe haber al menos 1 id de rol")
  private List<@Positive(message = "Los IDs de roles deben ser positivos") Long> roles;
}
