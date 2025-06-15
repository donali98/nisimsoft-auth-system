package com.nisimsoft.auth_system.dtos.requests;

import com.nisimsoft.auth_system.annotations.ExistsInDatabase;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Min(value = 1, message = "El id del usaurio debe ser mayor que 0")
    @ExistsInDatabase(table = "ns_users", column = "id", entityName = "usuario")
    private Long id;

    @NotNull(message = "El nombre es obligatorio")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+([\\s-][A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$", message = "El nombre solo puede contener letras, espacios y guiones")
    private String name;

    @NotNull(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, message = "El nombre de usuario debe tener al menos 3 caracteres")
    @Size(max = 50, message = "El nombre de usuario no puede exceder los 50 caracteres")
    private String username;

    @NotNull(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Size(max = 15, message = "La contraseña no puede exceder los 20 caracteres")
    private String password;

}
