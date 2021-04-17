package com.uipath.org.web.rest;

import com.uipath.org.repository.UrobotRepository;
import com.uipath.org.service.UrobotQueryService;
import com.uipath.org.service.UrobotService;
import com.uipath.org.service.criteria.UrobotCriteria;
import com.uipath.org.service.dto.UrobotDTO;
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
 * REST controller for managing {@link com.uipath.org.domain.Urobot}.
 */
@RestController
@RequestMapping("/api")
public class UrobotResource {

    private final Logger log = LoggerFactory.getLogger(UrobotResource.class);

    private static final String ENTITY_NAME = "urobot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UrobotService urobotService;

    private final UrobotRepository urobotRepository;

    private final UrobotQueryService urobotQueryService;

    public UrobotResource(UrobotService urobotService, UrobotRepository urobotRepository, UrobotQueryService urobotQueryService) {
        this.urobotService = urobotService;
        this.urobotRepository = urobotRepository;
        this.urobotQueryService = urobotQueryService;
    }

    /**
     * {@code POST  /urobots} : Create a new urobot.
     *
     * @param urobotDTO the urobotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new urobotDTO, or with status {@code 400 (Bad Request)} if the urobot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/urobots")
    public ResponseEntity<UrobotDTO> createUrobot(@RequestBody UrobotDTO urobotDTO) throws URISyntaxException {
        log.debug("REST request to save Urobot : {}", urobotDTO);
        if (urobotDTO.getId() != null) {
            throw new BadRequestAlertException("A new urobot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UrobotDTO result = urobotService.save(urobotDTO);
        return ResponseEntity
            .created(new URI("/api/urobots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /urobots/:id} : Updates an existing urobot.
     *
     * @param id the id of the urobotDTO to save.
     * @param urobotDTO the urobotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated urobotDTO,
     * or with status {@code 400 (Bad Request)} if the urobotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the urobotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/urobots/{id}")
    public ResponseEntity<UrobotDTO> updateUrobot(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UrobotDTO urobotDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Urobot : {}, {}", id, urobotDTO);
        if (urobotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, urobotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!urobotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UrobotDTO result = urobotService.save(urobotDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, urobotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /urobots/:id} : Partial updates given fields of an existing urobot, field will ignore if it is null
     *
     * @param id the id of the urobotDTO to save.
     * @param urobotDTO the urobotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated urobotDTO,
     * or with status {@code 400 (Bad Request)} if the urobotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the urobotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the urobotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/urobots/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UrobotDTO> partialUpdateUrobot(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UrobotDTO urobotDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Urobot partially : {}, {}", id, urobotDTO);
        if (urobotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, urobotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!urobotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UrobotDTO> result = urobotService.partialUpdate(urobotDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, urobotDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /urobots} : get all the urobots.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of urobots in body.
     */
    @GetMapping("/urobots")
    public ResponseEntity<List<UrobotDTO>> getAllUrobots(UrobotCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Urobots by criteria: {}", criteria);
        Page<UrobotDTO> page = urobotQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /urobots/count} : count all the urobots.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/urobots/count")
    public ResponseEntity<Long> countUrobots(UrobotCriteria criteria) {
        log.debug("REST request to count Urobots by criteria: {}", criteria);
        return ResponseEntity.ok().body(urobotQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /urobots/:id} : get the "id" urobot.
     *
     * @param id the id of the urobotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the urobotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/urobots/{id}")
    public ResponseEntity<UrobotDTO> getUrobot(@PathVariable Long id) {
        log.debug("REST request to get Urobot : {}", id);
        Optional<UrobotDTO> urobotDTO = urobotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(urobotDTO);
    }

    /**
     * {@code DELETE  /urobots/:id} : delete the "id" urobot.
     *
     * @param id the id of the urobotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/urobots/{id}")
    public ResponseEntity<Void> deleteUrobot(@PathVariable Long id) {
        log.debug("REST request to delete Urobot : {}", id);
        urobotService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
