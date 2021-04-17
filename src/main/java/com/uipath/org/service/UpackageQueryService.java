package com.uipath.org.service;

import com.uipath.org.domain.*; // for static metamodels
import com.uipath.org.domain.Upackage;
import com.uipath.org.repository.UpackageRepository;
import com.uipath.org.service.criteria.UpackageCriteria;
import com.uipath.org.service.dto.UpackageDTO;
import com.uipath.org.service.mapper.UpackageMapper;
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
 * Service for executing complex queries for {@link Upackage} entities in the database.
 * The main input is a {@link UpackageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UpackageDTO} or a {@link Page} of {@link UpackageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UpackageQueryService extends QueryService<Upackage> {

    private final Logger log = LoggerFactory.getLogger(UpackageQueryService.class);

    private final UpackageRepository upackageRepository;

    private final UpackageMapper upackageMapper;

    public UpackageQueryService(UpackageRepository upackageRepository, UpackageMapper upackageMapper) {
        this.upackageRepository = upackageRepository;
        this.upackageMapper = upackageMapper;
    }

    /**
     * Return a {@link List} of {@link UpackageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UpackageDTO> findByCriteria(UpackageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Upackage> specification = createSpecification(criteria);
        return upackageMapper.toDto(upackageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UpackageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UpackageDTO> findByCriteria(UpackageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Upackage> specification = createSpecification(criteria);
        return upackageRepository.findAll(specification, page).map(upackageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UpackageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Upackage> specification = createSpecification(criteria);
        return upackageRepository.count(specification);
    }

    /**
     * Function to convert {@link UpackageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Upackage> createSpecification(UpackageCriteria criteria) {
        Specification<Upackage> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Upackage_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Upackage_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Upackage_.description));
            }
            if (criteria.getProcessId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProcessId(), root -> root.join(Upackage_.process, JoinType.LEFT).get(Process_.id))
                    );
            }
            if (criteria.getProcessId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProcessId(), root -> root.join(Upackage_.process, JoinType.LEFT).get(Process_.id))
                    );
            }
        }
        return specification;
    }
}
