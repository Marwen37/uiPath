package com.uipath.org.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uipath.org.IntegrationTest;
import com.uipath.org.domain.Uenvironment;
import com.uipath.org.domain.Uprocess;
import com.uipath.org.domain.Urobot;
import com.uipath.org.repository.UenvironmentRepository;
import com.uipath.org.service.criteria.UenvironmentCriteria;
import com.uipath.org.service.dto.UenvironmentDTO;
import com.uipath.org.service.mapper.UenvironmentMapper;
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
 * Integration tests for the {@link UenvironmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UenvironmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/uenvironments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UenvironmentRepository uenvironmentRepository;

    @Autowired
    private UenvironmentMapper uenvironmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUenvironmentMockMvc;

    private Uenvironment uenvironment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Uenvironment createEntity(EntityManager em) {
        Uenvironment uenvironment = new Uenvironment().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return uenvironment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Uenvironment createUpdatedEntity(EntityManager em) {
        Uenvironment uenvironment = new Uenvironment().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return uenvironment;
    }

    @BeforeEach
    public void initTest() {
        uenvironment = createEntity(em);
    }

    @Test
    @Transactional
    void createUenvironment() throws Exception {
        int databaseSizeBeforeCreate = uenvironmentRepository.findAll().size();
        // Create the Uenvironment
        UenvironmentDTO uenvironmentDTO = uenvironmentMapper.toDto(uenvironment);
        restUenvironmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uenvironmentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Uenvironment in the database
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeCreate + 1);
        Uenvironment testUenvironment = uenvironmentList.get(uenvironmentList.size() - 1);
        assertThat(testUenvironment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUenvironment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createUenvironmentWithExistingId() throws Exception {
        // Create the Uenvironment with an existing ID
        uenvironment.setId(1L);
        UenvironmentDTO uenvironmentDTO = uenvironmentMapper.toDto(uenvironment);

        int databaseSizeBeforeCreate = uenvironmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUenvironmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uenvironmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uenvironment in the database
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUenvironments() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList
        restUenvironmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uenvironment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getUenvironment() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get the uenvironment
        restUenvironmentMockMvc
            .perform(get(ENTITY_API_URL_ID, uenvironment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uenvironment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getUenvironmentsByIdFiltering() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        Long id = uenvironment.getId();

        defaultUenvironmentShouldBeFound("id.equals=" + id);
        defaultUenvironmentShouldNotBeFound("id.notEquals=" + id);

        defaultUenvironmentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUenvironmentShouldNotBeFound("id.greaterThan=" + id);

        defaultUenvironmentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUenvironmentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUenvironmentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where name equals to DEFAULT_NAME
        defaultUenvironmentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the uenvironmentList where name equals to UPDATED_NAME
        defaultUenvironmentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUenvironmentsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where name not equals to DEFAULT_NAME
        defaultUenvironmentShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the uenvironmentList where name not equals to UPDATED_NAME
        defaultUenvironmentShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUenvironmentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUenvironmentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the uenvironmentList where name equals to UPDATED_NAME
        defaultUenvironmentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUenvironmentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where name is not null
        defaultUenvironmentShouldBeFound("name.specified=true");

        // Get all the uenvironmentList where name is null
        defaultUenvironmentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllUenvironmentsByNameContainsSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where name contains DEFAULT_NAME
        defaultUenvironmentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the uenvironmentList where name contains UPDATED_NAME
        defaultUenvironmentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUenvironmentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where name does not contain DEFAULT_NAME
        defaultUenvironmentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the uenvironmentList where name does not contain UPDATED_NAME
        defaultUenvironmentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUenvironmentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where description equals to DEFAULT_DESCRIPTION
        defaultUenvironmentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the uenvironmentList where description equals to UPDATED_DESCRIPTION
        defaultUenvironmentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUenvironmentsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where description not equals to DEFAULT_DESCRIPTION
        defaultUenvironmentShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the uenvironmentList where description not equals to UPDATED_DESCRIPTION
        defaultUenvironmentShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUenvironmentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultUenvironmentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the uenvironmentList where description equals to UPDATED_DESCRIPTION
        defaultUenvironmentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUenvironmentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where description is not null
        defaultUenvironmentShouldBeFound("description.specified=true");

        // Get all the uenvironmentList where description is null
        defaultUenvironmentShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllUenvironmentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where description contains DEFAULT_DESCRIPTION
        defaultUenvironmentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the uenvironmentList where description contains UPDATED_DESCRIPTION
        defaultUenvironmentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUenvironmentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        // Get all the uenvironmentList where description does not contain DEFAULT_DESCRIPTION
        defaultUenvironmentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the uenvironmentList where description does not contain UPDATED_DESCRIPTION
        defaultUenvironmentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUenvironmentsByUrobotIsEqualToSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);
        Urobot urobot = UrobotResourceIT.createEntity(em);
        em.persist(urobot);
        em.flush();
        uenvironment.addUrobot(urobot);
        uenvironmentRepository.saveAndFlush(uenvironment);
        Long urobotId = urobot.getId();

        // Get all the uenvironmentList where urobot equals to urobotId
        defaultUenvironmentShouldBeFound("urobotId.equals=" + urobotId);

        // Get all the uenvironmentList where urobot equals to (urobotId + 1)
        defaultUenvironmentShouldNotBeFound("urobotId.equals=" + (urobotId + 1));
    }

    @Test
    @Transactional
    void getAllUenvironmentsByUprocessIsEqualToSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);
        Uprocess uprocess = UprocessResourceIT.createEntity(em);
        em.persist(uprocess);
        em.flush();
        uenvironment.setUprocess(uprocess);
        uenvironmentRepository.saveAndFlush(uenvironment);
        Long uprocessId = uprocess.getId();

        // Get all the uenvironmentList where uprocess equals to uprocessId
        defaultUenvironmentShouldBeFound("uprocessId.equals=" + uprocessId);

        // Get all the uenvironmentList where uprocess equals to (uprocessId + 1)
        defaultUenvironmentShouldNotBeFound("uprocessId.equals=" + (uprocessId + 1));
    }

    @Test
    @Transactional
    void getAllUenvironmentsByUprocessIsEqualToSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);
        Uprocess uprocess = UprocessResourceIT.createEntity(em);
        em.persist(uprocess);
        em.flush();
        uenvironment.setUprocess(uprocess);
        uenvironmentRepository.saveAndFlush(uenvironment);
        Long uprocessId = uprocess.getId();

        // Get all the uenvironmentList where uprocess equals to uprocessId
        defaultUenvironmentShouldBeFound("uprocessId.equals=" + uprocessId);

        // Get all the uenvironmentList where uprocess equals to (uprocessId + 1)
        defaultUenvironmentShouldNotBeFound("uprocessId.equals=" + (uprocessId + 1));
    }

    @Test
    @Transactional
    void getAllUenvironmentsByUrobotIsEqualToSomething() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);
        Urobot urobot = UrobotResourceIT.createEntity(em);
        em.persist(urobot);
        em.flush();
        uenvironment.addUrobot(urobot);
        uenvironmentRepository.saveAndFlush(uenvironment);
        Long urobotId = urobot.getId();

        // Get all the uenvironmentList where urobot equals to urobotId
        defaultUenvironmentShouldBeFound("urobotId.equals=" + urobotId);

        // Get all the uenvironmentList where urobot equals to (urobotId + 1)
        defaultUenvironmentShouldNotBeFound("urobotId.equals=" + (urobotId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUenvironmentShouldBeFound(String filter) throws Exception {
        restUenvironmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uenvironment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restUenvironmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUenvironmentShouldNotBeFound(String filter) throws Exception {
        restUenvironmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUenvironmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUenvironment() throws Exception {
        // Get the uenvironment
        restUenvironmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUenvironment() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        int databaseSizeBeforeUpdate = uenvironmentRepository.findAll().size();

        // Update the uenvironment
        Uenvironment updatedUenvironment = uenvironmentRepository.findById(uenvironment.getId()).get();
        // Disconnect from session so that the updates on updatedUenvironment are not directly saved in db
        em.detach(updatedUenvironment);
        updatedUenvironment.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        UenvironmentDTO uenvironmentDTO = uenvironmentMapper.toDto(updatedUenvironment);

        restUenvironmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uenvironmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uenvironmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Uenvironment in the database
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeUpdate);
        Uenvironment testUenvironment = uenvironmentList.get(uenvironmentList.size() - 1);
        assertThat(testUenvironment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUenvironment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingUenvironment() throws Exception {
        int databaseSizeBeforeUpdate = uenvironmentRepository.findAll().size();
        uenvironment.setId(count.incrementAndGet());

        // Create the Uenvironment
        UenvironmentDTO uenvironmentDTO = uenvironmentMapper.toDto(uenvironment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUenvironmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uenvironmentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uenvironmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uenvironment in the database
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUenvironment() throws Exception {
        int databaseSizeBeforeUpdate = uenvironmentRepository.findAll().size();
        uenvironment.setId(count.incrementAndGet());

        // Create the Uenvironment
        UenvironmentDTO uenvironmentDTO = uenvironmentMapper.toDto(uenvironment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUenvironmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uenvironmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uenvironment in the database
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUenvironment() throws Exception {
        int databaseSizeBeforeUpdate = uenvironmentRepository.findAll().size();
        uenvironment.setId(count.incrementAndGet());

        // Create the Uenvironment
        UenvironmentDTO uenvironmentDTO = uenvironmentMapper.toDto(uenvironment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUenvironmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uenvironmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Uenvironment in the database
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUenvironmentWithPatch() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        int databaseSizeBeforeUpdate = uenvironmentRepository.findAll().size();

        // Update the uenvironment using partial update
        Uenvironment partialUpdatedUenvironment = new Uenvironment();
        partialUpdatedUenvironment.setId(uenvironment.getId());

        restUenvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUenvironment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUenvironment))
            )
            .andExpect(status().isOk());

        // Validate the Uenvironment in the database
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeUpdate);
        Uenvironment testUenvironment = uenvironmentList.get(uenvironmentList.size() - 1);
        assertThat(testUenvironment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUenvironment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateUenvironmentWithPatch() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        int databaseSizeBeforeUpdate = uenvironmentRepository.findAll().size();

        // Update the uenvironment using partial update
        Uenvironment partialUpdatedUenvironment = new Uenvironment();
        partialUpdatedUenvironment.setId(uenvironment.getId());

        partialUpdatedUenvironment.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restUenvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUenvironment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUenvironment))
            )
            .andExpect(status().isOk());

        // Validate the Uenvironment in the database
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeUpdate);
        Uenvironment testUenvironment = uenvironmentList.get(uenvironmentList.size() - 1);
        assertThat(testUenvironment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUenvironment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingUenvironment() throws Exception {
        int databaseSizeBeforeUpdate = uenvironmentRepository.findAll().size();
        uenvironment.setId(count.incrementAndGet());

        // Create the Uenvironment
        UenvironmentDTO uenvironmentDTO = uenvironmentMapper.toDto(uenvironment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUenvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uenvironmentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uenvironmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uenvironment in the database
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUenvironment() throws Exception {
        int databaseSizeBeforeUpdate = uenvironmentRepository.findAll().size();
        uenvironment.setId(count.incrementAndGet());

        // Create the Uenvironment
        UenvironmentDTO uenvironmentDTO = uenvironmentMapper.toDto(uenvironment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUenvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uenvironmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Uenvironment in the database
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUenvironment() throws Exception {
        int databaseSizeBeforeUpdate = uenvironmentRepository.findAll().size();
        uenvironment.setId(count.incrementAndGet());

        // Create the Uenvironment
        UenvironmentDTO uenvironmentDTO = uenvironmentMapper.toDto(uenvironment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUenvironmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uenvironmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Uenvironment in the database
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUenvironment() throws Exception {
        // Initialize the database
        uenvironmentRepository.saveAndFlush(uenvironment);

        int databaseSizeBeforeDelete = uenvironmentRepository.findAll().size();

        // Delete the uenvironment
        restUenvironmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, uenvironment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Uenvironment> uenvironmentList = uenvironmentRepository.findAll();
        assertThat(uenvironmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
