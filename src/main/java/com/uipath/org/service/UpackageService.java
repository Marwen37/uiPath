package com.uipath.org.service;

import com.uipath.org.domain.Upackage;
import com.uipath.org.repository.UpackageRepository;
import com.uipath.org.service.dto.UpackageDTO;
import com.uipath.org.service.mapper.UpackageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Upackage}.
 */
@Service
@Transactional
public class UpackageService {

    private final Logger log = LoggerFactory.getLogger(UpackageService.class);

    private final UpackageRepository upackageRepository;

    private final UpackageMapper upackageMapper;

    public UpackageService(UpackageRepository upackageRepository, UpackageMapper upackageMapper) {
        this.upackageRepository = upackageRepository;
        this.upackageMapper = upackageMapper;
    }

    /**
     * Save a upackage.
     *
     * @param upackageDTO the entity to save.
     * @return the persisted entity.
     */
    public UpackageDTO save(UpackageDTO upackageDTO) {
        log.debug("Request to save Upackage : {}", upackageDTO);
        Upackage upackage = upackageMapper.toEntity(upackageDTO);
        upackage = upackageRepository.save(upackage);
        return upackageMapper.toDto(upackage);
    }

    /**
     * Partially update a upackage.
     *
     * @param upackageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UpackageDTO> partialUpdate(UpackageDTO upackageDTO) {
        log.debug("Request to partially update Upackage : {}", upackageDTO);

        return upackageRepository
            .findById(upackageDTO.getId())
            .map(
                existingUpackage -> {
                    upackageMapper.partialUpdate(existingUpackage, upackageDTO);
                    return existingUpackage;
                }
            )
            .map(upackageRepository::save)
            .map(upackageMapper::toDto);
    }

    /**
     * Get all the upackages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UpackageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Upackages");
        return upackageRepository.findAll(pageable).map(upackageMapper::toDto);
    }

    /**
     * Get one upackage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UpackageDTO> findOne(Long id) {
        log.debug("Request to get Upackage : {}", id);
        return upackageRepository.findById(id).map(upackageMapper::toDto);
    }

    /**
     * Delete the upackage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Upackage : {}", id);
        upackageRepository.deleteById(id);
    }
}
