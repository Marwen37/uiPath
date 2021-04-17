package com.uipath.org.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uipath.org.IntegrationTest;
import com.uipath.org.domain.Ujob;
import com.uipath.org.repository.UjobRepository;
import com.uipath.org.service.criteria.UjobCriteria;
import com.uipath.org.service.dto.UjobDTO;
import com.uipath.org.service.mapper.UjobMapper;
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
 * Integration tests for the {@link UjobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UjobResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ujobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UjobRepository ujobRepository;

    @Autowired
    private UjobMapper ujobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUjobMockMvc;

    private Ujob ujob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ujob createEntity(EntityManager em) {
        Ujob ujob = new Ujob().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return ujob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ujob createUpdatedEntity(EntityManager em) {
        Ujob ujob = new Ujob().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return ujob;
    }

    @BeforeEach
    public void initTest() {
        ujob = createEntity(em);
    }

    @Test
    @Transactional
    void createUjob() throws Exception {
        int databaseSizeBeforeCreate = ujobRepository.findAll().size();
        // Create the Ujob
        UjobDTO ujobDTO = ujobMapper.toDto(ujob);
        restUjobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ujobDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Ujob in the database
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeCreate + 1);
        Ujob testUjob = ujobList.get(ujobList.size() - 1);
        assertThat(testUjob.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUjob.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createUjobWithExistingId() throws Exception {
        // Create the Ujob with an existing ID
        ujob.setId(1L);
        UjobDTO ujobDTO = ujobMapper.toDto(ujob);

        int databaseSizeBeforeCreate = ujobRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUjobMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ujobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ujob in the database
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUjobs() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList
        restUjobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ujob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getUjob() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get the ujob
        restUjobMockMvc
            .perform(get(ENTITY_API_URL_ID, ujob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ujob.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getUjobsByIdFiltering() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        Long id = ujob.getId();

        defaultUjobShouldBeFound("id.equals=" + id);
        defaultUjobShouldNotBeFound("id.notEquals=" + id);

        defaultUjobShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUjobShouldNotBeFound("id.greaterThan=" + id);

        defaultUjobShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUjobShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUjobsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where name equals to DEFAULT_NAME
        defaultUjobShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ujobList where name equals to UPDATED_NAME
        defaultUjobShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUjobsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where name not equals to DEFAULT_NAME
        defaultUjobShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the ujobList where name not equals to UPDATED_NAME
        defaultUjobShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUjobsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUjobShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ujobList where name equals to UPDATED_NAME
        defaultUjobShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUjobsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where name is not null
        defaultUjobShouldBeFound("name.specified=true");

        // Get all the ujobList where name is null
        defaultUjobShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllUjobsByNameContainsSomething() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where name contains DEFAULT_NAME
        defaultUjobShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ujobList where name contains UPDATED_NAME
        defaultUjobShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUjobsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where name does not contain DEFAULT_NAME
        defaultUjobShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ujobList where name does not contain UPDATED_NAME
        defaultUjobShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUjobsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where description equals to DEFAULT_DESCRIPTION
        defaultUjobShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the ujobList where description equals to UPDATED_DESCRIPTION
        defaultUjobShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUjobsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where description not equals to DEFAULT_DESCRIPTION
        defaultUjobShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the ujobList where description not equals to UPDATED_DESCRIPTION
        defaultUjobShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUjobsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultUjobShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the ujobList where description equals to UPDATED_DESCRIPTION
        defaultUjobShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUjobsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where description is not null
        defaultUjobShouldBeFound("description.specified=true");

        // Get all the ujobList where description is null
        defaultUjobShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllUjobsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where description contains DEFAULT_DESCRIPTION
        defaultUjobShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the ujobList where description contains UPDATED_DESCRIPTION
        defaultUjobShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUjobsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        // Get all the ujobList where description does not contain DEFAULT_DESCRIPTION
        defaultUjobShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the ujobList where description does not contain UPDATED_DESCRIPTION
        defaultUjobShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUjobShouldBeFound(String filter) throws Exception {
        restUjobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ujob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restUjobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUjobShouldNotBeFound(String filter) throws Exception {
        restUjobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUjobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUjob() throws Exception {
        // Get the ujob
        restUjobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUjob() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        int databaseSizeBeforeUpdate = ujobRepository.findAll().size();

        // Update the ujob
        Ujob updatedUjob = ujobRepository.findById(ujob.getId()).get();
        // Disconnect from session so that the updates on updatedUjob are not directly saved in db
        em.detach(updatedUjob);
        updatedUjob.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        UjobDTO ujobDTO = ujobMapper.toDto(updatedUjob);

        restUjobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ujobDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ujobDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ujob in the database
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeUpdate);
        Ujob testUjob = ujobList.get(ujobList.size() - 1);
        assertThat(testUjob.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUjob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingUjob() throws Exception {
        int databaseSizeBeforeUpdate = ujobRepository.findAll().size();
        ujob.setId(count.incrementAndGet());

        // Create the Ujob
        UjobDTO ujobDTO = ujobMapper.toDto(ujob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUjobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ujobDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ujobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ujob in the database
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUjob() throws Exception {
        int databaseSizeBeforeUpdate = ujobRepository.findAll().size();
        ujob.setId(count.incrementAndGet());

        // Create the Ujob
        UjobDTO ujobDTO = ujobMapper.toDto(ujob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUjobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ujobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ujob in the database
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUjob() throws Exception {
        int databaseSizeBeforeUpdate = ujobRepository.findAll().size();
        ujob.setId(count.incrementAndGet());

        // Create the Ujob
        UjobDTO ujobDTO = ujobMapper.toDto(ujob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUjobMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ujobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ujob in the database
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUjobWithPatch() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        int databaseSizeBeforeUpdate = ujobRepository.findAll().size();

        // Update the ujob using partial update
        Ujob partialUpdatedUjob = new Ujob();
        partialUpdatedUjob.setId(ujob.getId());

        partialUpdatedUjob.description(UPDATED_DESCRIPTION);

        restUjobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUjob.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUjob))
            )
            .andExpect(status().isOk());

        // Validate the Ujob in the database
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeUpdate);
        Ujob testUjob = ujobList.get(ujobList.size() - 1);
        assertThat(testUjob.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUjob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateUjobWithPatch() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        int databaseSizeBeforeUpdate = ujobRepository.findAll().size();

        // Update the ujob using partial update
        Ujob partialUpdatedUjob = new Ujob();
        partialUpdatedUjob.setId(ujob.getId());

        partialUpdatedUjob.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restUjobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUjob.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUjob))
            )
            .andExpect(status().isOk());

        // Validate the Ujob in the database
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeUpdate);
        Ujob testUjob = ujobList.get(ujobList.size() - 1);
        assertThat(testUjob.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUjob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingUjob() throws Exception {
        int databaseSizeBeforeUpdate = ujobRepository.findAll().size();
        ujob.setId(count.incrementAndGet());

        // Create the Ujob
        UjobDTO ujobDTO = ujobMapper.toDto(ujob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUjobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ujobDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ujobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ujob in the database
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUjob() throws Exception {
        int databaseSizeBeforeUpdate = ujobRepository.findAll().size();
        ujob.setId(count.incrementAndGet());

        // Create the Ujob
        UjobDTO ujobDTO = ujobMapper.toDto(ujob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUjobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ujobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ujob in the database
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUjob() throws Exception {
        int databaseSizeBeforeUpdate = ujobRepository.findAll().size();
        ujob.setId(count.incrementAndGet());

        // Create the Ujob
        UjobDTO ujobDTO = ujobMapper.toDto(ujob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUjobMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ujobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ujob in the database
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUjob() throws Exception {
        // Initialize the database
        ujobRepository.saveAndFlush(ujob);

        int databaseSizeBeforeDelete = ujobRepository.findAll().size();

        // Delete the ujob
        restUjobMockMvc
            .perform(delete(ENTITY_API_URL_ID, ujob.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ujob> ujobList = ujobRepository.findAll();
        assertThat(ujobList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
