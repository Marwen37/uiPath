package com.uipath.org.service;

import com.uipath.org.domain.*; // for static metamodels
import com.uipath.org.domain.Urobot;
import com.uipath.org.repository.UrobotRepository;
import com.uipath.org.service.criteria.UrobotCriteria;
import com.uipath.org.service.dto.UrobotDTO;
import com.uipath.org.service.mapper.UrobotMapper;
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
 * Service for executing complex queries for {@link Urobot} entities in the database.
 * The main input is a {@link UrobotCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UrobotDTO} or a {@link Page} of {@link UrobotDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UrobotQueryService extends QueryService<Urobot> {

    private final Logger log = LoggerFactory.getLogger(UrobotQueryService.class);

    private final UrobotRepository urobotRepository;

    private final UrobotMapper urobotMapper;

    public UrobotQueryService(UrobotRepository urobotRepository, UrobotMapper urobotMapper) {
        this.urobotRepository = urobotRepository;
        this.urobotMapper = urobotMapper;
    }

    /**
     * Return a {@link List} of {@link UrobotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UrobotDTO> findByCriteria(UrobotCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Urobot> specification = createSpecification(criteria);
        return urobotMapper.toDto(urobotRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UrobotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UrobotDTO> findByCriteria(UrobotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Urobot> specification = createSpecification(criteria);
        return urobotRepository.findAll(specification, page).map(urobotMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UrobotCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Urobot> specification = createSpecification(criteria);
        return urobotRepository.count(specification);
    }

    /**
     * Function to convert {@link UrobotCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Urobot> createSpecification(UrobotCriteria criteria) {
        Specification<Urobot> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Urobot_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Urobot_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Urobot_.description));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Urobot_.type));
            }
            if (criteria.getDomainUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDomainUsername(), Urobot_.domainUsername));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Urobot_.password));
            }
            if (criteria.getMachineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMachineId(), root -> root.join(Urobot_.machines, JoinType.LEFT).get(Machine_.id))
                    );
            }
            if (criteria.getUenvironmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUenvironmentId(),
                            root -> root.join(Urobot_.uenvironment, JoinType.LEFT).get(Uenvironment_.id)
                        )
                    );
            }
            if (criteria.getUenvironmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUenvironmentId(),
                            root -> root.join(Urobot_.uenvironment, JoinType.LEFT).get(Uenvironment_.id)
                        )
                    );
            }
            if (criteria.getMachineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMachineId(), root -> root.join(Urobot_.machines, JoinType.LEFT).get(Machine_.id))
                    );
            }
        }
        return specification;
    }
}
