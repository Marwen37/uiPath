package com.uipath.org.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.uipath.org.IntegrationTest;
import com.uipath.org.domain.Environment;
import com.uipath.org.domain.Machine;
import com.uipath.org.domain.Robot;
import com.uipath.org.domain.enumeration.RobotType;
import com.uipath.org.repository.RobotRepository;
import com.uipath.org.service.criteria.RobotCriteria;
import com.uipath.org.service.dto.RobotDTO;
import com.uipath.org.service.mapper.RobotMapper;
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
 * Integration tests for the {@link RobotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RobotResourceIT {

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

    private static final String ENTITY_API_URL = "/api/robots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private RobotMapper robotMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRobotMockMvc;

    private Robot robot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Robot createEntity(EntityManager em) {
        Robot robot = new Robot()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .domainUsername(DEFAULT_DOMAIN_USERNAME)
            .password(DEFAULT_PASSWORD);
        return robot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Robot createUpdatedEntity(EntityManager em) {
        Robot robot = new Robot()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .domainUsername(UPDATED_DOMAIN_USERNAME)
            .password(UPDATED_PASSWORD);
        return robot;
    }

    @BeforeEach
    public void initTest() {
        robot = createEntity(em);
    }

    @Test
    @Transactional
    void createRobot() throws Exception {
        int databaseSizeBeforeCreate = robotRepository.findAll().size();
        // Create the Robot
        RobotDTO robotDTO = robotMapper.toDto(robot);
        restRobotMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(robotDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Robot in the database
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeCreate + 1);
        Robot testRobot = robotList.get(robotList.size() - 1);
        assertThat(testRobot.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRobot.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRobot.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testRobot.getDomainUsername()).isEqualTo(DEFAULT_DOMAIN_USERNAME);
        assertThat(testRobot.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createRobotWithExistingId() throws Exception {
        // Create the Robot with an existing ID
        robot.setId(1L);
        RobotDTO robotDTO = robotMapper.toDto(robot);

        int databaseSizeBeforeCreate = robotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRobotMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(robotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Robot in the database
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRobots() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList
        restRobotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(robot.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].domainUsername").value(hasItem(DEFAULT_DOMAIN_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getRobot() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get the robot
        restRobotMockMvc
            .perform(get(ENTITY_API_URL_ID, robot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(robot.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.domainUsername").value(DEFAULT_DOMAIN_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getRobotsByIdFiltering() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        Long id = robot.getId();

        defaultRobotShouldBeFound("id.equals=" + id);
        defaultRobotShouldNotBeFound("id.notEquals=" + id);

        defaultRobotShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRobotShouldNotBeFound("id.greaterThan=" + id);

        defaultRobotShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRobotShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRobotsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where name equals to DEFAULT_NAME
        defaultRobotShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the robotList where name equals to UPDATED_NAME
        defaultRobotShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRobotsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where name not equals to DEFAULT_NAME
        defaultRobotShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the robotList where name not equals to UPDATED_NAME
        defaultRobotShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRobotsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRobotShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the robotList where name equals to UPDATED_NAME
        defaultRobotShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRobotsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where name is not null
        defaultRobotShouldBeFound("name.specified=true");

        // Get all the robotList where name is null
        defaultRobotShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRobotsByNameContainsSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where name contains DEFAULT_NAME
        defaultRobotShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the robotList where name contains UPDATED_NAME
        defaultRobotShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRobotsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where name does not contain DEFAULT_NAME
        defaultRobotShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the robotList where name does not contain UPDATED_NAME
        defaultRobotShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRobotsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where description equals to DEFAULT_DESCRIPTION
        defaultRobotShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the robotList where description equals to UPDATED_DESCRIPTION
        defaultRobotShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRobotsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where description not equals to DEFAULT_DESCRIPTION
        defaultRobotShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the robotList where description not equals to UPDATED_DESCRIPTION
        defaultRobotShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRobotsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRobotShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the robotList where description equals to UPDATED_DESCRIPTION
        defaultRobotShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRobotsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where description is not null
        defaultRobotShouldBeFound("description.specified=true");

        // Get all the robotList where description is null
        defaultRobotShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllRobotsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where description contains DEFAULT_DESCRIPTION
        defaultRobotShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the robotList where description contains UPDATED_DESCRIPTION
        defaultRobotShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRobotsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where description does not contain DEFAULT_DESCRIPTION
        defaultRobotShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the robotList where description does not contain UPDATED_DESCRIPTION
        defaultRobotShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRobotsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where type equals to DEFAULT_TYPE
        defaultRobotShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the robotList where type equals to UPDATED_TYPE
        defaultRobotShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRobotsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where type not equals to DEFAULT_TYPE
        defaultRobotShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the robotList where type not equals to UPDATED_TYPE
        defaultRobotShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRobotsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultRobotShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the robotList where type equals to UPDATED_TYPE
        defaultRobotShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRobotsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where type is not null
        defaultRobotShouldBeFound("type.specified=true");

        // Get all the robotList where type is null
        defaultRobotShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllRobotsByDomainUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where domainUsername equals to DEFAULT_DOMAIN_USERNAME
        defaultRobotShouldBeFound("domainUsername.equals=" + DEFAULT_DOMAIN_USERNAME);

        // Get all the robotList where domainUsername equals to UPDATED_DOMAIN_USERNAME
        defaultRobotShouldNotBeFound("domainUsername.equals=" + UPDATED_DOMAIN_USERNAME);
    }

    @Test
    @Transactional
    void getAllRobotsByDomainUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where domainUsername not equals to DEFAULT_DOMAIN_USERNAME
        defaultRobotShouldNotBeFound("domainUsername.notEquals=" + DEFAULT_DOMAIN_USERNAME);

        // Get all the robotList where domainUsername not equals to UPDATED_DOMAIN_USERNAME
        defaultRobotShouldBeFound("domainUsername.notEquals=" + UPDATED_DOMAIN_USERNAME);
    }

    @Test
    @Transactional
    void getAllRobotsByDomainUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where domainUsername in DEFAULT_DOMAIN_USERNAME or UPDATED_DOMAIN_USERNAME
        defaultRobotShouldBeFound("domainUsername.in=" + DEFAULT_DOMAIN_USERNAME + "," + UPDATED_DOMAIN_USERNAME);

        // Get all the robotList where domainUsername equals to UPDATED_DOMAIN_USERNAME
        defaultRobotShouldNotBeFound("domainUsername.in=" + UPDATED_DOMAIN_USERNAME);
    }

    @Test
    @Transactional
    void getAllRobotsByDomainUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where domainUsername is not null
        defaultRobotShouldBeFound("domainUsername.specified=true");

        // Get all the robotList where domainUsername is null
        defaultRobotShouldNotBeFound("domainUsername.specified=false");
    }

    @Test
    @Transactional
    void getAllRobotsByDomainUsernameContainsSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where domainUsername contains DEFAULT_DOMAIN_USERNAME
        defaultRobotShouldBeFound("domainUsername.contains=" + DEFAULT_DOMAIN_USERNAME);

        // Get all the robotList where domainUsername contains UPDATED_DOMAIN_USERNAME
        defaultRobotShouldNotBeFound("domainUsername.contains=" + UPDATED_DOMAIN_USERNAME);
    }

    @Test
    @Transactional
    void getAllRobotsByDomainUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where domainUsername does not contain DEFAULT_DOMAIN_USERNAME
        defaultRobotShouldNotBeFound("domainUsername.doesNotContain=" + DEFAULT_DOMAIN_USERNAME);

        // Get all the robotList where domainUsername does not contain UPDATED_DOMAIN_USERNAME
        defaultRobotShouldBeFound("domainUsername.doesNotContain=" + UPDATED_DOMAIN_USERNAME);
    }

    @Test
    @Transactional
    void getAllRobotsByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where password equals to DEFAULT_PASSWORD
        defaultRobotShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the robotList where password equals to UPDATED_PASSWORD
        defaultRobotShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllRobotsByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where password not equals to DEFAULT_PASSWORD
        defaultRobotShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the robotList where password not equals to UPDATED_PASSWORD
        defaultRobotShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllRobotsByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultRobotShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the robotList where password equals to UPDATED_PASSWORD
        defaultRobotShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllRobotsByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where password is not null
        defaultRobotShouldBeFound("password.specified=true");

        // Get all the robotList where password is null
        defaultRobotShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllRobotsByPasswordContainsSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where password contains DEFAULT_PASSWORD
        defaultRobotShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the robotList where password contains UPDATED_PASSWORD
        defaultRobotShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllRobotsByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        // Get all the robotList where password does not contain DEFAULT_PASSWORD
        defaultRobotShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the robotList where password does not contain UPDATED_PASSWORD
        defaultRobotShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllRobotsByMachineIsEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);
        Machine machine = MachineResourceIT.createEntity(em);
        em.persist(machine);
        em.flush();
        robot.addMachine(machine);
        robotRepository.saveAndFlush(robot);
        Long machineId = machine.getId();

        // Get all the robotList where machine equals to machineId
        defaultRobotShouldBeFound("machineId.equals=" + machineId);

        // Get all the robotList where machine equals to (machineId + 1)
        defaultRobotShouldNotBeFound("machineId.equals=" + (machineId + 1));
    }

    @Test
    @Transactional
    void getAllRobotsByEnvironmentIsEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);
        Environment environment = EnvironmentResourceIT.createEntity(em);
        em.persist(environment);
        em.flush();
        robot.setEnvironment(environment);
        robotRepository.saveAndFlush(robot);
        Long environmentId = environment.getId();

        // Get all the robotList where environment equals to environmentId
        defaultRobotShouldBeFound("environmentId.equals=" + environmentId);

        // Get all the robotList where environment equals to (environmentId + 1)
        defaultRobotShouldNotBeFound("environmentId.equals=" + (environmentId + 1));
    }

    @Test
    @Transactional
    void getAllRobotsByEnvironmentIsEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);
        Environment environment = EnvironmentResourceIT.createEntity(em);
        em.persist(environment);
        em.flush();
        robot.setEnvironment(environment);
        robotRepository.saveAndFlush(robot);
        Long environmentId = environment.getId();

        // Get all the robotList where environment equals to environmentId
        defaultRobotShouldBeFound("environmentId.equals=" + environmentId);

        // Get all the robotList where environment equals to (environmentId + 1)
        defaultRobotShouldNotBeFound("environmentId.equals=" + (environmentId + 1));
    }

    @Test
    @Transactional
    void getAllRobotsByMachineIsEqualToSomething() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);
        Machine machine = MachineResourceIT.createEntity(em);
        em.persist(machine);
        em.flush();
        robot.addMachine(machine);
        robotRepository.saveAndFlush(robot);
        Long machineId = machine.getId();

        // Get all the robotList where machine equals to machineId
        defaultRobotShouldBeFound("machineId.equals=" + machineId);

        // Get all the robotList where machine equals to (machineId + 1)
        defaultRobotShouldNotBeFound("machineId.equals=" + (machineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRobotShouldBeFound(String filter) throws Exception {
        restRobotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(robot.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].domainUsername").value(hasItem(DEFAULT_DOMAIN_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));

        // Check, that the count call also returns 1
        restRobotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRobotShouldNotBeFound(String filter) throws Exception {
        restRobotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRobotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRobot() throws Exception {
        // Get the robot
        restRobotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRobot() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        int databaseSizeBeforeUpdate = robotRepository.findAll().size();

        // Update the robot
        Robot updatedRobot = robotRepository.findById(robot.getId()).get();
        // Disconnect from session so that the updates on updatedRobot are not directly saved in db
        em.detach(updatedRobot);
        updatedRobot
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .domainUsername(UPDATED_DOMAIN_USERNAME)
            .password(UPDATED_PASSWORD);
        RobotDTO robotDTO = robotMapper.toDto(updatedRobot);

        restRobotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, robotDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(robotDTO))
            )
            .andExpect(status().isOk());

        // Validate the Robot in the database
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeUpdate);
        Robot testRobot = robotList.get(robotList.size() - 1);
        assertThat(testRobot.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRobot.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRobot.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRobot.getDomainUsername()).isEqualTo(UPDATED_DOMAIN_USERNAME);
        assertThat(testRobot.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingRobot() throws Exception {
        int databaseSizeBeforeUpdate = robotRepository.findAll().size();
        robot.setId(count.incrementAndGet());

        // Create the Robot
        RobotDTO robotDTO = robotMapper.toDto(robot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRobotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, robotDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(robotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Robot in the database
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRobot() throws Exception {
        int databaseSizeBeforeUpdate = robotRepository.findAll().size();
        robot.setId(count.incrementAndGet());

        // Create the Robot
        RobotDTO robotDTO = robotMapper.toDto(robot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRobotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(robotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Robot in the database
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRobot() throws Exception {
        int databaseSizeBeforeUpdate = robotRepository.findAll().size();
        robot.setId(count.incrementAndGet());

        // Create the Robot
        RobotDTO robotDTO = robotMapper.toDto(robot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRobotMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(robotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Robot in the database
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRobotWithPatch() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        int databaseSizeBeforeUpdate = robotRepository.findAll().size();

        // Update the robot using partial update
        Robot partialUpdatedRobot = new Robot();
        partialUpdatedRobot.setId(robot.getId());

        partialUpdatedRobot.description(UPDATED_DESCRIPTION).type(UPDATED_TYPE);

        restRobotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRobot.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRobot))
            )
            .andExpect(status().isOk());

        // Validate the Robot in the database
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeUpdate);
        Robot testRobot = robotList.get(robotList.size() - 1);
        assertThat(testRobot.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRobot.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRobot.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRobot.getDomainUsername()).isEqualTo(DEFAULT_DOMAIN_USERNAME);
        assertThat(testRobot.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateRobotWithPatch() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        int databaseSizeBeforeUpdate = robotRepository.findAll().size();

        // Update the robot using partial update
        Robot partialUpdatedRobot = new Robot();
        partialUpdatedRobot.setId(robot.getId());

        partialUpdatedRobot
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .domainUsername(UPDATED_DOMAIN_USERNAME)
            .password(UPDATED_PASSWORD);

        restRobotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRobot.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRobot))
            )
            .andExpect(status().isOk());

        // Validate the Robot in the database
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeUpdate);
        Robot testRobot = robotList.get(robotList.size() - 1);
        assertThat(testRobot.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRobot.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRobot.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testRobot.getDomainUsername()).isEqualTo(UPDATED_DOMAIN_USERNAME);
        assertThat(testRobot.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingRobot() throws Exception {
        int databaseSizeBeforeUpdate = robotRepository.findAll().size();
        robot.setId(count.incrementAndGet());

        // Create the Robot
        RobotDTO robotDTO = robotMapper.toDto(robot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRobotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, robotDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(robotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Robot in the database
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRobot() throws Exception {
        int databaseSizeBeforeUpdate = robotRepository.findAll().size();
        robot.setId(count.incrementAndGet());

        // Create the Robot
        RobotDTO robotDTO = robotMapper.toDto(robot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRobotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(robotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Robot in the database
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRobot() throws Exception {
        int databaseSizeBeforeUpdate = robotRepository.findAll().size();
        robot.setId(count.incrementAndGet());

        // Create the Robot
        RobotDTO robotDTO = robotMapper.toDto(robot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRobotMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(robotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Robot in the database
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRobot() throws Exception {
        // Initialize the database
        robotRepository.saveAndFlush(robot);

        int databaseSizeBeforeDelete = robotRepository.findAll().size();

        // Delete the robot
        restRobotMockMvc
            .perform(delete(ENTITY_API_URL_ID, robot.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Robot> robotList = robotRepository.findAll();
        assertThat(robotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
