package com.nisimsoft.auth_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nisimsoft.auth_system.entities.Program;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, encontrar un programa por su nombre o ID
    // Optional<Program> findByName(String name);

}
