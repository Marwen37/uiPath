package com.uipath.org.service;

import com.uipath.org.domain.*; // for static metamodels
import com.uipath.org.domain.Ujob;
import com.uipath.org.repository.UjobRepository;
import com.uipath.org.service.criteria.UjobCriteria;
import com.uipath.org.service.dto.UjobDTO;
import com.uipath.org.service.mapper.UjobMapper;
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
 * Service for executing complex queries for {@link Ujob} entities in the database.
 * The main input is a {@link UjobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UjobDTO} or a {@link Page} of {@link UjobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UjobQueryService extends QueryService<Ujob> {

    private final Logger log = LoggerFactory.getLogger(UjobQueryService.class);

    private final UjobRepository ujobRepository;

    private final UjobMapper ujobMapper;

    public UjobQueryService(UjobRepository ujobRepository, UjobMapper ujobMapper) {
        this.ujobRepository = ujobRepository;
        this.ujobMapper = ujobMapper;
    }

    /**
     * Return a {@link List} of {@link UjobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UjobDTO> findByCriteria(UjobCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ujob> specification = createSpecification(criteria);
        return ujobMapper.toDto(ujobRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UjobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UjobDTO> findByCriteria(UjobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ujob> specification = createSpecification(criteria);
        return ujobRepository.findAll(specification, page).map(ujobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UjobCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ujob> specification = createSpecification(criteria);
        return ujobRepository.count(specification);
    }

    /**
     * Function to convert {@link UjobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ujob> createSpecification(UjobCriteria criteria) {
        Specification<Ujob> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ujob_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Ujob_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Ujob_.description));
            }
        }
        return specification;
    }
}
