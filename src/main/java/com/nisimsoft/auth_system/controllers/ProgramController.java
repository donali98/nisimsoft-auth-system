package com.nisimsoft.auth_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nisimsoft.auth_system.dtos.requests.SaveProgramRequest;
import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseDTO;
import com.nisimsoft.auth_system.entities.Program;
import com.nisimsoft.auth_system.responses.Response;
import com.nisimsoft.auth_system.services.ProgramService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProgramController {

    @Autowired
    private ProgramService programService;

    @PostMapping("/program")
    public ResponseEntity<?> saveProgram(@Valid @RequestBody SaveProgramRequest request) {

        Program program = programService.saveProgram(request);

        ProgramResponseDTO responseDTO = new ProgramResponseDTO(
                program.getId(),
                program.getName(),
                program.getUri(),
                program.getParent() != null ? program.getParent() : null,
                program.getIcon(),
                program.getPinned());

        return new Response("Programa guardado exitosamente", responseDTO, HttpStatus.CREATED);
    }
}
