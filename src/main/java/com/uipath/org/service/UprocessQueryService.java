package com.uipath.org.service;

import com.uipath.org.domain.*; // for static metamodels
import com.uipath.org.domain.Uprocess;
import com.uipath.org.repository.UprocessRepository;
import com.uipath.org.service.criteria.UprocessCriteria;
import com.uipath.org.service.dto.UprocessDTO;
import com.uipath.org.service.mapper.UprocessMapper;
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
 * Service for executing complex queries for {@link Uprocess} entities in the database.
 * The main input is a {@link UprocessCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UprocessDTO} or a {@link Page} of {@link UprocessDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UprocessQueryService extends QueryService<Uprocess> {

    private final Logger log = LoggerFactory.getLogger(UprocessQueryService.class);

    private final UprocessRepository uprocessRepository;

    private final UprocessMapper uprocessMapper;

    public UprocessQueryService(UprocessRepository uprocessRepository, UprocessMapper uprocessMapper) {
        this.uprocessRepository = uprocessRepository;
        this.uprocessMapper = uprocessMapper;
    }

    /**
     * Return a {@link List} of {@link UprocessDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UprocessDTO> findByCriteria(UprocessCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Uprocess> specification = createSpecification(criteria);
        return uprocessMapper.toDto(uprocessRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UprocessDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UprocessDTO> findByCriteria(UprocessCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Uprocess> specification = createSpecification(criteria);
        return uprocessRepository.findAll(specification, page).map(uprocessMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UprocessCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Uprocess> specification = createSpecification(criteria);
        return uprocessRepository.count(specification);
    }

    /**
     * Function to convert {@link UprocessCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Uprocess> createSpecification(UprocessCriteria criteria) {
        Specification<Uprocess> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Uprocess_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Uprocess_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Uprocess_.description));
            }
            if (criteria.getJobPriority() != null) {
                specification = specification.and(buildSpecification(criteria.getJobPriority(), Uprocess_.jobPriority));
            }
            if (criteria.getUpackageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUpackageId(),
                            root -> root.join(Uprocess_.upackages, JoinType.LEFT).get(Upackage_.id)
                        )
                    );
            }
            if (criteria.getUenvironmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUenvironmentId(),
                            root -> root.join(Uprocess_.uenvironments, JoinType.LEFT).get(Uenvironment_.id)
                        )
                    );
            }
            if (criteria.getUpackageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUpackageId(),
                            root -> root.join(Uprocess_.upackages, JoinType.LEFT).get(Upackage_.id)
                        )
                    );
            }
            if (criteria.getUenvironmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUenvironmentId(),
                            root -> root.join(Uprocess_.uenvironments, JoinType.LEFT).get(Uenvironment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
