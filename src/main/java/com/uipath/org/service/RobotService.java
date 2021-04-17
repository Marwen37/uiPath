package com.uipath.org.service;

import com.uipath.org.domain.Robot;
import com.uipath.org.repository.RobotRepository;
import com.uipath.org.service.dto.RobotDTO;
import com.uipath.org.service.mapper.RobotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Robot}.
 */
@Service
@Transactional
public class RobotService {

    private final Logger log = LoggerFactory.getLogger(RobotService.class);

    private final RobotRepository robotRepository;

    private final RobotMapper robotMapper;

    public RobotService(RobotRepository robotRepository, RobotMapper robotMapper) {
        this.robotRepository = robotRepository;
        this.robotMapper = robotMapper;
    }

    /**
     * Save a robot.
     *
     * @param robotDTO the entity to save.
     * @return the persisted entity.
     */
    public RobotDTO save(RobotDTO robotDTO) {
        log.debug("Request to save Robot : {}", robotDTO);
        Robot robot = robotMapper.toEntity(robotDTO);
        robot = robotRepository.save(robot);
        return robotMapper.toDto(robot);
    }

    /**
     * Partially update a robot.
     *
     * @param robotDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RobotDTO> partialUpdate(RobotDTO robotDTO) {
        log.debug("Request to partially update Robot : {}", robotDTO);

        return robotRepository
            .findById(robotDTO.getId())
            .map(
                existingRobot -> {
                    robotMapper.partialUpdate(existingRobot, robotDTO);
                    return existingRobot;
                }
            )
            .map(robotRepository::save)
            .map(robotMapper::toDto);
    }

    /**
     * Get all the robots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RobotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Robots");
        return robotRepository.findAll(pageable).map(robotMapper::toDto);
    }

    /**
     * Get one robot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RobotDTO> findOne(Long id) {
        log.debug("Request to get Robot : {}", id);
        return robotRepository.findById(id).map(robotMapper::toDto);
    }

    /**
     * Delete the robot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Robot : {}", id);
        robotRepository.deleteById(id);
    }
}
