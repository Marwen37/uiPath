package com.uipath.org.service;

import com.uipath.org.domain.Machine;
import com.uipath.org.repository.MachineRepository;
import com.uipath.org.service.dto.MachineDTO;
import com.uipath.org.service.mapper.MachineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Machine}.
 */
@Service
@Transactional
public class MachineService {

    private final Logger log = LoggerFactory.getLogger(MachineService.class);

    private final MachineRepository machineRepository;

    private final MachineMapper machineMapper;

    public MachineService(MachineRepository machineRepository, MachineMapper machineMapper) {
        this.machineRepository = machineRepository;
        this.machineMapper = machineMapper;
    }

    /**
     * Save a machine.
     *
     * @param machineDTO the entity to save.
     * @return the persisted entity.
     */
    public MachineDTO save(MachineDTO machineDTO) {
        log.debug("Request to save Machine : {}", machineDTO);
        Machine machine = machineMapper.toEntity(machineDTO);
        machine = machineRepository.save(machine);
        return machineMapper.toDto(machine);
    }

    /**
     * Partially update a machine.
     *
     * @param machineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MachineDTO> partialUpdate(MachineDTO machineDTO) {
        log.debug("Request to partially update Machine : {}", machineDTO);

        return machineRepository
            .findById(machineDTO.getId())
            .map(
                existingMachine -> {
                    machineMapper.partialUpdate(existingMachine, machineDTO);
                    return existingMachine;
                }
            )
            .map(machineRepository::save)
            .map(machineMapper::toDto);
    }

    /**
     * Get all the machines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MachineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Machines");
        return machineRepository.findAll(pageable).map(machineMapper::toDto);
    }

    /**
     * Get one machine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MachineDTO> findOne(Long id) {
        log.debug("Request to get Machine : {}", id);
        return machineRepository.findById(id).map(machineMapper::toDto);
    }

    /**
     * Delete the machine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Machine : {}", id);
        machineRepository.deleteById(id);
    }
}
