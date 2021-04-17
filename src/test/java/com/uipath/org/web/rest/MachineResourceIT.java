package com.uipath.org.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uipath.org.IntegrationTest;
import com.uipath.org.domain.Machine;
import com.uipath.org.domain.Robot;
import com.uipath.org.repository.MachineRepository;
import com.uipath.org.service.criteria.MachineCriteria;
import com.uipath.org.service.dto.MachineDTO;
import com.uipath.org.service.mapper.MachineMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MachineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MachineResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_LICENCE = 1;
    private static final Integer UPDATED_LICENCE = 2;
    private static final Integer SMALLER_LICENCE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/machines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private MachineMapper machineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMachineMockMvc;

    private Machine machine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machine createEntity(EntityManager em) {
        Machine machine = new Machine().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).licence(DEFAULT_LICENCE);
        return machine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machine createUpdatedEntity(EntityManager em) {
        Machine machine = new Machine().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).licence(UPDATED_LICENCE);
        return machine;
    }

    @BeforeEach
    public void initTest() {
        machine = createEntity(em);
    }

    @Test
    @Transactional
    void createMachine() throws Exception {
        int databaseSizeBeforeCreate = machineRepository.findAll().size();
        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);
        restMachineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeCreate + 1);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMachine.getLicence()).isEqualTo(DEFAULT_LICENCE);
    }

    @Test
    @Transactional
    void createMachineWithExistingId() throws Exception {
        // Create the Machine with an existing ID
        machine.setId(1L);
        MachineDTO machineDTO = machineMapper.toDto(machine);

        int databaseSizeBeforeCreate = machineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMachineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMachines() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].licence").value(hasItem(DEFAULT_LICENCE)));
    }

    @Test
    @Transactional
    void getMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get the machine
        restMachineMockMvc
            .perform(get(ENTITY_API_URL_ID, machine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(machine.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.licence").value(DEFAULT_LICENCE));
    }

    @Test
    @Transactional
    void getMachinesByIdFiltering() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        Long id = machine.getId();

        defaultMachineShouldBeFound("id.equals=" + id);
        defaultMachineShouldNotBeFound("id.notEquals=" + id);

        defaultMachineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMachineShouldNotBeFound("id.greaterThan=" + id);

        defaultMachineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMachineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMachinesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where name equals to DEFAULT_NAME
        defaultMachineShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the machineList where name equals to UPDATED_NAME
        defaultMachineShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachinesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where name not equals to DEFAULT_NAME
        defaultMachineShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the machineList where name not equals to UPDATED_NAME
        defaultMachineShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachinesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMachineShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the machineList where name equals to UPDATED_NAME
        defaultMachineShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachinesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where name is not null
        defaultMachineShouldBeFound("name.specified=true");

        // Get all the machineList where name is null
        defaultMachineShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesByNameContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where name contains DEFAULT_NAME
        defaultMachineShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the machineList where name contains UPDATED_NAME
        defaultMachineShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachinesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where name does not contain DEFAULT_NAME
        defaultMachineShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the machineList where name does not contain UPDATED_NAME
        defaultMachineShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMachinesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where description equals to DEFAULT_DESCRIPTION
        defaultMachineShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the machineList where description equals to UPDATED_DESCRIPTION
        defaultMachineShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachinesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where description not equals to DEFAULT_DESCRIPTION
        defaultMachineShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the machineList where description not equals to UPDATED_DESCRIPTION
        defaultMachineShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachinesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMachineShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the machineList where description equals to UPDATED_DESCRIPTION
        defaultMachineShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachinesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where description is not null
        defaultMachineShouldBeFound("description.specified=true");

        // Get all the machineList where description is null
        defaultMachineShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where description contains DEFAULT_DESCRIPTION
        defaultMachineShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the machineList where description contains UPDATED_DESCRIPTION
        defaultMachineShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachinesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where description does not contain DEFAULT_DESCRIPTION
        defaultMachineShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the machineList where description does not contain UPDATED_DESCRIPTION
        defaultMachineShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMachinesByLicenceIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where licence equals to DEFAULT_LICENCE
        defaultMachineShouldBeFound("licence.equals=" + DEFAULT_LICENCE);

        // Get all the machineList where licence equals to UPDATED_LICENCE
        defaultMachineShouldNotBeFound("licence.equals=" + UPDATED_LICENCE);
    }

    @Test
    @Transactional
    void getAllMachinesByLicenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where licence not equals to DEFAULT_LICENCE
        defaultMachineShouldNotBeFound("licence.notEquals=" + DEFAULT_LICENCE);

        // Get all the machineList where licence not equals to UPDATED_LICENCE
        defaultMachineShouldBeFound("licence.notEquals=" + UPDATED_LICENCE);
    }

    @Test
    @Transactional
    void getAllMachinesByLicenceIsInShouldWork() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where licence in DEFAULT_LICENCE or UPDATED_LICENCE
        defaultMachineShouldBeFound("licence.in=" + DEFAULT_LICENCE + "," + UPDATED_LICENCE);

        // Get all the machineList where licence equals to UPDATED_LICENCE
        defaultMachineShouldNotBeFound("licence.in=" + UPDATED_LICENCE);
    }

    @Test
    @Transactional
    void getAllMachinesByLicenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where licence is not null
        defaultMachineShouldBeFound("licence.specified=true");

        // Get all the machineList where licence is null
        defaultMachineShouldNotBeFound("licence.specified=false");
    }

    @Test
    @Transactional
    void getAllMachinesByLicenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where licence is greater than or equal to DEFAULT_LICENCE
        defaultMachineShouldBeFound("licence.greaterThanOrEqual=" + DEFAULT_LICENCE);

        // Get all the machineList where licence is greater than or equal to UPDATED_LICENCE
        defaultMachineShouldNotBeFound("licence.greaterThanOrEqual=" + UPDATED_LICENCE);
    }

    @Test
    @Transactional
    void getAllMachinesByLicenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where licence is less than or equal to DEFAULT_LICENCE
        defaultMachineShouldBeFound("licence.lessThanOrEqual=" + DEFAULT_LICENCE);

        // Get all the machineList where licence is less than or equal to SMALLER_LICENCE
        defaultMachineShouldNotBeFound("licence.lessThanOrEqual=" + SMALLER_LICENCE);
    }

    @Test
    @Transactional
    void getAllMachinesByLicenceIsLessThanSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where licence is less than DEFAULT_LICENCE
        defaultMachineShouldNotBeFound("licence.lessThan=" + DEFAULT_LICENCE);

        // Get all the machineList where licence is less than UPDATED_LICENCE
        defaultMachineShouldBeFound("licence.lessThan=" + UPDATED_LICENCE);
    }

    @Test
    @Transactional
    void getAllMachinesByLicenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList where licence is greater than DEFAULT_LICENCE
        defaultMachineShouldNotBeFound("licence.greaterThan=" + DEFAULT_LICENCE);

        // Get all the machineList where licence is greater than SMALLER_LICENCE
        defaultMachineShouldBeFound("licence.greaterThan=" + SMALLER_LICENCE);
    }

    @Test
    @Transactional
    void getAllMachinesByRobotIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);
        Robot robot = RobotResourceIT.createEntity(em);
        em.persist(robot);
        em.flush();
        machine.setRobot(robot);
        machineRepository.saveAndFlush(machine);
        Long robotId = robot.getId();

        // Get all the machineList where robot equals to robotId
        defaultMachineShouldBeFound("robotId.equals=" + robotId);

        // Get all the machineList where robot equals to (robotId + 1)
        defaultMachineShouldNotBeFound("robotId.equals=" + (robotId + 1));
    }

    @Test
    @Transactional
    void getAllMachinesByRobotIsEqualToSomething() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);
        Robot robot = RobotResourceIT.createEntity(em);
        em.persist(robot);
        em.flush();
        machine.setRobot(robot);
        machineRepository.saveAndFlush(machine);
        Long robotId = robot.getId();

        // Get all the machineList where robot equals to robotId
        defaultMachineShouldBeFound("robotId.equals=" + robotId);

        // Get all the machineList where robot equals to (robotId + 1)
        defaultMachineShouldNotBeFound("robotId.equals=" + (robotId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMachineShouldBeFound(String filter) throws Exception {
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].licence").value(hasItem(DEFAULT_LICENCE)));

        // Check, that the count call also returns 1
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMachineShouldNotBeFound(String filter) throws Exception {
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMachineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMachine() throws Exception {
        // Get the machine
        restMachineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeUpdate = machineRepository.findAll().size();

        // Update the machine
        Machine updatedMachine = machineRepository.findById(machine.getId()).get();
        // Disconnect from session so that the updates on updatedMachine are not directly saved in db
        em.detach(updatedMachine);
        updatedMachine.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).licence(UPDATED_LICENCE);
        MachineDTO machineDTO = machineMapper.toDto(updatedMachine);

        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, machineDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachine.getLicence()).isEqualTo(UPDATED_LICENCE);
    }

    @Test
    @Transactional
    void putNonExistingMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, machineDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMachineWithPatch() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeUpdate = machineRepository.findAll().size();

        // Update the machine using partial update
        Machine partialUpdatedMachine = new Machine();
        partialUpdatedMachine.setId(machine.getId());

        partialUpdatedMachine.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMachine))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachine.getLicence()).isEqualTo(DEFAULT_LICENCE);
    }

    @Test
    @Transactional
    void fullUpdateMachineWithPatch() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeUpdate = machineRepository.findAll().size();

        // Update the machine using partial update
        Machine partialUpdatedMachine = new Machine();
        partialUpdatedMachine.setId(machine.getId());

        partialUpdatedMachine.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).licence(UPDATED_LICENCE);

        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMachine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMachine))
            )
            .andExpect(status().isOk());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMachine.getLicence()).isEqualTo(UPDATED_LICENCE);
    }

    @Test
    @Transactional
    void patchNonExistingMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, machineDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();
        machine.setId(count.incrementAndGet());

        // Create the Machine
        MachineDTO machineDTO = machineMapper.toDto(machine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMachineMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(machineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeDelete = machineRepository.findAll().size();

        // Delete the machine
        restMachineMockMvc
            .perform(delete(ENTITY_API_URL_ID, machine.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
