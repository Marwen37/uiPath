package com.uipath.org.repository;

import com.uipath.org.domain.Uprocess;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Uprocess entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UprocessRepository extends JpaRepository<Uprocess, Long>, JpaSpecificationExecutor<Uprocess> {}
