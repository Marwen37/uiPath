package com.uipath.org.service;

import com.uipath.org.domain.*; // for static metamodels
import com.uipath.org.domain.Environment;
import com.uipath.org.repository.EnvironmentRepository;
import com.uipath.org.service.criteria.EnvironmentCriteria;
import com.uipath.org.service.dto.EnvironmentDTO;
import com.uipath.org.service.mapper.EnvironmentMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Environment} entities in the database.
 * The main input is a {@link EnvironmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EnvironmentDTO} or a {@link Page} of {@link EnvironmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EnvironmentQueryService extends QueryService<Environment> {

    private final Logger log = LoggerFactory.getLogger(EnvironmentQueryService.class);

    private final EnvironmentRepository environmentRepository;

    private final EnvironmentMapper environmentMapper;

    public EnvironmentQueryService(EnvironmentRepository environmentRepository, EnvironmentMapper environmentMapper) {
        this.environmentRepository = environmentRepository;
        this.environmentMapper = environmentMapper;
    }

    /**
     * Return a {@link List} of {@link EnvironmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EnvironmentDTO> findByCriteria(EnvironmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Environment> specification = createSpecification(criteria);
        return environmentMapper.toDto(environmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EnvironmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EnvironmentDTO> findByCriteria(EnvironmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Environment> specification = createSpecification(criteria);
        return environmentRepository.findAll(specification, page).map(environmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EnvironmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Environment> specification = createSpecification(criteria);
        return environmentRepository.count(specification);
    }

    /**
     * Function to convert {@link EnvironmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Environment> createSpecification(EnvironmentCriteria criteria) {
        Specification<Environment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Environment_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Environment_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Environment_.description));
            }
            if (criteria.getRobotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRobotId(), root -> root.join(Environment_.robots, JoinType.LEFT).get(Robot_.id))
                    );
            }
            if (criteria.getProcessId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProcessId(), root -> root.join(Environment_.process, JoinType.LEFT).get(Process_.id))
                    );
            }
            if (criteria.getProcessId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProcessId(), root -> root.join(Environment_.process, JoinType.LEFT).get(Process_.id))
                    );
            }
            if (criteria.getRobotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRobotId(), root -> root.join(Environment_.robots, JoinType.LEFT).get(Robot_.id))
                    );
            }
        }
        return specification;
    }
}
