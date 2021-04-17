package com.uipath.org.service;

import com.uipath.org.domain.Uenvironment;
import com.uipath.org.repository.UenvironmentRepository;
import com.uipath.org.service.dto.UenvironmentDTO;
import com.uipath.org.service.mapper.UenvironmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Uenvironment}.
 */
@Service
@Transactional
public class UenvironmentService {

    private final Logger log = LoggerFactory.getLogger(UenvironmentService.class);

    private final UenvironmentRepository uenvironmentRepository;

    private final UenvironmentMapper uenvironmentMapper;

    public UenvironmentService(UenvironmentRepository uenvironmentRepository, UenvironmentMapper uenvironmentMapper) {
        this.uenvironmentRepository = uenvironmentRepository;
        this.uenvironmentMapper = uenvironmentMapper;
    }

    /**
     * Save a uenvironment.
     *
     * @param uenvironmentDTO the entity to save.
     * @return the persisted entity.
     */
    public UenvironmentDTO save(UenvironmentDTO uenvironmentDTO) {
        log.debug("Request to save Uenvironment : {}", uenvironmentDTO);
        Uenvironment uenvironment = uenvironmentMapper.toEntity(uenvironmentDTO);
        uenvironment = uenvironmentRepository.save(uenvironment);
        return uenvironmentMapper.toDto(uenvironment);
    }

    /**
     * Partially update a uenvironment.
     *
     * @param uenvironmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UenvironmentDTO> partialUpdate(UenvironmentDTO uenvironmentDTO) {
        log.debug("Request to partially update Uenvironment : {}", uenvironmentDTO);

        return uenvironmentRepository
            .findById(uenvironmentDTO.getId())
            .map(
                existingUenvironment -> {
                    uenvironmentMapper.partialUpdate(existingUenvironment, uenvironmentDTO);
                    return existingUenvironment;
                }
            )
            .map(uenvironmentRepository::save)
            .map(uenvironmentMapper::toDto);
    }

    /**
     * Get all the uenvironments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UenvironmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Uenvironments");
        return uenvironmentRepository.findAll(pageable).map(uenvironmentMapper::toDto);
    }

    /**
     * Get one uenvironment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UenvironmentDTO> findOne(Long id) {
        log.debug("Request to get Uenvironment : {}", id);
        return uenvironmentRepository.findById(id).map(uenvironmentMapper::toDto);
    }

    /**
     * Delete the uenvironment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Uenvironment : {}", id);
        uenvironmentRepository.deleteById(id);
    }
}
