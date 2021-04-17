package com.uipath.org.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uipath.org.IntegrationTest;
import com.uipath.org.domain.Environment;
import com.uipath.org.domain.Process;
import com.uipath.org.domain.Robot;
import com.uipath.org.repository.EnvironmentRepository;
import com.uipath.org.service.criteria.EnvironmentCriteria;
import com.uipath.org.service.dto.EnvironmentDTO;
import com.uipath.org.service.mapper.EnvironmentMapper;
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
 * Integration tests for the {@link EnvironmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnvironmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/environments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private EnvironmentMapper environmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnvironmentMockMvc;

    private Environment environment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Environment createEntity(EntityManager em) {
        Environment environment = new Environment().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return environment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Environment createUpdatedEntity(EntityManager em) {
        Environment environment = new Environment().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return environment;
    }

    @BeforeEach
    public void initTest() {
        environment = createEntity(em);
    }

    @Test
    @Transactional
    void createEnvironment() throws Exception {
        int databaseSizeBeforeCreate = environmentRepository.findAll().size();
        // Create the Environment
        EnvironmentDTO environmentDTO = environmentMapper.toDto(environment);
        restEnvironmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(environmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeCreate + 1);
        Environment testEnvironment = environmentList.get(environmentList.size() - 1);
        assertThat(testEnvironment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEnvironment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createEnvironmentWithExistingId() throws Exception {
        // Create the Environment with an existing ID
        environment.setId(1L);
        EnvironmentDTO environmentDTO = environmentMapper.toDto(environment);

        int databaseSizeBeforeCreate = environmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnvironmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(environmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEnvironments() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList
        restEnvironmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(environment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getEnvironment() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get the environment
        restEnvironmentMockMvc
            .perform(get(ENTITY_API_URL_ID, environment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(environment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getEnvironmentsByIdFiltering() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        Long id = environment.getId();

        defaultEnvironmentShouldBeFound("id.equals=" + id);
        defaultEnvironmentShouldNotBeFound("id.notEquals=" + id);

        defaultEnvironmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEnvironmentShouldNotBeFound("id.greaterThan=" + id);

        defaultEnvironmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEnvironmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEnvironmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where name equals to DEFAULT_NAME
        defaultEnvironmentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the environmentList where name equals to UPDATED_NAME
        defaultEnvironmentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEnvironmentsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where name not equals to DEFAULT_NAME
        defaultEnvironmentShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the environmentList where name not equals to UPDATED_NAME
        defaultEnvironmentShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEnvironmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEnvironmentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the environmentList where name equals to UPDATED_NAME
        defaultEnvironmentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEnvironmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where name is not null
        defaultEnvironmentShouldBeFound("name.specified=true");

        // Get all the environmentList where name is null
        defaultEnvironmentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEnvironmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where name contains DEFAULT_NAME
        defaultEnvironmentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the environmentList where name contains UPDATED_NAME
        defaultEnvironmentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEnvironmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where name does not contain DEFAULT_NAME
        defaultEnvironmentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the environmentList where name does not contain UPDATED_NAME
        defaultEnvironmentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEnvironmentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where description equals to DEFAULT_DESCRIPTION
        defaultEnvironmentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the environmentList where description equals to UPDATED_DESCRIPTION
        defaultEnvironmentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEnvironmentsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where description not equals to DEFAULT_DESCRIPTION
        defaultEnvironmentShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the environmentList where description not equals to UPDATED_DESCRIPTION
        defaultEnvironmentShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEnvironmentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEnvironmentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the environmentList where description equals to UPDATED_DESCRIPTION
        defaultEnvironmentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEnvironmentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where description is not null
        defaultEnvironmentShouldBeFound("description.specified=true");

        // Get all the environmentList where description is null
        defaultEnvironmentShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEnvironmentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where description contains DEFAULT_DESCRIPTION
        defaultEnvironmentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the environmentList where description contains UPDATED_DESCRIPTION
        defaultEnvironmentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEnvironmentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        // Get all the environmentList where description does not contain DEFAULT_DESCRIPTION
        defaultEnvironmentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the environmentList where description does not contain UPDATED_DESCRIPTION
        defaultEnvironmentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEnvironmentsByRobotIsEqualToSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);
        Robot robot = RobotResourceIT.createEntity(em);
        em.persist(robot);
        em.flush();
        environment.addRobot(robot);
        environmentRepository.saveAndFlush(environment);
        Long robotId = robot.getId();

        // Get all the environmentList where robot equals to robotId
        defaultEnvironmentShouldBeFound("robotId.equals=" + robotId);

        // Get all the environmentList where robot equals to (robotId + 1)
        defaultEnvironmentShouldNotBeFound("robotId.equals=" + (robotId + 1));
    }

    @Test
    @Transactional
    void getAllEnvironmentsByProcessIsEqualToSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);
        Process process = ProcessResourceIT.createEntity(em);
        em.persist(process);
        em.flush();
        environment.setProcess(process);
        environmentRepository.saveAndFlush(environment);
        Long processId = process.getId();

        // Get all the environmentList where process equals to processId
        defaultEnvironmentShouldBeFound("processId.equals=" + processId);

        // Get all the environmentList where process equals to (processId + 1)
        defaultEnvironmentShouldNotBeFound("processId.equals=" + (processId + 1));
    }

    @Test
    @Transactional
    void getAllEnvironmentsByProcessIsEqualToSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);
        Process process = ProcessResourceIT.createEntity(em);
        em.persist(process);
        em.flush();
        environment.setProcess(process);
        environmentRepository.saveAndFlush(environment);
        Long processId = process.getId();

        // Get all the environmentList where process equals to processId
        defaultEnvironmentShouldBeFound("processId.equals=" + processId);

        // Get all the environmentList where process equals to (processId + 1)
        defaultEnvironmentShouldNotBeFound("processId.equals=" + (processId + 1));
    }

    @Test
    @Transactional
    void getAllEnvironmentsByRobotIsEqualToSomething() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);
        Robot robot = RobotResourceIT.createEntity(em);
        em.persist(robot);
        em.flush();
        environment.addRobot(robot);
        environmentRepository.saveAndFlush(environment);
        Long robotId = robot.getId();

        // Get all the environmentList where robot equals to robotId
        defaultEnvironmentShouldBeFound("robotId.equals=" + robotId);

        // Get all the environmentList where robot equals to (robotId + 1)
        defaultEnvironmentShouldNotBeFound("robotId.equals=" + (robotId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEnvironmentShouldBeFound(String filter) throws Exception {
        restEnvironmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(environment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restEnvironmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEnvironmentShouldNotBeFound(String filter) throws Exception {
        restEnvironmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEnvironmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEnvironment() throws Exception {
        // Get the environment
        restEnvironmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEnvironment() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();

        // Update the environment
        Environment updatedEnvironment = environmentRepository.findById(environment.getId()).get();
        // Disconnect from session so that the updates on updatedEnvironment are not directly saved in db
        em.detach(updatedEnvironment);
        updatedEnvironment.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        EnvironmentDTO environmentDTO = environmentMapper.toDto(updatedEnvironment);

        restEnvironmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, environmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(environmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
        Environment testEnvironment = environmentList.get(environmentList.size() - 1);
        assertThat(testEnvironment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEnvironment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // Create the Environment
        EnvironmentDTO environmentDTO = environmentMapper.toDto(environment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, environmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(environmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // Create the Environment
        EnvironmentDTO environmentDTO = environmentMapper.toDto(environment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(environmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // Create the Environment
        EnvironmentDTO environmentDTO = environmentMapper.toDto(environment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(environmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnvironmentWithPatch() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();

        // Update the environment using partial update
        Environment partialUpdatedEnvironment = new Environment();
        partialUpdatedEnvironment.setId(environment.getId());

        partialUpdatedEnvironment.description(UPDATED_DESCRIPTION);

        restEnvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnvironment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnvironment))
            )
            .andExpect(status().isOk());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
        Environment testEnvironment = environmentList.get(environmentList.size() - 1);
        assertThat(testEnvironment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEnvironment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateEnvironmentWithPatch() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();

        // Update the environment using partial update
        Environment partialUpdatedEnvironment = new Environment();
        partialUpdatedEnvironment.setId(environment.getId());

        partialUpdatedEnvironment.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restEnvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnvironment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnvironment))
            )
            .andExpect(status().isOk());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
        Environment testEnvironment = environmentList.get(environmentList.size() - 1);
        assertThat(testEnvironment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEnvironment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // Create the Environment
        EnvironmentDTO environmentDTO = environmentMapper.toDto(environment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, environmentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(environmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // Create the Environment
        EnvironmentDTO environmentDTO = environmentMapper.toDto(environment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(environmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnvironment() throws Exception {
        int databaseSizeBeforeUpdate = environmentRepository.findAll().size();
        environment.setId(count.incrementAndGet());

        // Create the Environment
        EnvironmentDTO environmentDTO = environmentMapper.toDto(environment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(environmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Environment in the database
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnvironment() throws Exception {
        // Initialize the database
        environmentRepository.saveAndFlush(environment);

        int databaseSizeBeforeDelete = environmentRepository.findAll().size();

        // Delete the environment
        restEnvironmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, environment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Environment> environmentList = environmentRepository.findAll();
        assertThat(environmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
