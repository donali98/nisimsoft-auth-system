package com.nisimsoft.auth_system.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveCorpRequest {

    @NotBlank(message = "El motor de la base de datos a conectar es obligatorio")
    private String dbEngine;

    @NotBlank(message = "El nombre de la base de datos a conectar es obligatorio")
    private String dbName;

    @NotBlank(message = "El host donde está alojada la base de datos es obligatorio")
    private String host;

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String name;

    @NotBlank(message = "El usuario del motor de base de datos es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña de usuario del motor de base de datos es obligatorio")
    private String password;
}