package com.uipath.org.service;

import com.uipath.org.domain.*; // for static metamodels
import com.uipath.org.domain.Process;
import com.uipath.org.repository.ProcessRepository;
import com.uipath.org.service.criteria.ProcessCriteria;
import com.uipath.org.service.dto.ProcessDTO;
import com.uipath.org.service.mapper.ProcessMapper;
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
 * Service for executing complex queries for {@link Process} entities in the database.
 * The main input is a {@link ProcessCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProcessDTO} or a {@link Page} of {@link ProcessDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProcessQueryService extends QueryService<Process> {

    private final Logger log = LoggerFactory.getLogger(ProcessQueryService.class);

    private final ProcessRepository processRepository;

    private final ProcessMapper processMapper;

    public ProcessQueryService(ProcessRepository processRepository, ProcessMapper processMapper) {
        this.processRepository = processRepository;
        this.processMapper = processMapper;
    }

    /**
     * Return a {@link List} of {@link ProcessDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProcessDTO> findByCriteria(ProcessCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Process> specification = createSpecification(criteria);
        return processMapper.toDto(processRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProcessDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProcessDTO> findByCriteria(ProcessCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Process> specification = createSpecification(criteria);
        return processRepository.findAll(specification, page).map(processMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProcessCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Process> specification = createSpecification(criteria);
        return processRepository.count(specification);
    }

    /**
     * Function to convert {@link ProcessCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Process> createSpecification(ProcessCriteria criteria) {
        Specification<Process> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Process_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Process_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Process_.description));
            }
            if (criteria.getJobPriority() != null) {
                specification = specification.and(buildSpecification(criteria.getJobPriority(), Process_.jobPriority));
            }
            if (criteria.getUpackageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUpackageId(), root -> root.join(Process_.upackages, JoinType.LEFT).get(Upackage_.id))
                    );
            }
            if (criteria.getEnvironmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEnvironmentId(),
                            root -> root.join(Process_.environments, JoinType.LEFT).get(Environment_.id)
                        )
                    );
            }
            if (criteria.getUpackageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUpackageId(), root -> root.join(Process_.upackages, JoinType.LEFT).get(Upackage_.id))
                    );
            }
            if (criteria.getEnvironmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEnvironmentId(),
                            root -> root.join(Process_.environments, JoinType.LEFT).get(Environment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
