package com.uipath.org.web.rest;

import com.uipath.org.repository.RobotRepository;
import com.uipath.org.service.RobotQueryService;
import com.uipath.org.service.RobotService;
import com.uipath.org.service.criteria.RobotCriteria;
import com.uipath.org.service.dto.RobotDTO;
import com.uipath.org.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.uipath.org.domain.Robot}.
 */
@RestController
@RequestMapping("/api")
public class RobotResource {

    private final Logger log = LoggerFactory.getLogger(RobotResource.class);

    private static final String ENTITY_NAME = "robot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RobotService robotService;

    private final RobotRepository robotRepository;

    private final RobotQueryService robotQueryService;

    public RobotResource(RobotService robotService, RobotRepository robotRepository, RobotQueryService robotQueryService) {
        this.robotService = robotService;
        this.robotRepository = robotRepository;
        this.robotQueryService = robotQueryService;
    }

    /**
     * {@code POST  /robots} : Create a new robot.
     *
     * @param robotDTO the robotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new robotDTO, or with status {@code 400 (Bad Request)} if the robot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/robots")
    public ResponseEntity<RobotDTO> createRobot(@RequestBody RobotDTO robotDTO) throws URISyntaxException {
        log.debug("REST request to save Robot : {}", robotDTO);
        if (robotDTO.getId() != null) {
            throw new BadRequestAlertException("A new robot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RobotDTO result = robotService.save(robotDTO);
        return ResponseEntity
            .created(new URI("/api/robots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /robots/:id} : Updates an existing robot.
     *
     * @param id the id of the robotDTO to save.
     * @param robotDTO the robotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated robotDTO,
     * or with status {@code 400 (Bad Request)} if the robotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the robotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/robots/{id}")
    public ResponseEntity<RobotDTO> updateRobot(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RobotDTO robotDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Robot : {}, {}", id, robotDTO);
        if (robotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, robotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!robotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RobotDTO result = robotService.save(robotDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, robotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /robots/:id} : Partial updates given fields of an existing robot, field will ignore if it is null
     *
     * @param id the id of the robotDTO to save.
     * @param robotDTO the robotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated robotDTO,
     * or with status {@code 400 (Bad Request)} if the robotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the robotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the robotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/robots/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<RobotDTO> partialUpdateRobot(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RobotDTO robotDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Robot partially : {}, {}", id, robotDTO);
        if (robotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, robotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!robotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RobotDTO> result = robotService.partialUpdate(robotDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, robotDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /robots} : get all the robots.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of robots in body.
     */
    @GetMapping("/robots")
    public ResponseEntity<List<RobotDTO>> getAllRobots(RobotCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Robots by criteria: {}", criteria);
        Page<RobotDTO> page = robotQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /robots/count} : count all the robots.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/robots/count")
    public ResponseEntity<Long> countRobots(RobotCriteria criteria) {
        log.debug("REST request to count Robots by criteria: {}", criteria);
        return ResponseEntity.ok().body(robotQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /robots/:id} : get the "id" robot.
     *
     * @param id the id of the robotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the robotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/robots/{id}")
    public ResponseEntity<RobotDTO> getRobot(@PathVariable Long id) {
        log.debug("REST request to get Robot : {}", id);
        Optional<RobotDTO> robotDTO = robotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(robotDTO);
    }

    /**
     * {@code DELETE  /robots/:id} : delete the "id" robot.
     *
     * @param id the id of the robotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/robots/{id}")
    public ResponseEntity<Void> deleteRobot(@PathVariable Long id) {
        log.debug("REST request to delete Robot : {}", id);
        robotService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
