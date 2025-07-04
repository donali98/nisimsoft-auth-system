package com.nisimsoft.auth_system.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import lombok.Data;

// Constructor, getters y setters generados por Lombok
// No es necesario escribirlos manualmente debido a la anotación @Data
@Data
public class SaveOrUpdateUserRequest {

  private Long id = null;

  @NotBlank(message = "El nombre es obligatorio")
  @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
  @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
  @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+([\\s-][A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "El nombre solo puede contener letras, espacios y guiones")
  private String name;

  @NotBlank(message = "El nombre de usuario es obligatorio")
  @Size(min = 3, message = "El nombre de usuario debe tener al menos 3 caracteres")
  @Size(max = 50, message = "El nombre de usuario no puede exceder los 50 caracteres")
  private String username;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "Formato de email inválido")
  private String email;

  @NotBlank(message = "La contraseña es obligatoria")
  @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
  @Size(max = 15, message = "La contraseña no puede exceder los 20 caracteres")
  private String password;

  @NotBlank(message = "La confirmación de contraseña es obligatoria")
  private String confirmPassword;

  private List<@NotNull(message = "El ID de la corporación no puede ser nulo") Long> corporationIds;

  private List<@NotNull(message = "El ID del rol no puede ser nulo") Long> roleIds;

  // Validación personalizada para coincidencia de contraseñas
  // @AssertTrue(message = "Las contraseñas no coinciden")
  // public boolean isPasswordMatch() {
  // return password.equals(confirmPassword);
  // return password != null && password.equals(confirmPassword);
  // }
}
