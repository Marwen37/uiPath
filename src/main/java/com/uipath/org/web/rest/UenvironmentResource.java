package com.uipath.org.web.rest;

import com.uipath.org.repository.UenvironmentRepository;
import com.uipath.org.service.UenvironmentQueryService;
import com.uipath.org.service.UenvironmentService;
import com.uipath.org.service.criteria.UenvironmentCriteria;
import com.uipath.org.service.dto.UenvironmentDTO;
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
 * REST controller for managing {@link com.uipath.org.domain.Uenvironment}.
 */
@RestController
@RequestMapping("/api")
public class UenvironmentResource {

    private final Logger log = LoggerFactory.getLogger(UenvironmentResource.class);

    private static final String ENTITY_NAME = "uenvironment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UenvironmentService uenvironmentService;

    private final UenvironmentRepository uenvironmentRepository;

    private final UenvironmentQueryService uenvironmentQueryService;

    public UenvironmentResource(
        UenvironmentService uenvironmentService,
        UenvironmentRepository uenvironmentRepository,
        UenvironmentQueryService uenvironmentQueryService
    ) {
        this.uenvironmentService = uenvironmentService;
        this.uenvironmentRepository = uenvironmentRepository;
        this.uenvironmentQueryService = uenvironmentQueryService;
    }

    /**
     * {@code POST  /uenvironments} : Create a new uenvironment.
     *
     * @param uenvironmentDTO the uenvironmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uenvironmentDTO, or with status {@code 400 (Bad Request)} if the uenvironment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/uenvironments")
    public ResponseEntity<UenvironmentDTO> createUenvironment(@RequestBody UenvironmentDTO uenvironmentDTO) throws URISyntaxException {
        log.debug("REST request to save Uenvironment : {}", uenvironmentDTO);
        if (uenvironmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new uenvironment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UenvironmentDTO result = uenvironmentService.save(uenvironmentDTO);
        return ResponseEntity
            .created(new URI("/api/uenvironments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /uenvironments/:id} : Updates an existing uenvironment.
     *
     * @param id the id of the uenvironmentDTO to save.
     * @param uenvironmentDTO the uenvironmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uenvironmentDTO,
     * or with status {@code 400 (Bad Request)} if the uenvironmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uenvironmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uenvironments/{id}")
    public ResponseEntity<UenvironmentDTO> updateUenvironment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UenvironmentDTO uenvironmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Uenvironment : {}, {}", id, uenvironmentDTO);
        if (uenvironmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uenvironmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uenvironmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UenvironmentDTO result = uenvironmentService.save(uenvironmentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uenvironmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /uenvironments/:id} : Partial updates given fields of an existing uenvironment, field will ignore if it is null
     *
     * @param id the id of the uenvironmentDTO to save.
     * @param uenvironmentDTO the uenvironmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uenvironmentDTO,
     * or with status {@code 400 (Bad Request)} if the uenvironmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uenvironmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uenvironmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/uenvironments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UenvironmentDTO> partialUpdateUenvironment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UenvironmentDTO uenvironmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Uenvironment partially : {}, {}", id, uenvironmentDTO);
        if (uenvironmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uenvironmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uenvironmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UenvironmentDTO> result = uenvironmentService.partialUpdate(uenvironmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uenvironmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /uenvironments} : get all the uenvironments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uenvironments in body.
     */
    @GetMapping("/uenvironments")
    public ResponseEntity<List<UenvironmentDTO>> getAllUenvironments(UenvironmentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Uenvironments by criteria: {}", criteria);
        Page<UenvironmentDTO> page = uenvironmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /uenvironments/count} : count all the uenvironments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/uenvironments/count")
    public ResponseEntity<Long> countUenvironments(UenvironmentCriteria criteria) {
        log.debug("REST request to count Uenvironments by criteria: {}", criteria);
        return ResponseEntity.ok().body(uenvironmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /uenvironments/:id} : get the "id" uenvironment.
     *
     * @param id the id of the uenvironmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uenvironmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uenvironments/{id}")
    public ResponseEntity<UenvironmentDTO> getUenvironment(@PathVariable Long id) {
        log.debug("REST request to get Uenvironment : {}", id);
        Optional<UenvironmentDTO> uenvironmentDTO = uenvironmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uenvironmentDTO);
    }

    /**
     * {@code DELETE  /uenvironments/:id} : delete the "id" uenvironment.
     *
     * @param id the id of the uenvironmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/uenvironments/{id}")
    public ResponseEntity<Void> deleteUenvironment(@PathVariable Long id) {
        log.debug("REST request to delete Uenvironment : {}", id);
        uenvironmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
