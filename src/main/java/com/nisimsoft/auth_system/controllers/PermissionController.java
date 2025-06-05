package com.nisimsoft.auth_system.controllers;

import com.nisimsoft.auth_system.dtos.requests.PermissionFilterRequest;
import com.nisimsoft.auth_system.dtos.requests.SavePermissionRequest;
import com.nisimsoft.auth_system.dtos.responses.PaginatedResponse;
import com.nisimsoft.auth_system.entities.Permission;
import com.nisimsoft.auth_system.responses.Response;
import com.nisimsoft.auth_system.services.PermissionService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PermissionController {

  @Autowired
  private PermissionService permissionService;

  @PostMapping("/permissions")
  public ResponseEntity<?> savePermission(@Valid @RequestBody SavePermissionRequest request) {

    // Registrar permiso
    Permission permission = permissionService.savePermission(request);

    return new Response(
        "Permiso guardado exitosamente", Map.of("permission", permission), HttpStatus.CREATED);
  }

  @GetMapping("/permissions")
  public ResponseEntity<?> getPermissions(@ModelAttribute PermissionFilterRequest request) {

    Map<String, Object> filters = new HashMap<>();

    if (request.getType() != null) {
      filters.put("type", request.getType());
    }

    Page<Permission> result = permissionService.getPermissions(
        request.getPage(),
        request.getSize(),
        request.getSearch(),
        request.getSortColumn(),
        request.getSortOrder(),
        filters);

    return new Response("Permisos obtenidos exitosamente", PaginatedResponse.fromPage(result), HttpStatus.OK);
  }
}
