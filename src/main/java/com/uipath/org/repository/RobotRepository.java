package com.uipath.org.repository;

import com.uipath.org.domain.Robot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Robot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RobotRepository extends JpaRepository<Robot, Long>, JpaSpecificationExecutor<Robot> {}
