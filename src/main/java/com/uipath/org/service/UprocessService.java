package com.uipath.org.service;

import com.uipath.org.domain.Uprocess;
import com.uipath.org.repository.UprocessRepository;
import com.uipath.org.service.dto.UprocessDTO;
import com.uipath.org.service.mapper.UprocessMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Uprocess}.
 */
@Service
@Transactional
public class UprocessService {

    private final Logger log = LoggerFactory.getLogger(UprocessService.class);

    private final UprocessRepository uprocessRepository;

    private final UprocessMapper uprocessMapper;

    public UprocessService(UprocessRepository uprocessRepository, UprocessMapper uprocessMapper) {
        this.uprocessRepository = uprocessRepository;
        this.uprocessMapper = uprocessMapper;
    }

    /**
     * Save a uprocess.
     *
     * @param uprocessDTO the entity to save.
     * @return the persisted entity.
     */
    public UprocessDTO save(UprocessDTO uprocessDTO) {
        log.debug("Request to save Uprocess : {}", uprocessDTO);
        Uprocess uprocess = uprocessMapper.toEntity(uprocessDTO);
        uprocess = uprocessRepository.save(uprocess);
        return uprocessMapper.toDto(uprocess);
    }

    /**
     * Partially update a uprocess.
     *
     * @param uprocessDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UprocessDTO> partialUpdate(UprocessDTO uprocessDTO) {
        log.debug("Request to partially update Uprocess : {}", uprocessDTO);

        return uprocessRepository
            .findById(uprocessDTO.getId())
            .map(
                existingUprocess -> {
                    uprocessMapper.partialUpdate(existingUprocess, uprocessDTO);
                    return existingUprocess;
                }
            )
            .map(uprocessRepository::save)
            .map(uprocessMapper::toDto);
    }

    /**
     * Get all the uprocesses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UprocessDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Uprocesses");
        return uprocessRepository.findAll(pageable).map(uprocessMapper::toDto);
    }

    /**
     * Get one uprocess by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UprocessDTO> findOne(Long id) {
        log.debug("Request to get Uprocess : {}", id);
        return uprocessRepository.findById(id).map(uprocessMapper::toDto);
    }

    /**
     * Delete the uprocess by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Uprocess : {}", id);
        uprocessRepository.deleteById(id);
    }
}
