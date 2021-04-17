package com.uipath.org.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uipath.org.IntegrationTest;
import com.uipath.org.domain.Uenvironment;
import com.uipath.org.domain.Upackage;
import com.uipath.org.domain.Uprocess;
import com.uipath.org.domain.enumeration.Priority;
import com.uipath.org.repository.UprocessRepository;
import com.uipath.org.service.criteria.UprocessCriteria;
import com.uipath.org.service.dto.UprocessDTO;
import com.uipath.org.service.mapper.UprocessMapper;
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
 * Integration tests for the {@link UprocessResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UprocessResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Priority DEFAULT_JOB_PRIORITY = Priority.Low;
    private static final Priority UPDATED_JOB_PRIORITY = Priority.Normal;

    private static final String ENTITY_API_URL = "/api/uprocesses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UprocessRepository uprocessRepository;

    @Autowired
    private UprocessMapper uprocessMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUprocessMockMvc;

    private Uprocess uprocess;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Uprocess createEntity(EntityManager em) {
        Uprocess uprocess = new Uprocess().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).jobPriority(DEFAULT_JOB_PRIORITY);
        return uprocess;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Uprocess createUpdatedEntity(EntityManager em) {
        Uprocess uprocess = new Uprocess().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).jobPriority(UPDATED_JOB_PRIORITY);
        return uprocess;
    }

    @BeforeEach
    public void initTest() {
        uprocess = createEntity(em);
    }

    @Test
    @Transactional
    void createUprocess() throws Exception {
        int databaseSizeBeforeCreate = uprocessRepository.findAll().size();
        // Create the Uprocess
        UprocessDTO uprocessDTO = uprocessMapper.toDto(uprocess);
        restUprocessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uprocessDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Uprocess in the database
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeCreate + 1);
        Uprocess testUprocess = uprocessList.get(uprocessList.size() - 1);
        assertThat(testUprocess.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUprocess.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUprocess.getJobPriority()).isEqualTo(DEFAULT_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void createUprocessWithExistingId() throws Exception {
        // Create the Uprocess with an existing ID
        uprocess.setId(1L);
        UprocessDTO uprocessDTO = uprocessMapper.toDto(uprocess);

        int databaseSizeBeforeCreate = uprocessRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUprocessMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uprocessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uprocess in the database
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUprocesses() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList
        restUprocessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uprocess.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].jobPriority").value(hasItem(DEFAULT_JOB_PRIORITY.toString())));
    }

    @Test
    @Transactional
    void getUprocess() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get the uprocess
        restUprocessMockMvc
            .perform(get(ENTITY_API_URL_ID, uprocess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uprocess.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.jobPriority").value(DEFAULT_JOB_PRIORITY.toString()));
    }

    @Test
    @Transactional
    void getUprocessesByIdFiltering() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        Long id = uprocess.getId();

        defaultUprocessShouldBeFound("id.equals=" + id);
        defaultUprocessShouldNotBeFound("id.notEquals=" + id);

        defaultUprocessShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUprocessShouldNotBeFound("id.greaterThan=" + id);

        defaultUprocessShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUprocessShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUprocessesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where name equals to DEFAULT_NAME
        defaultUprocessShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the uprocessList where name equals to UPDATED_NAME
        defaultUprocessShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUprocessesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where name not equals to DEFAULT_NAME
        defaultUprocessShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the uprocessList where name not equals to UPDATED_NAME
        defaultUprocessShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUprocessesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUprocessShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the uprocessList where name equals to UPDATED_NAME
        defaultUprocessShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUprocessesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where name is not null
        defaultUprocessShouldBeFound("name.specified=true");

        // Get all the uprocessList where name is null
        defaultUprocessShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllUprocessesByNameContainsSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where name contains DEFAULT_NAME
        defaultUprocessShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the uprocessList where name contains UPDATED_NAME
        defaultUprocessShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUprocessesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where name does not contain DEFAULT_NAME
        defaultUprocessShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the uprocessList where name does not contain UPDATED_NAME
        defaultUprocessShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUprocessesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where description equals to DEFAULT_DESCRIPTION
        defaultUprocessShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the uprocessList where description equals to UPDATED_DESCRIPTION
        defaultUprocessShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUprocessesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where description not equals to DEFAULT_DESCRIPTION
        defaultUprocessShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the uprocessList where description not equals to UPDATED_DESCRIPTION
        defaultUprocessShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUprocessesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultUprocessShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the uprocessList where description equals to UPDATED_DESCRIPTION
        defaultUprocessShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUprocessesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where description is not null
        defaultUprocessShouldBeFound("description.specified=true");

        // Get all the uprocessList where description is null
        defaultUprocessShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllUprocessesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where description contains DEFAULT_DESCRIPTION
        defaultUprocessShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the uprocessList where description contains UPDATED_DESCRIPTION
        defaultUprocessShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUprocessesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where description does not contain DEFAULT_DESCRIPTION
        defaultUprocessShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the uprocessList where description does not contain UPDATED_DESCRIPTION
        defaultUprocessShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUprocessesByJobPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where jobPriority equals to DEFAULT_JOB_PRIORITY
        defaultUprocessShouldBeFound("jobPriority.equals=" + DEFAULT_JOB_PRIORITY);

        // Get all the uprocessList where jobPriority equals to UPDATED_JOB_PRIORITY
        defaultUprocessShouldNotBeFound("jobPriority.equals=" + UPDATED_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void getAllUprocessesByJobPriorityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where jobPriority not equals to DEFAULT_JOB_PRIORITY
        defaultUprocessShouldNotBeFound("jobPriority.notEquals=" + DEFAULT_JOB_PRIORITY);

        // Get all the uprocessList where jobPriority not equals to UPDATED_JOB_PRIORITY
        defaultUprocessShouldBeFound("jobPriority.notEquals=" + UPDATED_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void getAllUprocessesByJobPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where jobPriority in DEFAULT_JOB_PRIORITY or UPDATED_JOB_PRIORITY
        defaultUprocessShouldBeFound("jobPriority.in=" + DEFAULT_JOB_PRIORITY + "," + UPDATED_JOB_PRIORITY);

        // Get all the uprocessList where jobPriority equals to UPDATED_JOB_PRIORITY
        defaultUprocessShouldNotBeFound("jobPriority.in=" + UPDATED_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void getAllUprocessesByJobPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        // Get all the uprocessList where jobPriority is not null
        defaultUprocessShouldBeFound("jobPriority.specified=true");

        // Get all the uprocessList where jobPriority is null
        defaultUprocessShouldNotBeFound("jobPriority.specified=false");
    }

    @Test
    @Transactional
    void getAllUprocessesByUpackageIsEqualToSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);
        Upackage upackage = UpackageResourceIT.createEntity(em);
        em.persist(upackage);
        em.flush();
        uprocess.addUpackage(upackage);
        uprocessRepository.saveAndFlush(uprocess);
        Long upackageId = upackage.getId();

        // Get all the uprocessList where upackage equals to upackageId
        defaultUprocessShouldBeFound("upackageId.equals=" + upackageId);

        // Get all the uprocessList where upackage equals to (upackageId + 1)
        defaultUprocessShouldNotBeFound("upackageId.equals=" + (upackageId + 1));
    }

    @Test
    @Transactional
    void getAllUprocessesByUenvironmentIsEqualToSomething() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);
        Uenvironment uenvironment = UenvironmentResourceIT.createEntity(em);
        em.persist(uenvironment);
        em.flush();
        uprocess.addUenvironment(uenvironment);
        uprocessRepository.saveAndFlush(uprocess);
        Long uenvironmentId = uenvironment.getId();

        // Get all the uprocessList where uenvironment equals to uenvironmentId
        defaultUprocessShouldBeFound("uenvironmentId.equals=" + uenvironmentId);

        // Get all the uprocessList where uenvironment equals to (uenvironmentId + 1)
        defaultUprocessShouldNotBeFound("uenvironmentId.equals=" + (uenvironmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUprocessShouldBeFound(String filter) throws Exception {
        restUprocessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uprocess.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].jobPriority").value(hasItem(DEFAULT_JOB_PRIORITY.toString())));

        // Check, that the count call also returns 1
        restUprocessMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUprocessShouldNotBeFound(String filter) throws Exception {
        restUprocessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUprocessMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUprocess() throws Exception {
        // Get the uprocess
        restUprocessMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUprocess() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        int databaseSizeBeforeUpdate = uprocessRepository.findAll().size();

        // Update the uprocess
        Uprocess updatedUprocess = uprocessRepository.findById(uprocess.getId()).get();
        // Disconnect from session so that the updates on updatedUprocess are not directly saved in db
        em.detach(updatedUprocess);
        updatedUprocess.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).jobPriority(UPDATED_JOB_PRIORITY);
        UprocessDTO uprocessDTO = uprocessMapper.toDto(updatedUprocess);

        restUprocessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uprocessDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uprocessDTO))
            )
            .andExpect(status().isOk());

        // Validate the Uprocess in the database
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeUpdate);
        Uprocess testUprocess = uprocessList.get(uprocessList.size() - 1);
        assertThat(testUprocess.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUprocess.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUprocess.getJobPriority()).isEqualTo(UPDATED_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void putNonExistingUprocess() throws Exception {
        int databaseSizeBeforeUpdate = uprocessRepository.findAll().size();
        uprocess.setId(count.incrementAndGet());

        // Create the Uprocess
        UprocessDTO uprocessDTO = uprocessMapper.toDto(uprocess);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUprocessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uprocessDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uprocessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uprocess in the database
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUprocess() throws Exception {
        int databaseSizeBeforeUpdate = uprocessRepository.findAll().size();
        uprocess.setId(count.incrementAndGet());

        // Create the Uprocess
        UprocessDTO uprocessDTO = uprocessMapper.toDto(uprocess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUprocessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uprocessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uprocess in the database
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUprocess() throws Exception {
        int databaseSizeBeforeUpdate = uprocessRepository.findAll().size();
        uprocess.setId(count.incrementAndGet());

        // Create the Uprocess
        UprocessDTO uprocessDTO = uprocessMapper.toDto(uprocess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUprocessMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uprocessDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Uprocess in the database
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUprocessWithPatch() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        int databaseSizeBeforeUpdate = uprocessRepository.findAll().size();

        // Update the uprocess using partial update
        Uprocess partialUpdatedUprocess = new Uprocess();
        partialUpdatedUprocess.setId(uprocess.getId());

        partialUpdatedUprocess.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).jobPriority(UPDATED_JOB_PRIORITY);

        restUprocessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUprocess.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUprocess))
            )
            .andExpect(status().isOk());

        // Validate the Uprocess in the database
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeUpdate);
        Uprocess testUprocess = uprocessList.get(uprocessList.size() - 1);
        assertThat(testUprocess.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUprocess.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUprocess.getJobPriority()).isEqualTo(UPDATED_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateUprocessWithPatch() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        int databaseSizeBeforeUpdate = uprocessRepository.findAll().size();

        // Update the uprocess using partial update
        Uprocess partialUpdatedUprocess = new Uprocess();
        partialUpdatedUprocess.setId(uprocess.getId());

        partialUpdatedUprocess.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).jobPriority(UPDATED_JOB_PRIORITY);

        restUprocessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUprocess.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUprocess))
            )
            .andExpect(status().isOk());

        // Validate the Uprocess in the database
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeUpdate);
        Uprocess testUprocess = uprocessList.get(uprocessList.size() - 1);
        assertThat(testUprocess.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUprocess.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUprocess.getJobPriority()).isEqualTo(UPDATED_JOB_PRIORITY);
    }

    @Test
    @Transactional
    void patchNonExistingUprocess() throws Exception {
        int databaseSizeBeforeUpdate = uprocessRepository.findAll().size();
        uprocess.setId(count.incrementAndGet());

        // Create the Uprocess
        UprocessDTO uprocessDTO = uprocessMapper.toDto(uprocess);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUprocessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uprocessDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uprocessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uprocess in the database
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUprocess() throws Exception {
        int databaseSizeBeforeUpdate = uprocessRepository.findAll().size();
        uprocess.setId(count.incrementAndGet());

        // Create the Uprocess
        UprocessDTO uprocessDTO = uprocessMapper.toDto(uprocess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUprocessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uprocessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uprocess in the database
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUprocess() throws Exception {
        int databaseSizeBeforeUpdate = uprocessRepository.findAll().size();
        uprocess.setId(count.incrementAndGet());

        // Create the Uprocess
        UprocessDTO uprocessDTO = uprocessMapper.toDto(uprocess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUprocessMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uprocessDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Uprocess in the database
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUprocess() throws Exception {
        // Initialize the database
        uprocessRepository.saveAndFlush(uprocess);

        int databaseSizeBeforeDelete = uprocessRepository.findAll().size();

        // Delete the uprocess
        restUprocessMockMvc
            .perform(delete(ENTITY_API_URL_ID, uprocess.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Uprocess> uprocessList = uprocessRepository.findAll();
        assertThat(uprocessList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
