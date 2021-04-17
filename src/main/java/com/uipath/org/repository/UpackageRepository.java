package com.uipath.org.repository;

import com.uipath.org.domain.Upackage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Upackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UpackageRepository extends JpaRepository<Upackage, Long>, JpaSpecificationExecutor<Upackage> {}
