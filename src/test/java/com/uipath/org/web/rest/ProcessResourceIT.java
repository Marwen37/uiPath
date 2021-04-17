package com.uipath.org.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uipath.org.IntegrationTest;
import com.uipath.org.domain.Environment;
import com.uipath.org.domain.Process;
import com.uipath.org.domain.Upackage;
import com.uipath.org.domain.enumeration.Priority;
import com.uipath.org.repository.ProcessRepository;
import com.uipath.org.service.criteria.ProcessCriteria;
import com.uipath.org.service.dto.ProcessDTO;
import com.uipath.org.service.mapper.ProcessMapper;
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
 * Integration tests for the {@link ProcessResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProcessResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Priority DEFAULT_JOB_PRIORITY = Priority.Low;
    private static final Priority UPDATED_JOB_PRIORITY = Priority.Normal;

    private static final String ENTITY_API_URL = "/api/processes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProcessMockMvc;

    private Process process;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Process createEntity(EntityManager em) {
        Process process = new Process().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).jobPriority(DEFAULT_JOB_PRIORITY);
        return process;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Process createUpdatedEntity(EntityManager em) {
        Process process = new Process().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).jobPriority(UPDATED_JOB_PRIORITY);
        return process;
    }

    @BeforeEach
    public void initTest() {
        process = createEntity(em);
    }

    @Test
    @Transactional
    void createProcess() throws Exception {
        int databaseSizeBeforeCreate = processRepository.findAll().size();
        // Create the Process
        ProcessDTO processDTO = processMapper.toDto(process);
        restProcessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeCreate + 1);
        Process testProcess = processList.get(processList.size() - 1);
        assertThat(testProcess.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProcess.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProcess.getJobPriority()).isEqualTo(DEFAULT_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void createProcessWithExistingId() throws Exception {
        // Create the Process with an existing ID
        process.setId(1L);
        ProcessDTO processDTO = processMapper.toDto(process);

        int databaseSizeBeforeCreate = processRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProcesses() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList
        restProcessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(process.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].jobPriority").value(hasItem(DEFAULT_JOB_PRIORITY.toString())));
    }

    @Test
    @Transactional
    void getProcess() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get the process
        restProcessMockMvc
            .perform(get(ENTITY_API_URL_ID, process.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(process.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.jobPriority").value(DEFAULT_JOB_PRIORITY.toString()));
    }

    @Test
    @Transactional
    void getProcessesByIdFiltering() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        Long id = process.getId();

        defaultProcessShouldBeFound("id.equals=" + id);
        defaultProcessShouldNotBeFound("id.notEquals=" + id);

        defaultProcessShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProcessShouldNotBeFound("id.greaterThan=" + id);

        defaultProcessShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProcessShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProcessesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where name equals to DEFAULT_NAME
        defaultProcessShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the processList where name equals to UPDATED_NAME
        defaultProcessShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProcessesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where name not equals to DEFAULT_NAME
        defaultProcessShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the processList where name not equals to UPDATED_NAME
        defaultProcessShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProcessesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProcessShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the processList where name equals to UPDATED_NAME
        defaultProcessShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProcessesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where name is not null
        defaultProcessShouldBeFound("name.specified=true");

        // Get all the processList where name is null
        defaultProcessShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessesByNameContainsSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where name contains DEFAULT_NAME
        defaultProcessShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the processList where name contains UPDATED_NAME
        defaultProcessShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProcessesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where name does not contain DEFAULT_NAME
        defaultProcessShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the processList where name does not contain UPDATED_NAME
        defaultProcessShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProcessesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where description equals to DEFAULT_DESCRIPTION
        defaultProcessShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the processList where description equals to UPDATED_DESCRIPTION
        defaultProcessShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProcessesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where description not equals to DEFAULT_DESCRIPTION
        defaultProcessShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the processList where description not equals to UPDATED_DESCRIPTION
        defaultProcessShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProcessesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProcessShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the processList where description equals to UPDATED_DESCRIPTION
        defaultProcessShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProcessesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where description is not null
        defaultProcessShouldBeFound("description.specified=true");

        // Get all the processList where description is null
        defaultProcessShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where description contains DEFAULT_DESCRIPTION
        defaultProcessShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the processList where description contains UPDATED_DESCRIPTION
        defaultProcessShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProcessesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where description does not contain DEFAULT_DESCRIPTION
        defaultProcessShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the processList where description does not contain UPDATED_DESCRIPTION
        defaultProcessShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProcessesByJobPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where jobPriority equals to DEFAULT_JOB_PRIORITY
        defaultProcessShouldBeFound("jobPriority.equals=" + DEFAULT_JOB_PRIORITY);

        // Get all the processList where jobPriority equals to UPDATED_JOB_PRIORITY
        defaultProcessShouldNotBeFound("jobPriority.equals=" + UPDATED_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProcessesByJobPriorityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where jobPriority not equals to DEFAULT_JOB_PRIORITY
        defaultProcessShouldNotBeFound("jobPriority.notEquals=" + DEFAULT_JOB_PRIORITY);

        // Get all the processList where jobPriority not equals to UPDATED_JOB_PRIORITY
        defaultProcessShouldBeFound("jobPriority.notEquals=" + UPDATED_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProcessesByJobPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where jobPriority in DEFAULT_JOB_PRIORITY or UPDATED_JOB_PRIORITY
        defaultProcessShouldBeFound("jobPriority.in=" + DEFAULT_JOB_PRIORITY + "," + UPDATED_JOB_PRIORITY);

        // Get all the processList where jobPriority equals to UPDATED_JOB_PRIORITY
        defaultProcessShouldNotBeFound("jobPriority.in=" + UPDATED_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void getAllProcessesByJobPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        // Get all the processList where jobPriority is not null
        defaultProcessShouldBeFound("jobPriority.specified=true");

        // Get all the processList where jobPriority is null
        defaultProcessShouldNotBeFound("jobPriority.specified=false");
    }

    @Test
    @Transactional
    void getAllProcessesByUpackageIsEqualToSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);
        Upackage upackage = UpackageResourceIT.createEntity(em);
        em.persist(upackage);
        em.flush();
        process.addUpackage(upackage);
        processRepository.saveAndFlush(process);
        Long upackageId = upackage.getId();

        // Get all the processList where upackage equals to upackageId
        defaultProcessShouldBeFound("upackageId.equals=" + upackageId);

        // Get all the processList where upackage equals to (upackageId + 1)
        defaultProcessShouldNotBeFound("upackageId.equals=" + (upackageId + 1));
    }

    @Test
    @Transactional
    void getAllProcessesByEnvironmentIsEqualToSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);
        Environment environment = EnvironmentResourceIT.createEntity(em);
        em.persist(environment);
        em.flush();
        process.addEnvironment(environment);
        processRepository.saveAndFlush(process);
        Long environmentId = environment.getId();

        // Get all the processList where environment equals to environmentId
        defaultProcessShouldBeFound("environmentId.equals=" + environmentId);

        // Get all the processList where environment equals to (environmentId + 1)
        defaultProcessShouldNotBeFound("environmentId.equals=" + (environmentId + 1));
    }

    @Test
    @Transactional
    void getAllProcessesByUpackageIsEqualToSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);
        Upackage upackage = UpackageResourceIT.createEntity(em);
        em.persist(upackage);
        em.flush();
        process.addUpackage(upackage);
        processRepository.saveAndFlush(process);
        Long upackageId = upackage.getId();

        // Get all the processList where upackage equals to upackageId
        defaultProcessShouldBeFound("upackageId.equals=" + upackageId);

        // Get all the processList where upackage equals to (upackageId + 1)
        defaultProcessShouldNotBeFound("upackageId.equals=" + (upackageId + 1));
    }

    @Test
    @Transactional
    void getAllProcessesByEnvironmentIsEqualToSomething() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);
        Environment environment = EnvironmentResourceIT.createEntity(em);
        em.persist(environment);
        em.flush();
        process.addEnvironment(environment);
        processRepository.saveAndFlush(process);
        Long environmentId = environment.getId();

        // Get all the processList where environment equals to environmentId
        defaultProcessShouldBeFound("environmentId.equals=" + environmentId);

        // Get all the processList where environment equals to (environmentId + 1)
        defaultProcessShouldNotBeFound("environmentId.equals=" + (environmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProcessShouldBeFound(String filter) throws Exception {
        restProcessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(process.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].jobPriority").value(hasItem(DEFAULT_JOB_PRIORITY.toString())));

        // Check, that the count call also returns 1
        restProcessMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProcessShouldNotBeFound(String filter) throws Exception {
        restProcessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProcessMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProcess() throws Exception {
        // Get the process
        restProcessMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProcess() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        int databaseSizeBeforeUpdate = processRepository.findAll().size();

        // Update the process
        Process updatedProcess = processRepository.findById(process.getId()).get();
        // Disconnect from session so that the updates on updatedProcess are not directly saved in db
        em.detach(updatedProcess);
        updatedProcess.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).jobPriority(UPDATED_JOB_PRIORITY);
        ProcessDTO processDTO = processMapper.toDto(updatedProcess);

        restProcessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processDTO))
            )
            .andExpect(status().isOk());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
        Process testProcess = processList.get(processList.size() - 1);
        assertThat(testProcess.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProcess.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProcess.getJobPriority()).isEqualTo(UPDATED_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void putNonExistingProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().size();
        process.setId(count.incrementAndGet());

        // Create the Process
        ProcessDTO processDTO = processMapper.toDto(process);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, processDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().size();
        process.setId(count.incrementAndGet());

        // Create the Process
        ProcessDTO processDTO = processMapper.toDto(process);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().size();
        process.setId(count.incrementAndGet());

        // Create the Process
        ProcessDTO processDTO = processMapper.toDto(process);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(processDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProcessWithPatch() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        int databaseSizeBeforeUpdate = processRepository.findAll().size();

        // Update the process using partial update
        Process partialUpdatedProcess = new Process();
        partialUpdatedProcess.setId(process.getId());

        partialUpdatedProcess.name(UPDATED_NAME);

        restProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcess.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcess))
            )
            .andExpect(status().isOk());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
        Process testProcess = processList.get(processList.size() - 1);
        assertThat(testProcess.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProcess.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProcess.getJobPriority()).isEqualTo(DEFAULT_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateProcessWithPatch() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        int databaseSizeBeforeUpdate = processRepository.findAll().size();

        // Update the process using partial update
        Process partialUpdatedProcess = new Process();
        partialUpdatedProcess.setId(process.getId());

        partialUpdatedProcess.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).jobPriority(UPDATED_JOB_PRIORITY);

        restProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProcess.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProcess))
            )
            .andExpect(status().isOk());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
        Process testProcess = processList.get(processList.size() - 1);
        assertThat(testProcess.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProcess.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProcess.getJobPriority()).isEqualTo(UPDATED_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void patchNonExistingProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().size();
        process.setId(count.incrementAndGet());

        // Create the Process
        ProcessDTO processDTO = processMapper.toDto(process);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, processDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().size();
        process.setId(count.incrementAndGet());

        // Create the Process
        ProcessDTO processDTO = processMapper.toDto(process);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProcess() throws Exception {
        int databaseSizeBeforeUpdate = processRepository.findAll().size();
        process.setId(count.incrementAndGet());

        // Create the Process
        ProcessDTO processDTO = processMapper.toDto(process);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProcessMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(processDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Process in the database
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProcess() throws Exception {
        // Initialize the database
        processRepository.saveAndFlush(process);

        int databaseSizeBeforeDelete = processRepository.findAll().size();

        // Delete the process
        restProcessMockMvc
            .perform(delete(ENTITY_API_URL_ID, process.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Process> processList = processRepository.findAll();
        assertThat(processList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
