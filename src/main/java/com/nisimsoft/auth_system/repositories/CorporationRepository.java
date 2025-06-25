package com.nisimsoft.auth_system.repositories;

import com.nisimsoft.auth_system.entities.Corporation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CorporationRepository extends JpaRepository<Corporation, Long>, JpaSpecificationExecutor<Corporation> {
}
