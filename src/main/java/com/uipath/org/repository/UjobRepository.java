package com.uipath.org.repository;

import com.uipath.org.domain.Ujob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ujob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UjobRepository extends JpaRepository<Ujob, Long>, JpaSpecificationExecutor<Ujob> {}
