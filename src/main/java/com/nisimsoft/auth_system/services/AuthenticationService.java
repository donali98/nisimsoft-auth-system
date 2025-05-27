package com.nisimsoft.auth_system.services;

import com.nisimsoft.auth_system.dtos.requests.AssignRolesToUserRequest;
import com.nisimsoft.auth_system.dtos.requests.RegisterUserRequest;
import com.nisimsoft.auth_system.entities.Corporation;
import com.nisimsoft.auth_system.entities.Role;
import com.nisimsoft.auth_system.entities.User;
import com.nisimsoft.auth_system.exceptions.auth.AuthenticationFailedException;
import com.nisimsoft.auth_system.exceptions.auth.EmailAlreadyExistsException;
import com.nisimsoft.auth_system.repositories.CorporationRepository;
import com.nisimsoft.auth_system.repositories.RoleRepository;
import com.nisimsoft.auth_system.repositories.UserRepository;
import com.nisimsoft.auth_system.services.providers.AuthenticationProvider;

import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final CorporationRepository corporationRepository;
  private final RoleRepository roleRepository;

  public User registerUser(RegisterUserRequest request) {

    String email = request.getEmail();

    // Verificar si el email ya existe
    if (userRepository.findByEmail(email).isPresent()) {
      throw new EmailAlreadyExistsException("El email ya está registrado");
    }

    User user = new User();
    user.setName(request.getName());
    user.setUsername(request.getUsername());
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    Set<Long> corpIds = request.getCorporationIds();

    if (corpIds != null && !corpIds.isEmpty()) {

      Set<Corporation> corporations = new HashSet<>(corporationRepository.findAllById(corpIds));
      user.setCorporations(corporations);
    }

    return userRepository.save(user);
  }

  public User getUserByEmailOrThrow(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
  }

  public void authenticateOrThrow(AuthenticationProvider provider, String email, String password) {
    if (!provider.authenticate(email, password)) {
      throw new AuthenticationFailedException("Credenciales inválidas");
    }
  }

  public void validateUserBelongsToCorporation(User user, Long corpId) {
    boolean belongs = user.getCorporations()
        .stream()
        .anyMatch(c -> c.getId().equals(corpId));
    if (!belongs) {
      throw new AuthenticationFailedException("El usuario no pertenece a la corporación");
    }
  }

  public User getUserByIdOrThrow(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
  }

  @Transactional
  public User assignRoleToUser(AssignRolesToUserRequest request) {

    User user = userRepository.findById(request.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")); // Si no existe

    List<Long> roleIds = request.getRoles();
    // Buscar los permisos por ID
    List<Role> foundRoles = roleRepository.findAllById(roleIds);

    if (foundRoles.size() != request.getRoles().size()) {
      throw new IllegalArgumentException("Uno o más roles no existen en el sistema");
    }

    // Actualizar el set de permisos
    user.setRoles(new HashSet<>(foundRoles));

    return userRepository.save(user);
  }
}
