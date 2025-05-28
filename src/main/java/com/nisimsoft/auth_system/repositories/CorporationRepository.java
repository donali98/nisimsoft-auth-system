package com.nisimsoft.auth_system.repositories;

import com.nisimsoft.auth_system.entities.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorporationRepository extends JpaRepository<Corporation, Long> {
  // Aquí puedes agregar métodos personalizados si es necesario
  // Por ejemplo, encontrar una corporación por su nombre o ID
  // Optional<Corporation> findByName(String name);

}
