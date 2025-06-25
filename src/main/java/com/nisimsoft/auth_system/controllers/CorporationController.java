package com.nisimsoft.auth_system.controllers;

import com.nisimsoft.auth_system.dtos.requests.CorporationFilterRequest;
import com.nisimsoft.auth_system.dtos.requests.SaveCorpRequest;
import com.nisimsoft.auth_system.dtos.requests.UpdateCorpRequest;
import com.nisimsoft.auth_system.dtos.responses.PaginatedResponse;
import com.nisimsoft.auth_system.dtos.responses.user.CorporationResponseDTO;
import com.nisimsoft.auth_system.entities.Corporation;
import com.nisimsoft.auth_system.entities.enums.NSCorpDBEngineEnum;
import com.nisimsoft.auth_system.repositories.CorporationRepository;
import com.nisimsoft.auth_system.responses.Response;
import com.nisimsoft.auth_system.services.CorporationService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class CorporationController {

    @Autowired
    private CorporationRepository corporationRepository;
    @Autowired
    private CorporationService corporationService;

    @GetMapping("/corporations")
    public ResponseEntity<?> getCorporations(@ModelAttribute CorporationFilterRequest request) {
        Map<String, Object> filters = new HashMap<>();

        if (request.getName() != null) {
            filters.put("name", request.getName());
        }
        if (request.getDbName() != null) {
            filters.put("dbName", request.getDbName());
        }
        if (request.getUsername() != null) {
            filters.put("username", request.getUsername());
        }
        if (request.getHost() != null) {
            filters.put("host", request.getHost());
        }
        if (request.getDbEngine() != null) {
            filters.put("dbEngine", request.getDbEngine());
        }

        Page<Corporation> result = corporationService.getCorporations(
                request.getPage(),
                request.getSize(),
                request.getSearch(),
                request.getSortColumn(),
                request.getSortOrder(),
                filters);

        // Usar el DTO ubicado en responses/corporation explícitamente
        Page<com.nisimsoft.auth_system.dtos.responses.corporation.CorporationResponseDTO> responsePageAlt = result
                .map(corp -> new com.nisimsoft.auth_system.dtos.responses.corporation.CorporationResponseDTO(
                        corp.getId(),
                        corp.getName(),
                        corp.getDbName(),
                        corp.getUsername(),
                        corp.getLogo(),
                        corp.getHost(),
                        corp.getDbEngine()));

        return new Response("Corporaciones obtenidos exitosamente", PaginatedResponse.fromPage(responsePageAlt),
                HttpStatus.OK);

    }

    @PostMapping("/corporations")
    public ResponseEntity<?> saveCorporation(@Valid @RequestBody SaveCorpRequest request) {

        Corporation corporation = new Corporation();

        corporation.setDbEngine(NSCorpDBEngineEnum.valueOf(request.getDbEngine()));
        corporation.setDbName(request.getDbName());
        corporation.setHost(request.getHost());
        corporation.setName(request.getName());
        corporation.setUsername(request.getUsername());
        corporation.setPassword(request.getPassword());

        corporationRepository.save(corporation);
        CorporationResponseDTO responseDTO = new CorporationResponseDTO(corporation.getId(), corporation.getName());

        return new Response(
                "Corporación guardada realizada exitosamente", responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/corporations")
    public ResponseEntity<?> updateCorporation(@Valid @RequestBody UpdateCorpRequest request) {

        Corporation corporation = corporationRepository
                .findById(request.getId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Corporación no encontrada"));

        corporation.setDbEngine(NSCorpDBEngineEnum.valueOf(request.getDbEngine()));
        corporation.setDbName(request.getDbName());
        corporation.setHost(request.getHost());
        corporation.setName(request.getName());
        corporation.setUsername(request.getUsername());
        corporation.setPassword(request.getPassword());

        corporationRepository.save(corporation);

        CorporationResponseDTO responseDTO = new CorporationResponseDTO(request.getId(), corporation.getName());

        return new Response(
                "Corporación actualizada realizada exitosamente", responseDTO, HttpStatus.OK);
    }
}
