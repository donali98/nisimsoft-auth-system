package com.nisimsoft.auth_system.controllers;

import com.nisimsoft.auth_system.dtos.requests.SaveOrUpdateProgramWithRolesRequest;
import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseWithRolesDTO;
import com.nisimsoft.auth_system.entities.Program;
import com.nisimsoft.auth_system.responses.Response;
import com.nisimsoft.auth_system.services.ProgramService;
import com.nisimsoft.auth_system.utils.ProgramMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProgramController {

  @Autowired
  private ProgramService programService;

  @PostMapping("/programs")
  public ResponseEntity<?> saveOrUpdateProgramWithRoles(
      @Valid @RequestBody SaveOrUpdateProgramWithRolesRequest request) {

    Program program = programService.saveOrUpdateProgramWithRoles(request);

    // Asegurar que los hijos y los roles estén cargados
    program.getChildren().size();
    program.getRoles().size();

    ProgramResponseWithRolesDTO responseDTO = ProgramMapper.toDTOWithRoles(program);

    return new Response("Programa guardado exitosamente", responseDTO, HttpStatus.CREATED);
  }
}
