package com.uipath.org.service;

import com.uipath.org.domain.Environment;
import com.uipath.org.repository.EnvironmentRepository;
import com.uipath.org.service.dto.EnvironmentDTO;
import com.uipath.org.service.mapper.EnvironmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Environment}.
 */
@Service
@Transactional
public class EnvironmentService {

    private final Logger log = LoggerFactory.getLogger(EnvironmentService.class);

    private final EnvironmentRepository environmentRepository;

    private final EnvironmentMapper environmentMapper;

    public EnvironmentService(EnvironmentRepository environmentRepository, EnvironmentMapper environmentMapper) {
        this.environmentRepository = environmentRepository;
        this.environmentMapper = environmentMapper;
    }

    /**
     * Save a environment.
     *
     * @param environmentDTO the entity to save.
     * @return the persisted entity.
     */
    public EnvironmentDTO save(EnvironmentDTO environmentDTO) {
        log.debug("Request to save Environment : {}", environmentDTO);
        Environment environment = environmentMapper.toEntity(environmentDTO);
        environment = environmentRepository.save(environment);
        return environmentMapper.toDto(environment);
    }

    /**
     * Partially update a environment.
     *
     * @param environmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EnvironmentDTO> partialUpdate(EnvironmentDTO environmentDTO) {
        log.debug("Request to partially update Environment : {}", environmentDTO);

        return environmentRepository
            .findById(environmentDTO.getId())
            .map(
                existingEnvironment -> {
                    environmentMapper.partialUpdate(existingEnvironment, environmentDTO);
                    return existingEnvironment;
                }
            )
            .map(environmentRepository::save)
            .map(environmentMapper::toDto);
    }

    /**
     * Get all the environments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EnvironmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Environments");
        return environmentRepository.findAll(pageable).map(environmentMapper::toDto);
    }

    /**
     * Get one environment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EnvironmentDTO> findOne(Long id) {
        log.debug("Request to get Environment : {}", id);
        return environmentRepository.findById(id).map(environmentMapper::toDto);
    }

    /**
     * Delete the environment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Environment : {}", id);
        environmentRepository.deleteById(id);
    }
}
