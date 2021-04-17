package com.uipath.org.service;

import com.uipath.org.domain.*; // for static metamodels
import com.uipath.org.domain.Uenvironment;
import com.uipath.org.repository.UenvironmentRepository;
import com.uipath.org.service.criteria.UenvironmentCriteria;
import com.uipath.org.service.dto.UenvironmentDTO;
import com.uipath.org.service.mapper.UenvironmentMapper;
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
 * Service for executing complex queries for {@link Uenvironment} entities in the database.
 * The main input is a {@link UenvironmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UenvironmentDTO} or a {@link Page} of {@link UenvironmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UenvironmentQueryService extends QueryService<Uenvironment> {

    private final Logger log = LoggerFactory.getLogger(UenvironmentQueryService.class);

    private final UenvironmentRepository uenvironmentRepository;

    private final UenvironmentMapper uenvironmentMapper;

    public UenvironmentQueryService(UenvironmentRepository uenvironmentRepository, UenvironmentMapper uenvironmentMapper) {
        this.uenvironmentRepository = uenvironmentRepository;
        this.uenvironmentMapper = uenvironmentMapper;
    }

    /**
     * Return a {@link List} of {@link UenvironmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UenvironmentDTO> findByCriteria(UenvironmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Uenvironment> specification = createSpecification(criteria);
        return uenvironmentMapper.toDto(uenvironmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UenvironmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UenvironmentDTO> findByCriteria(UenvironmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Uenvironment> specification = createSpecification(criteria);
        return uenvironmentRepository.findAll(specification, page).map(uenvironmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UenvironmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Uenvironment> specification = createSpecification(criteria);
        return uenvironmentRepository.count(specification);
    }

    /**
     * Function to convert {@link UenvironmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Uenvironment> createSpecification(UenvironmentCriteria criteria) {
        Specification<Uenvironment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Uenvironment_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Uenvironment_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Uenvironment_.description));
            }
            if (criteria.getUrobotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUrobotId(), root -> root.join(Uenvironment_.urobots, JoinType.LEFT).get(Urobot_.id))
                    );
            }
            if (criteria.getUprocessId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUprocessId(),
                            root -> root.join(Uenvironment_.uprocess, JoinType.LEFT).get(Uprocess_.id)
                        )
                    );
            }
            if (criteria.getUprocessId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUprocessId(),
                            root -> root.join(Uenvironment_.uprocess, JoinType.LEFT).get(Uprocess_.id)
                        )
                    );
            }
            if (criteria.getUrobotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUrobotId(), root -> root.join(Uenvironment_.urobots, JoinType.LEFT).get(Urobot_.id))
                    );
            }
        }
        return specification;
    }
}
