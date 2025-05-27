package com.nisimsoft.auth_system.services;

import org.springframework.stereotype.Service;

import com.nisimsoft.auth_system.dtos.requests.SaveProgramRequest;
import com.nisimsoft.auth_system.entities.Program;
import com.nisimsoft.auth_system.repositories.ProgramRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgramService {
    private final ProgramRepository programRepository;

    public Program saveProgram(SaveProgramRequest request) {

        Program program = new Program();
        Long parentId = request.getParentId();

        if (parentId != null) {
            Program parent = programRepository.findById(parentId).orElse(null);
            program.setParent(parent);

        } else {
            program.setParent(null);
        }

        program.setName(request.getName());
        program.setUri(request.getUri());
        program.setIcon(request.getIcon());
        program.setPinned(request.getPinned());

        return programRepository.save(program);
    }
}
