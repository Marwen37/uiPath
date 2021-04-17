package com.uipath.org.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uipath.org.IntegrationTest;
import com.uipath.org.domain.Machine;
import com.uipath.org.domain.Uenvironment;
import com.uipath.org.domain.Urobot;
import com.uipath.org.domain.enumeration.RobotType;
import com.uipath.org.repository.UrobotRepository;
import com.uipath.org.service.criteria.UrobotCriteria;
import com.uipath.org.service.dto.UrobotDTO;
import com.uipath.org.service.mapper.UrobotMapper;
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
 * Integration tests for the {@link UrobotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UrobotResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final RobotType DEFAULT_TYPE = RobotType.Studio;
    private static final RobotType UPDATED_TYPE = RobotType.Unattended;

    private static final String DEFAULT_DOMAIN_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/urobots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UrobotRepository urobotRepository;

    @Autowired
    private UrobotMapper urobotMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUrobotMockMvc;

    private Urobot urobot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Urobot createEntity(EntityManager em) {
        Urobot urobot = new Urobot()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .domainUsername(DEFAULT_DOMAIN_USERNAME)
            .password(DEFAULT_PASSWORD);
        return urobot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Urobot createUpdatedEntity(EntityManager em) {
        Urobot urobot = new Urobot()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .domainUsername(UPDATED_DOMAIN_USERNAME)
            .password(UPDATED_PASSWORD);
        return urobot;
    }

    @BeforeEach
    public void initTest() {
        urobot = createEntity(em);
    }

    @Test
    @Transactional
    void createUrobot() throws Exception {
        int databaseSizeBeforeCreate = urobotRepository.findAll().size();
        // Create the Urobot
        UrobotDTO urobotDTO = urobotMapper.toDto(urobot);
        restUrobotMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urobotDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Urobot in the database
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeCreate + 1);
        Urobot testUrobot = urobotList.get(urobotList.size() - 1);
        assertThat(testUrobot.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUrobot.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUrobot.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testUrobot.getDomainUsername()).isEqualTo(DEFAULT_DOMAIN_USERNAME);
        assertThat(testUrobot.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createUrobotWithExistingId() throws Exception {
        // Create the Urobot with an existing ID
        urobot.setId(1L);
        UrobotDTO urobotDTO = urobotMapper.toDto(urobot);

        int databaseSizeBeforeCreate = urobotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUrobotMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urobotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urobot in the database
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUrobots() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList
        restUrobotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(urobot.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].domainUsername").value(hasItem(DEFAULT_DOMAIN_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getUrobot() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get the urobot
        restUrobotMockMvc
            .perform(get(ENTITY_API_URL_ID, urobot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(urobot.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.domainUsername").value(DEFAULT_DOMAIN_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getUrobotsByIdFiltering() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        Long id = urobot.getId();

        defaultUrobotShouldBeFound("id.equals=" + id);
        defaultUrobotShouldNotBeFound("id.notEquals=" + id);

        defaultUrobotShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUrobotShouldNotBeFound("id.greaterThan=" + id);

        defaultUrobotShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUrobotShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUrobotsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where name equals to DEFAULT_NAME
        defaultUrobotShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the urobotList where name equals to UPDATED_NAME
        defaultUrobotShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUrobotsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where name not equals to DEFAULT_NAME
        defaultUrobotShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the urobotList where name not equals to UPDATED_NAME
        defaultUrobotShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUrobotsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where name in DEFAULT_NAME or UPDATED_NAME
        defaultUrobotShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the urobotList where name equals to UPDATED_NAME
        defaultUrobotShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUrobotsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where name is not null
        defaultUrobotShouldBeFound("name.specified=true");

        // Get all the urobotList where name is null
        defaultUrobotShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllUrobotsByNameContainsSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where name contains DEFAULT_NAME
        defaultUrobotShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the urobotList where name contains UPDATED_NAME
        defaultUrobotShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUrobotsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where name does not contain DEFAULT_NAME
        defaultUrobotShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the urobotList where name does not contain UPDATED_NAME
        defaultUrobotShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllUrobotsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where description equals to DEFAULT_DESCRIPTION
        defaultUrobotShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the urobotList where description equals to UPDATED_DESCRIPTION
        defaultUrobotShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUrobotsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where description not equals to DEFAULT_DESCRIPTION
        defaultUrobotShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the urobotList where description not equals to UPDATED_DESCRIPTION
        defaultUrobotShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUrobotsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultUrobotShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the urobotList where description equals to UPDATED_DESCRIPTION
        defaultUrobotShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUrobotsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where description is not null
        defaultUrobotShouldBeFound("description.specified=true");

        // Get all the urobotList where description is null
        defaultUrobotShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllUrobotsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where description contains DEFAULT_DESCRIPTION
        defaultUrobotShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the urobotList where description contains UPDATED_DESCRIPTION
        defaultUrobotShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUrobotsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where description does not contain DEFAULT_DESCRIPTION
        defaultUrobotShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the urobotList where description does not contain UPDATED_DESCRIPTION
        defaultUrobotShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllUrobotsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where type equals to DEFAULT_TYPE
        defaultUrobotShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the urobotList where type equals to UPDATED_TYPE
        defaultUrobotShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUrobotsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where type not equals to DEFAULT_TYPE
        defaultUrobotShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the urobotList where type not equals to UPDATED_TYPE
        defaultUrobotShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUrobotsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultUrobotShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the urobotList where type equals to UPDATED_TYPE
        defaultUrobotShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllUrobotsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where type is not null
        defaultUrobotShouldBeFound("type.specified=true");

        // Get all the urobotList where type is null
        defaultUrobotShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllUrobotsByDomainUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where domainUsername equals to DEFAULT_DOMAIN_USERNAME
        defaultUrobotShouldBeFound("domainUsername.equals=" + DEFAULT_DOMAIN_USERNAME);

        // Get all the urobotList where domainUsername equals to UPDATED_DOMAIN_USERNAME
        defaultUrobotShouldNotBeFound("domainUsername.equals=" + UPDATED_DOMAIN_USERNAME);
    }

    @Test
    @Transactional
    void getAllUrobotsByDomainUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where domainUsername not equals to DEFAULT_DOMAIN_USERNAME
        defaultUrobotShouldNotBeFound("domainUsername.notEquals=" + DEFAULT_DOMAIN_USERNAME);

        // Get all the urobotList where domainUsername not equals to UPDATED_DOMAIN_USERNAME
        defaultUrobotShouldBeFound("domainUsername.notEquals=" + UPDATED_DOMAIN_USERNAME);
    }

    @Test
    @Transactional
    void getAllUrobotsByDomainUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where domainUsername in DEFAULT_DOMAIN_USERNAME or UPDATED_DOMAIN_USERNAME
        defaultUrobotShouldBeFound("domainUsername.in=" + DEFAULT_DOMAIN_USERNAME + "," + UPDATED_DOMAIN_USERNAME);

        // Get all the urobotList where domainUsername equals to UPDATED_DOMAIN_USERNAME
        defaultUrobotShouldNotBeFound("domainUsername.in=" + UPDATED_DOMAIN_USERNAME);
    }

    @Test
    @Transactional
    void getAllUrobotsByDomainUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where domainUsername is not null
        defaultUrobotShouldBeFound("domainUsername.specified=true");

        // Get all the urobotList where domainUsername is null
        defaultUrobotShouldNotBeFound("domainUsername.specified=false");
    }

    @Test
    @Transactional
    void getAllUrobotsByDomainUsernameContainsSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where domainUsername contains DEFAULT_DOMAIN_USERNAME
        defaultUrobotShouldBeFound("domainUsername.contains=" + DEFAULT_DOMAIN_USERNAME);

        // Get all the urobotList where domainUsername contains UPDATED_DOMAIN_USERNAME
        defaultUrobotShouldNotBeFound("domainUsername.contains=" + UPDATED_DOMAIN_USERNAME);
    }

    @Test
    @Transactional
    void getAllUrobotsByDomainUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where domainUsername does not contain DEFAULT_DOMAIN_USERNAME
        defaultUrobotShouldNotBeFound("domainUsername.doesNotContain=" + DEFAULT_DOMAIN_USERNAME);

        // Get all the urobotList where domainUsername does not contain UPDATED_DOMAIN_USERNAME
        defaultUrobotShouldBeFound("domainUsername.doesNotContain=" + UPDATED_DOMAIN_USERNAME);
    }

    @Test
    @Transactional
    void getAllUrobotsByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where password equals to DEFAULT_PASSWORD
        defaultUrobotShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the urobotList where password equals to UPDATED_PASSWORD
        defaultUrobotShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUrobotsByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where password not equals to DEFAULT_PASSWORD
        defaultUrobotShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the urobotList where password not equals to UPDATED_PASSWORD
        defaultUrobotShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUrobotsByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultUrobotShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the urobotList where password equals to UPDATED_PASSWORD
        defaultUrobotShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUrobotsByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where password is not null
        defaultUrobotShouldBeFound("password.specified=true");

        // Get all the urobotList where password is null
        defaultUrobotShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllUrobotsByPasswordContainsSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where password contains DEFAULT_PASSWORD
        defaultUrobotShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the urobotList where password contains UPDATED_PASSWORD
        defaultUrobotShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUrobotsByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        // Get all the urobotList where password does not contain DEFAULT_PASSWORD
        defaultUrobotShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the urobotList where password does not contain UPDATED_PASSWORD
        defaultUrobotShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllUrobotsByMachineIsEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);
        Machine machine = MachineResourceIT.createEntity(em);
        em.persist(machine);
        em.flush();
        urobot.addMachine(machine);
        urobotRepository.saveAndFlush(urobot);
        Long machineId = machine.getId();

        // Get all the urobotList where machine equals to machineId
        defaultUrobotShouldBeFound("machineId.equals=" + machineId);

        // Get all the urobotList where machine equals to (machineId + 1)
        defaultUrobotShouldNotBeFound("machineId.equals=" + (machineId + 1));
    }

    @Test
    @Transactional
    void getAllUrobotsByUenvironmentIsEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);
        Uenvironment uenvironment = UenvironmentResourceIT.createEntity(em);
        em.persist(uenvironment);
        em.flush();
        urobot.setUenvironment(uenvironment);
        urobotRepository.saveAndFlush(urobot);
        Long uenvironmentId = uenvironment.getId();

        // Get all the urobotList where uenvironment equals to uenvironmentId
        defaultUrobotShouldBeFound("uenvironmentId.equals=" + uenvironmentId);

        // Get all the urobotList where uenvironment equals to (uenvironmentId + 1)
        defaultUrobotShouldNotBeFound("uenvironmentId.equals=" + (uenvironmentId + 1));
    }

    @Test
    @Transactional
    void getAllUrobotsByUenvironmentIsEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);
        Uenvironment uenvironment = UenvironmentResourceIT.createEntity(em);
        em.persist(uenvironment);
        em.flush();
        urobot.setUenvironment(uenvironment);
        urobotRepository.saveAndFlush(urobot);
        Long uenvironmentId = uenvironment.getId();

        // Get all the urobotList where uenvironment equals to uenvironmentId
        defaultUrobotShouldBeFound("uenvironmentId.equals=" + uenvironmentId);

        // Get all the urobotList where uenvironment equals to (uenvironmentId + 1)
        defaultUrobotShouldNotBeFound("uenvironmentId.equals=" + (uenvironmentId + 1));
    }

    @Test
    @Transactional
    void getAllUrobotsByMachineIsEqualToSomething() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);
        Machine machine = MachineResourceIT.createEntity(em);
        em.persist(machine);
        em.flush();
        urobot.addMachine(machine);
        urobotRepository.saveAndFlush(urobot);
        Long machineId = machine.getId();

        // Get all the urobotList where machine equals to machineId
        defaultUrobotShouldBeFound("machineId.equals=" + machineId);

        // Get all the urobotList where machine equals to (machineId + 1)
        defaultUrobotShouldNotBeFound("machineId.equals=" + (machineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUrobotShouldBeFound(String filter) throws Exception {
        restUrobotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(urobot.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].domainUsername").value(hasItem(DEFAULT_DOMAIN_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));

        // Check, that the count call also returns 1
        restUrobotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUrobotShouldNotBeFound(String filter) throws Exception {
        restUrobotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUrobotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUrobot() throws Exception {
        // Get the urobot
        restUrobotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUrobot() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        int databaseSizeBeforeUpdate = urobotRepository.findAll().size();

        // Update the urobot
        Urobot updatedUrobot = urobotRepository.findById(urobot.getId()).get();
        // Disconnect from session so that the updates on updatedUrobot are not directly saved in db
        em.detach(updatedUrobot);
        updatedUrobot
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .domainUsername(UPDATED_DOMAIN_USERNAME)
            .password(UPDATED_PASSWORD);
        UrobotDTO urobotDTO = urobotMapper.toDto(updatedUrobot);

        restUrobotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, urobotDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urobotDTO))
            )
            .andExpect(status().isOk());

        // Validate the Urobot in the database
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeUpdate);
        Urobot testUrobot = urobotList.get(urobotList.size() - 1);
        assertThat(testUrobot.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUrobot.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUrobot.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUrobot.getDomainUsername()).isEqualTo(UPDATED_DOMAIN_USERNAME);
        assertThat(testUrobot.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingUrobot() throws Exception {
        int databaseSizeBeforeUpdate = urobotRepository.findAll().size();
        urobot.setId(count.incrementAndGet());

        // Create the Urobot
        UrobotDTO urobotDTO = urobotMapper.toDto(urobot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUrobotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, urobotDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urobotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urobot in the database
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUrobot() throws Exception {
        int databaseSizeBeforeUpdate = urobotRepository.findAll().size();
        urobot.setId(count.incrementAndGet());

        // Create the Urobot
        UrobotDTO urobotDTO = urobotMapper.toDto(urobot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrobotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urobotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urobot in the database
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUrobot() throws Exception {
        int databaseSizeBeforeUpdate = urobotRepository.findAll().size();
        urobot.setId(count.incrementAndGet());

        // Create the Urobot
        UrobotDTO urobotDTO = urobotMapper.toDto(urobot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrobotMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(urobotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Urobot in the database
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUrobotWithPatch() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        int databaseSizeBeforeUpdate = urobotRepository.findAll().size();

        // Update the urobot using partial update
        Urobot partialUpdatedUrobot = new Urobot();
        partialUpdatedUrobot.setId(urobot.getId());

        partialUpdatedUrobot.type(UPDATED_TYPE).domainUsername(UPDATED_DOMAIN_USERNAME).password(UPDATED_PASSWORD);

        restUrobotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUrobot.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUrobot))
            )
            .andExpect(status().isOk());

        // Validate the Urobot in the database
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeUpdate);
        Urobot testUrobot = urobotList.get(urobotList.size() - 1);
        assertThat(testUrobot.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUrobot.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUrobot.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUrobot.getDomainUsername()).isEqualTo(UPDATED_DOMAIN_USERNAME);
        assertThat(testUrobot.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateUrobotWithPatch() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        int databaseSizeBeforeUpdate = urobotRepository.findAll().size();

        // Update the urobot using partial update
        Urobot partialUpdatedUrobot = new Urobot();
        partialUpdatedUrobot.setId(urobot.getId());

        partialUpdatedUrobot
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .domainUsername(UPDATED_DOMAIN_USERNAME)
            .password(UPDATED_PASSWORD);

        restUrobotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUrobot.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUrobot))
            )
            .andExpect(status().isOk());

        // Validate the Urobot in the database
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeUpdate);
        Urobot testUrobot = urobotList.get(urobotList.size() - 1);
        assertThat(testUrobot.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUrobot.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUrobot.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testUrobot.getDomainUsername()).isEqualTo(UPDATED_DOMAIN_USERNAME);
        assertThat(testUrobot.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingUrobot() throws Exception {
        int databaseSizeBeforeUpdate = urobotRepository.findAll().size();
        urobot.setId(count.incrementAndGet());

        // Create the Urobot
        UrobotDTO urobotDTO = urobotMapper.toDto(urobot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUrobotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, urobotDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(urobotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urobot in the database
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUrobot() throws Exception {
        int databaseSizeBeforeUpdate = urobotRepository.findAll().size();
        urobot.setId(count.incrementAndGet());

        // Create the Urobot
        UrobotDTO urobotDTO = urobotMapper.toDto(urobot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrobotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(urobotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Urobot in the database
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUrobot() throws Exception {
        int databaseSizeBeforeUpdate = urobotRepository.findAll().size();
        urobot.setId(count.incrementAndGet());

        // Create the Urobot
        UrobotDTO urobotDTO = urobotMapper.toDto(urobot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUrobotMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(urobotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Urobot in the database
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUrobot() throws Exception {
        // Initialize the database
        urobotRepository.saveAndFlush(urobot);

        int databaseSizeBeforeDelete = urobotRepository.findAll().size();

        // Delete the urobot
        restUrobotMockMvc
            .perform(delete(ENTITY_API_URL_ID, urobot.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Urobot> urobotList = urobotRepository.findAll();
        assertThat(urobotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
