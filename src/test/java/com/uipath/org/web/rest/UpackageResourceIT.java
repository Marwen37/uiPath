package com.uipath.org.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uipath.org.IntegrationTest;
import com.uipath.org.domain.Upackage;
import com.uipath.org.domain.Uprocess;
import com.uipath.org.repository.UpackageRepository;
import com.uipath.org.service.criteria.UpackageCriteria;
import com.uipath.org.service.dto.UpackageDTO;
import com.uipath.org.service.mapper.UpackageMapper;
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
 * Integration tests for the {@link UpackageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UpackageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/upackages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UpackageRepository upackageRepository;

    @Autowired
    private UpackageMapper upackageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUpackageMockMvc;

    private Upackage upackage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upackage createEntity(EntityManager em) {
        Upackage upackage = new Upackage().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return upackage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Upackage createUpdatedEntity(EntityManager em) {
        Upackage upackage = new Upackage().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return upackage;
    }

    @BeforeEach
    public void initTest() {
        upackage = createEntity(em);
    }

    @Test
    @Transactional
    void createUpackage() throws Exception {
        int databaseSizeBeforeCreate = upackageRepository.findAll().size();
        // Create the Upackage
        UpackageDTO upackageDTO = upackageMapper.toDto(upackage);
        restUpackageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(upackageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Upackage in the database
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeCreate + 1);
        Upackage testUpackage = upackageList.get(upackageList.size() - 1);
        assertThat(testUpackage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUpackage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createUpackageWithExistingId() throws Exception {
        // Create the Upackage with an existing ID
        upackage.setId(1L);
        UpackageDTO upackageDTO = upackageMapper.toDto(upackage);

        int databaseSizeBeforeCreate = upackageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUpackageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(upackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upackage in the database
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUpackages() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList
        restUpackageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(upackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getUpackage() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get the upackage
        restUpackageMockMvc
            .perform(get(ENTITY_API_URL_ID, upackage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(upackage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getUpackagesByIdFiltering() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        Long id = upackage.getId();

        defaultUpackageShouldBeFound("id.equals=" + id);
        defaultUpackageShouldNotBeFound("id.notEquals=" + id);

        defaultUpackageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUpackageShouldNotBeFound("id.greaterThan=" + id);

        defaultUpackageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUpackageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUpackagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where name equals to DEFAULT_NAME
        defaultUpackageShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the upackageList where name equals to UPDATED_NAME
        defaultUpackageShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUpackagesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where name not equals to DEFAULT_NAME
        defaultUpackageShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the upackageList where name not equals to UPDATED_NAME
        defaultUpackageShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUpackagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUpackageShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the upackageList where name equals to UPDATED_NAME
        defaultUpackageShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUpackagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where name is not null
        defaultUpackageShouldBeFound("name.specified=true");

        // Get all the upackageList where name is null
        defaultUpackageShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllUpackagesByNameContainsSomething() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where name contains DEFAULT_NAME
        defaultUpackageShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the upackageList where name contains UPDATED_NAME
        defaultUpackageShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUpackagesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where name does not contain DEFAULT_NAME
        defaultUpackageShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the upackageList where name does not contain UPDATED_NAME
        defaultUpackageShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUpackagesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where description equals to DEFAULT_DESCRIPTION
        defaultUpackageShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the upackageList where description equals to UPDATED_DESCRIPTION
        defaultUpackageShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUpackagesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where description not equals to DEFAULT_DESCRIPTION
        defaultUpackageShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the upackageList where description not equals to UPDATED_DESCRIPTION
        defaultUpackageShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUpackagesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultUpackageShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the upackageList where description equals to UPDATED_DESCRIPTION
        defaultUpackageShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUpackagesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where description is not null
        defaultUpackageShouldBeFound("description.specified=true");

        // Get all the upackageList where description is null
        defaultUpackageShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllUpackagesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where description contains DEFAULT_DESCRIPTION
        defaultUpackageShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the upackageList where description contains UPDATED_DESCRIPTION
        defaultUpackageShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUpackagesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        // Get all the upackageList where description does not contain DEFAULT_DESCRIPTION
        defaultUpackageShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the upackageList where description does not contain UPDATED_DESCRIPTION
        defaultUpackageShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUpackagesByUprocessIsEqualToSomething() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);
        Uprocess uprocess = UprocessResourceIT.createEntity(em);
        em.persist(uprocess);
        em.flush();
        upackage.setUprocess(uprocess);
        upackageRepository.saveAndFlush(upackage);
        Long uprocessId = uprocess.getId();

        // Get all the upackageList where uprocess equals to uprocessId
        defaultUpackageShouldBeFound("uprocessId.equals=" + uprocessId);

        // Get all the upackageList where uprocess equals to (uprocessId + 1)
        defaultUpackageShouldNotBeFound("uprocessId.equals=" + (uprocessId + 1));
    }

    @Test
    @Transactional
    void getAllUpackagesByUprocessIsEqualToSomething() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);
        Uprocess uprocess = UprocessResourceIT.createEntity(em);
        em.persist(uprocess);
        em.flush();
        upackage.setUprocess(uprocess);
        upackageRepository.saveAndFlush(upackage);
        Long uprocessId = uprocess.getId();

        // Get all the upackageList where uprocess equals to uprocessId
        defaultUpackageShouldBeFound("uprocessId.equals=" + uprocessId);

        // Get all the upackageList where uprocess equals to (uprocessId + 1)
        defaultUpackageShouldNotBeFound("uprocessId.equals=" + (uprocessId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUpackageShouldBeFound(String filter) throws Exception {
        restUpackageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(upackage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restUpackageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUpackageShouldNotBeFound(String filter) throws Exception {
        restUpackageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUpackageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUpackage() throws Exception {
        // Get the upackage
        restUpackageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUpackage() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        int databaseSizeBeforeUpdate = upackageRepository.findAll().size();

        // Update the upackage
        Upackage updatedUpackage = upackageRepository.findById(upackage.getId()).get();
        // Disconnect from session so that the updates on updatedUpackage are not directly saved in db
        em.detach(updatedUpackage);
        updatedUpackage.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        UpackageDTO upackageDTO = upackageMapper.toDto(updatedUpackage);

        restUpackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, upackageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(upackageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Upackage in the database
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeUpdate);
        Upackage testUpackage = upackageList.get(upackageList.size() - 1);
        assertThat(testUpackage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUpackage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingUpackage() throws Exception {
        int databaseSizeBeforeUpdate = upackageRepository.findAll().size();
        upackage.setId(count.incrementAndGet());

        // Create the Upackage
        UpackageDTO upackageDTO = upackageMapper.toDto(upackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUpackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, upackageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(upackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upackage in the database
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUpackage() throws Exception {
        int databaseSizeBeforeUpdate = upackageRepository.findAll().size();
        upackage.setId(count.incrementAndGet());

        // Create the Upackage
        UpackageDTO upackageDTO = upackageMapper.toDto(upackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpackageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(upackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upackage in the database
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUpackage() throws Exception {
        int databaseSizeBeforeUpdate = upackageRepository.findAll().size();
        upackage.setId(count.incrementAndGet());

        // Create the Upackage
        UpackageDTO upackageDTO = upackageMapper.toDto(upackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpackageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(upackageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Upackage in the database
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUpackageWithPatch() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        int databaseSizeBeforeUpdate = upackageRepository.findAll().size();

        // Update the upackage using partial update
        Upackage partialUpdatedUpackage = new Upackage();
        partialUpdatedUpackage.setId(upackage.getId());

        partialUpdatedUpackage.name(UPDATED_NAME);

        restUpackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUpackage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUpackage))
            )
            .andExpect(status().isOk());

        // Validate the Upackage in the database
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeUpdate);
        Upackage testUpackage = upackageList.get(upackageList.size() - 1);
        assertThat(testUpackage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUpackage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateUpackageWithPatch() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        int databaseSizeBeforeUpdate = upackageRepository.findAll().size();

        // Update the upackage using partial update
        Upackage partialUpdatedUpackage = new Upackage();
        partialUpdatedUpackage.setId(upackage.getId());

        partialUpdatedUpackage.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restUpackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUpackage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUpackage))
            )
            .andExpect(status().isOk());

        // Validate the Upackage in the database
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeUpdate);
        Upackage testUpackage = upackageList.get(upackageList.size() - 1);
        assertThat(testUpackage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUpackage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingUpackage() throws Exception {
        int databaseSizeBeforeUpdate = upackageRepository.findAll().size();
        upackage.setId(count.incrementAndGet());

        // Create the Upackage
        UpackageDTO upackageDTO = upackageMapper.toDto(upackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUpackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, upackageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(upackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upackage in the database
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUpackage() throws Exception {
        int databaseSizeBeforeUpdate = upackageRepository.findAll().size();
        upackage.setId(count.incrementAndGet());

        // Create the Upackage
        UpackageDTO upackageDTO = upackageMapper.toDto(upackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpackageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(upackageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Upackage in the database
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUpackage() throws Exception {
        int databaseSizeBeforeUpdate = upackageRepository.findAll().size();
        upackage.setId(count.incrementAndGet());

        // Create the Upackage
        UpackageDTO upackageDTO = upackageMapper.toDto(upackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUpackageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(upackageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Upackage in the database
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUpackage() throws Exception {
        // Initialize the database
        upackageRepository.saveAndFlush(upackage);

        int databaseSizeBeforeDelete = upackageRepository.findAll().size();

        // Delete the upackage
        restUpackageMockMvc
            .perform(delete(ENTITY_API_URL_ID, upackage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Upackage> upackageList = upackageRepository.findAll();
        assertThat(upackageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
