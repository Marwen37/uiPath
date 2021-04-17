package com.uipath.org.repository;

import com.uipath.org.domain.Urobot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Urobot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UrobotRepository extends JpaRepository<Urobot, Long>, JpaSpecificationExecutor<Urobot> {}
