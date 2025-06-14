package com.nisimsoft.auth_system.controllers;

import com.nisimsoft.auth_system.dtos.requests.AssignRolesToUserRequest;
import com.nisimsoft.auth_system.dtos.requests.LoginRequest;
import com.nisimsoft.auth_system.dtos.requests.RegisterUserRequest;
import com.nisimsoft.auth_system.dtos.requests.UpdateUserRequest;
import com.nisimsoft.auth_system.dtos.requests.UserFilterRequest;
import com.nisimsoft.auth_system.dtos.requests.VerifyUserRequest;
import com.nisimsoft.auth_system.dtos.responses.PaginatedResponse;
import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseWithoutRolesDTO;
import com.nisimsoft.auth_system.dtos.responses.roles.RoleResponseDTO;
import com.nisimsoft.auth_system.dtos.responses.user.AssignRoleToUserResponseDTO;
import com.nisimsoft.auth_system.dtos.responses.user.CorporationResponseDTO;
import com.nisimsoft.auth_system.dtos.responses.user.UserResponseDTO;
import com.nisimsoft.auth_system.entities.Corporation;
import com.nisimsoft.auth_system.entities.Role;
import com.nisimsoft.auth_system.entities.User;
import com.nisimsoft.auth_system.exceptions.auth.AuthenticationFailedException;
import com.nisimsoft.auth_system.responses.Response;
import com.nisimsoft.auth_system.services.AuthProviderFactory;
import com.nisimsoft.auth_system.services.AuthenticationService;
import com.nisimsoft.auth_system.services.ProgramService;
import com.nisimsoft.auth_system.services.providers.AuthenticationProvider;
import com.nisimsoft.auth_system.utils.JwtUtils;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class AuthController {
  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private ProgramService programService;

  @Autowired
  private AuthProviderFactory authProviderFactory;

  @Autowired
  private JwtUtils jwtUtils;

  @Value("${app.auth.provider}")
  private String activeAuthProvider;

  @PostMapping("/users")
  public ResponseEntity<?> update(@Valid @RequestBody UpdateUserRequest request) {

    // Actualizar usuario
    User user = authenticationService.updateUser(request);

    @SuppressWarnings("unchecked")
    List<ProgramResponseWithoutRolesDTO> programTree = (List<ProgramResponseWithoutRolesDTO>) programService
        .getProgramTree(user.getRoles(), false);

    UserResponseDTO responseDTO = new UserResponseDTO(
        user.getId(),
        user.getName(),
        user.getUsername(),
        user.getEmail(),
        mapCorporations(user.getCorporations()),
        mapRoles(user.getRoles()),
        programTree);

    return new Response("Usuario actualizado exitosamente", responseDTO, HttpStatus.CREATED);
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequest request) {

    // Registrar usuario
    User user = authenticationService.registerUser(request);

    // IMPORTANTE: crea una copia segura del set (para evitar que JPA lo gestione al
    // recorrer)
    Set<Corporation> safeCorporations = new HashSet<>(user.getCorporations());

    // Convertir corporaciones a resumen DTO
    List<CorporationResponseDTO> corporationDTOs = mapCorporations(safeCorporations);

    @SuppressWarnings("unchecked")
    List<ProgramResponseWithoutRolesDTO> programTree = (List<ProgramResponseWithoutRolesDTO>) programService
        .getProgramTree(user.getRoles(), false);

    UserResponseDTO responseDTO = new UserResponseDTO(
        user.getId(),
        user.getName(),
        user.getUsername(),
        user.getEmail(),
        corporationDTOs,
        mapRoles(user.getRoles()),
        programTree);

    return new Response("Usuario registrado exitosamente", responseDTO, HttpStatus.CREATED);
  }

  @PostMapping("/verify-user")
  public ResponseEntity<?> verifyUser(@Valid @RequestBody VerifyUserRequest request) {
    // AuthenticationProvider provider = getAuthenticationProvider();

    User user = authenticationService.getUserByEmailOrThrow(request.getEmail());

    // authenticationService.authenticateOrThrow(provider, request.getEmail(),
    // request.getPassword());

    Set<Corporation> safeCorporations = new HashSet<>(user.getCorporations());

    List<CorporationResponseDTO> corporationDTOs = mapCorporations(safeCorporations);

    @SuppressWarnings("unchecked")
    List<ProgramResponseWithoutRolesDTO> programTree = (List<ProgramResponseWithoutRolesDTO>) programService
        .getProgramTree(user.getRoles(), false);

    UserResponseDTO responseDTO = new UserResponseDTO(
        user.getId(),
        user.getName(),
        user.getUsername(),
        user.getEmail(),
        corporationDTOs,
        mapRoles(user.getRoles()),
        programTree);

    return new Response("Usuario encontrado exitosamente", responseDTO, HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    AuthenticationProvider provider = getAuthenticationProvider();

    User user = authenticationService.getUserByIdOrThrow(request.getUserId());

    authenticationService.validateUserBelongsToCorporation(user, request.getCorpId());

    authenticationService.authenticateOrThrow(provider, user.getEmail(), request.getPassword());

    String token = jwtUtils.generateToken(user.getEmail(), request.getCorpId().toString());

    @SuppressWarnings("unchecked")
    List<ProgramResponseWithoutRolesDTO> programTree = (List<ProgramResponseWithoutRolesDTO>) programService
        .getProgramTree(user.getRoles(), false);

    UserResponseDTO userResponseDTO = new UserResponseDTO(
        user.getId(),
        user.getName(),
        user.getUsername(),
        user.getEmail(),
        mapCorporations(user.getCorporations()),
        mapRoles(user.getRoles()),
        programTree);

    return new Response(
        "Autenticación exitosa", Map.of("user", userResponseDTO, "token", token), HttpStatus.OK);
  }

  @PostMapping("/assign-roles-to-user")
  public ResponseEntity<?> assignRoleToUser(@RequestBody @Valid AssignRolesToUserRequest request) {

    User savedUser = authenticationService.assignRoleToUser(request);

    AssignRoleToUserResponseDTO responseDTO = new AssignRoleToUserResponseDTO(
        savedUser.getId(),
        savedUser.getName(),
        savedUser.getUsername(),
        savedUser.getEmail(),
        savedUser.getRoles().stream()
            .map(
                role -> new RoleResponseDTO(role.getId(), role.getName(), role.getDescription()))
            .toList());

    return new Response("Rol asignado al usuario exitosamente", responseDTO, HttpStatus.CREATED);
  }

  @GetMapping("/users")
  public ResponseEntity<?> getUsers(@ModelAttribute UserFilterRequest request) {
    Map<String, Object> filters = new HashMap<>();

    if (request.getName() != null) {
      filters.put("name", request.getName());
    }
    if (request.getUsername() != null) {
      filters.put("username", request.getUsername());
    }
    if (request.getEmail() != null) {
      filters.put("email", request.getEmail());
    }

    Page<User> result = authenticationService.getUsers(
        request.getPage(),
        request.getSize(),
        request.getSearch(),
        request.getSortColumn(),
        request.getSortOrder(),
        filters);

    return new Response("Usuarios obtenidos exitosamente", PaginatedResponse.fromPage(result), HttpStatus.OK);

  }

  private AuthenticationProvider getAuthenticationProvider() {
    AuthenticationProvider provider = authProviderFactory.getProvider(activeAuthProvider);
    if (provider == null) {
      throw new AuthenticationFailedException("Proveedor de autenticación no configurado");
    }
    return provider;
  }

  private List<CorporationResponseDTO> mapCorporations(Set<Corporation> corporations) {
    return corporations.stream()
        .map(corp -> new CorporationResponseDTO(corp.getId(), corp.getName()))
        .toList();
  }

  private List<RoleResponseDTO> mapRoles(Set<Role> roles) {
    return roles.stream()
        .map(corp -> new RoleResponseDTO(corp.getId(), corp.getName(), corp.getDescription()))
        .toList();
  }
}
