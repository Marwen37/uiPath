package com.uipath.org.service;

import com.uipath.org.domain.Ujob;
import com.uipath.org.repository.UjobRepository;
import com.uipath.org.service.dto.UjobDTO;
import com.uipath.org.service.mapper.UjobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ujob}.
 */
@Service
@Transactional
public class UjobService {

    private final Logger log = LoggerFactory.getLogger(UjobService.class);

    private final UjobRepository ujobRepository;

    private final UjobMapper ujobMapper;

    public UjobService(UjobRepository ujobRepository, UjobMapper ujobMapper) {
        this.ujobRepository = ujobRepository;
        this.ujobMapper = ujobMapper;
    }

    /**
     * Save a ujob.
     *
     * @param ujobDTO the entity to save.
     * @return the persisted entity.
     */
    public UjobDTO save(UjobDTO ujobDTO) {
        log.debug("Request to save Ujob : {}", ujobDTO);
        Ujob ujob = ujobMapper.toEntity(ujobDTO);
        ujob = ujobRepository.save(ujob);
        return ujobMapper.toDto(ujob);
    }

    /**
     * Partially update a ujob.
     *
     * @param ujobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UjobDTO> partialUpdate(UjobDTO ujobDTO) {
        log.debug("Request to partially update Ujob : {}", ujobDTO);

        return ujobRepository
            .findById(ujobDTO.getId())
            .map(
                existingUjob -> {
                    ujobMapper.partialUpdate(existingUjob, ujobDTO);
                    return existingUjob;
                }
            )
            .map(ujobRepository::save)
            .map(ujobMapper::toDto);
    }

    /**
     * Get all the ujobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UjobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ujobs");
        return ujobRepository.findAll(pageable).map(ujobMapper::toDto);
    }

    /**
     * Get one ujob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UjobDTO> findOne(Long id) {
        log.debug("Request to get Ujob : {}", id);
        return ujobRepository.findById(id).map(ujobMapper::toDto);
    }

    /**
     * Delete the ujob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ujob : {}", id);
        ujobRepository.deleteById(id);
    }
}
