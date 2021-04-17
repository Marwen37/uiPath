package com.uipath.org.web.rest;

import com.uipath.org.repository.UpackageRepository;
import com.uipath.org.service.UpackageQueryService;
import com.uipath.org.service.UpackageService;
import com.uipath.org.service.criteria.UpackageCriteria;
import com.uipath.org.service.dto.UpackageDTO;
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
 * REST controller for managing {@link com.uipath.org.domain.Upackage}.
 */
@RestController
@RequestMapping("/api")
public class UpackageResource {

    private final Logger log = LoggerFactory.getLogger(UpackageResource.class);

    private static final String ENTITY_NAME = "upackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UpackageService upackageService;

    private final UpackageRepository upackageRepository;

    private final UpackageQueryService upackageQueryService;

    public UpackageResource(
        UpackageService upackageService,
        UpackageRepository upackageRepository,
        UpackageQueryService upackageQueryService
    ) {
        this.upackageService = upackageService;
        this.upackageRepository = upackageRepository;
        this.upackageQueryService = upackageQueryService;
    }

    /**
     * {@code POST  /upackages} : Create a new upackage.
     *
     * @param upackageDTO the upackageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new upackageDTO, or with status {@code 400 (Bad Request)} if the upackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/upackages")
    public ResponseEntity<UpackageDTO> createUpackage(@RequestBody UpackageDTO upackageDTO) throws URISyntaxException {
        log.debug("REST request to save Upackage : {}", upackageDTO);
        if (upackageDTO.getId() != null) {
            throw new BadRequestAlertException("A new upackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UpackageDTO result = upackageService.save(upackageDTO);
        return ResponseEntity
            .created(new URI("/api/upackages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /upackages/:id} : Updates an existing upackage.
     *
     * @param id the id of the upackageDTO to save.
     * @param upackageDTO the upackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated upackageDTO,
     * or with status {@code 400 (Bad Request)} if the upackageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the upackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/upackages/{id}")
    public ResponseEntity<UpackageDTO> updateUpackage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UpackageDTO upackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Upackage : {}, {}", id, upackageDTO);
        if (upackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, upackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!upackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UpackageDTO result = upackageService.save(upackageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upackageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /upackages/:id} : Partial updates given fields of an existing upackage, field will ignore if it is null
     *
     * @param id the id of the upackageDTO to save.
     * @param upackageDTO the upackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated upackageDTO,
     * or with status {@code 400 (Bad Request)} if the upackageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the upackageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the upackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/upackages/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UpackageDTO> partialUpdateUpackage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UpackageDTO upackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Upackage partially : {}, {}", id, upackageDTO);
        if (upackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, upackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!upackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UpackageDTO> result = upackageService.partialUpdate(upackageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upackageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /upackages} : get all the upackages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of upackages in body.
     */
    @GetMapping("/upackages")
    public ResponseEntity<List<UpackageDTO>> getAllUpackages(UpackageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Upackages by criteria: {}", criteria);
        Page<UpackageDTO> page = upackageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /upackages/count} : count all the upackages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/upackages/count")
    public ResponseEntity<Long> countUpackages(UpackageCriteria criteria) {
        log.debug("REST request to count Upackages by criteria: {}", criteria);
        return ResponseEntity.ok().body(upackageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /upackages/:id} : get the "id" upackage.
     *
     * @param id the id of the upackageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the upackageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/upackages/{id}")
    public ResponseEntity<UpackageDTO> getUpackage(@PathVariable Long id) {
        log.debug("REST request to get Upackage : {}", id);
        Optional<UpackageDTO> upackageDTO = upackageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(upackageDTO);
    }

    /**
     * {@code DELETE  /upackages/:id} : delete the "id" upackage.
     *
     * @param id the id of the upackageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/upackages/{id}")
    public ResponseEntity<Void> deleteUpackage(@PathVariable Long id) {
        log.debug("REST request to delete Upackage : {}", id);
        upackageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
