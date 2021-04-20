package com.uipath.org.repository;

import com.uipath.org.domain.Uenvironment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Uenvironment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UenvironmentRepository extends JpaRepository<Uenvironment, Long>, JpaSpecificationExecutor<Uenvironment> {}
