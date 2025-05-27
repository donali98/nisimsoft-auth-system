package com.nisimsoft.auth_system.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SaveProgramRequest {
    @NotBlank(message = "El nombre del programa es obligatorio")
    private String name;

    @NotBlank(message = "El valor del nombre de acceso es obligatorio")
    @Pattern(regexp = "^[a-z]+(_[a-z]+)*$", message = "El valor debe estar en formato snake_case, solo letras minúsculas y guiones bajos simples")
    private String accessName;

    private String uri = null;

    private Long parentId = null;

    private String icon = null;

    private Boolean pinned = false;

}
