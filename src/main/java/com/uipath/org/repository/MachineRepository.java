package com.uipath.org.repository;

import com.uipath.org.domain.Machine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Machine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MachineRepository extends JpaRepository<Machine, Long>, JpaSpecificationExecutor<Machine> {}
