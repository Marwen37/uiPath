package com.uipath.org.repository;

import com.uipath.org.domain.Environment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Environment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, Long>, JpaSpecificationExecutor<Environment> {}
