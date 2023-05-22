package com.uipath.org.repository;

import com.uipath.org.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 * add this to pull
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
