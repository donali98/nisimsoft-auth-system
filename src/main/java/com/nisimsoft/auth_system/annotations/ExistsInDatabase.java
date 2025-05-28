package com.nisimsoft.auth_system.annotations;

import com.nisimsoft.auth_system.validators.ExistsInDatabaseValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistsInDatabaseValidator.class)
@Target({ElementType.FIELD}) // Solo sobre campos
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsInDatabase {

  String message() default ""; // Se ignora si se usa entityName

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String table();

  String column();

  String entityName(); // Nuevo campo para personalizar mensaje
}
