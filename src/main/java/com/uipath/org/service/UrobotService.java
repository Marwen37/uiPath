package com.uipath.org.service;

import com.uipath.org.domain.Urobot;
import com.uipath.org.repository.UrobotRepository;
import com.uipath.org.service.dto.UrobotDTO;
import com.uipath.org.service.mapper.UrobotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Urobot}.
 */
@Service
@Transactional
public class UrobotService {

    private final Logger log = LoggerFactory.getLogger(UrobotService.class);

    private final UrobotRepository urobotRepository;

    private final UrobotMapper urobotMapper;

    public UrobotService(UrobotRepository urobotRepository, UrobotMapper urobotMapper) {
        this.urobotRepository = urobotRepository;
        this.urobotMapper = urobotMapper;
    }

    /**
     * Save a urobot.
     *
     * @param urobotDTO the entity to save.
     * @return the persisted entity.
     */
    public UrobotDTO save(UrobotDTO urobotDTO) {
        log.debug("Request to save Urobot : {}", urobotDTO);
        Urobot urobot = urobotMapper.toEntity(urobotDTO);
        urobot = urobotRepository.save(urobot);
        return urobotMapper.toDto(urobot);
    }

    /**
     * Partially update a urobot.
     *
     * @param urobotDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UrobotDTO> partialUpdate(UrobotDTO urobotDTO) {
        log.debug("Request to partially update Urobot : {}", urobotDTO);

        return urobotRepository
            .findById(urobotDTO.getId())
            .map(
                existingUrobot -> {
                    urobotMapper.partialUpdate(existingUrobot, urobotDTO);
                    return existingUrobot;
                }
            )
            .map(urobotRepository::save)
            .map(urobotMapper::toDto);
    }

    /**
     * Get all the urobots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UrobotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Urobots");
        return urobotRepository.findAll(pageable).map(urobotMapper::toDto);
    }

    /**
     * Get one urobot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UrobotDTO> findOne(Long id) {
        log.debug("Request to get Urobot : {}", id);
        return urobotRepository.findById(id).map(urobotMapper::toDto);
    }

    /**
     * Delete the urobot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Urobot : {}", id);
        urobotRepository.deleteById(id);
    }
}
