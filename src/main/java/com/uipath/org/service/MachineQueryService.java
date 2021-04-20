package com.uipath.org.service;

import com.uipath.org.domain.*; // for static metamodels
import com.uipath.org.domain.Machine;
import com.uipath.org.repository.MachineRepository;
import com.uipath.org.service.criteria.MachineCriteria;
import com.uipath.org.service.dto.MachineDTO;
import com.uipath.org.service.mapper.MachineMapper;
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
 * Service for executing complex queries for {@link Machine} entities in the database.
 * The main input is a {@link MachineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MachineDTO} or a {@link Page} of {@link MachineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MachineQueryService extends QueryService<Machine> {

    private final Logger log = LoggerFactory.getLogger(MachineQueryService.class);

    private final MachineRepository machineRepository;

    private final MachineMapper machineMapper;

    public MachineQueryService(MachineRepository machineRepository, MachineMapper machineMapper) {
        this.machineRepository = machineRepository;
        this.machineMapper = machineMapper;
    }

    /**
     * Return a {@link List} of {@link MachineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MachineDTO> findByCriteria(MachineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Machine> specification = createSpecification(criteria);
        return machineMapper.toDto(machineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MachineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MachineDTO> findByCriteria(MachineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Machine> specification = createSpecification(criteria);
        return machineRepository.findAll(specification, page).map(machineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MachineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Machine> specification = createSpecification(criteria);
        return machineRepository.count(specification);
    }

    /**
     * Function to convert {@link MachineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Machine> createSpecification(MachineCriteria criteria) {
        Specification<Machine> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Machine_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Machine_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Machine_.description));
            }
            if (criteria.getLicence() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLicence(), Machine_.licence));
            }
            if (criteria.getUrobotId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUrobotId(), root -> root.join(Machine_.urobot, JoinType.LEFT).get(Urobot_.id))
                    );
            }
        }
        return specification;
    }
}
