package com.uipath.org.service;

import com.uipath.org.domain.*; // for static metamodels
import com.uipath.org.domain.Robot;
import com.uipath.org.repository.RobotRepository;
import com.uipath.org.service.criteria.RobotCriteria;
import com.uipath.org.service.dto.RobotDTO;
import com.uipath.org.service.mapper.RobotMapper;
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
 * Service for executing complex queries for {@link Robot} entities in the database.
 * The main input is a {@link RobotCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RobotDTO} or a {@link Page} of {@link RobotDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RobotQueryService extends QueryService<Robot> {

    private final Logger log = LoggerFactory.getLogger(RobotQueryService.class);

    private final RobotRepository robotRepository;

    private final RobotMapper robotMapper;

    public RobotQueryService(RobotRepository robotRepository, RobotMapper robotMapper) {
        this.robotRepository = robotRepository;
        this.robotMapper = robotMapper;
    }

    /**
     * Return a {@link List} of {@link RobotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RobotDTO> findByCriteria(RobotCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Robot> specification = createSpecification(criteria);
        return robotMapper.toDto(robotRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RobotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RobotDTO> findByCriteria(RobotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Robot> specification = createSpecification(criteria);
        return robotRepository.findAll(specification, page).map(robotMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RobotCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Robot> specification = createSpecification(criteria);
        return robotRepository.count(specification);
    }

    /**
     * Function to convert {@link RobotCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Robot> createSpecification(RobotCriteria criteria) {
        Specification<Robot> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Robot_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Robot_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Robot_.description));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Robot_.type));
            }
            if (criteria.getDomainUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDomainUsername(), Robot_.domainUsername));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Robot_.password));
            }
            if (criteria.getMachineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMachineId(), root -> root.join(Robot_.machines, JoinType.LEFT).get(Machine_.id))
                    );
            }
            if (criteria.getEnvironmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEnvironmentId(),
                            root -> root.join(Robot_.environment, JoinType.LEFT).get(Environment_.id)
                        )
                    );
            }
            if (criteria.getEnvironmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEnvironmentId(),
                            root -> root.join(Robot_.environment, JoinType.LEFT).get(Environment_.id)
                        )
                    );
            }
            if (criteria.getMachineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMachineId(), root -> root.join(Robot_.machines, JoinType.LEFT).get(Machine_.id))
                    );
            }
        }
        return specification;
    }
}
