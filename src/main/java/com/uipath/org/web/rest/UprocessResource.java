package com.uipath.org.web.rest;

import com.uipath.org.repository.UprocessRepository;
import com.uipath.org.service.UprocessQueryService;
import com.uipath.org.service.UprocessService;
import com.uipath.org.service.criteria.UprocessCriteria;
import com.uipath.org.service.dto.UprocessDTO;
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
 * REST controller for managing {@link com.uipath.org.domain.Uprocess}.
 */
@RestController
@RequestMapping("/api")
public class UprocessResource {

    private final Logger log = LoggerFactory.getLogger(UprocessResource.class);

    private static final String ENTITY_NAME = "uprocess";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UprocessService uprocessService;

    private final UprocessRepository uprocessRepository;

    private final UprocessQueryService uprocessQueryService;

    public UprocessResource(
        UprocessService uprocessService,
        UprocessRepository uprocessRepository,
        UprocessQueryService uprocessQueryService
    ) {
        this.uprocessService = uprocessService;
        this.uprocessRepository = uprocessRepository;
        this.uprocessQueryService = uprocessQueryService;
    }

    /**
     * {@code POST  /uprocesses} : Create a new uprocess.
     *
     * @param uprocessDTO the uprocessDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uprocessDTO, or with status {@code 400 (Bad Request)} if the uprocess has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/uprocesses")
    public ResponseEntity<UprocessDTO> createUprocess(@RequestBody UprocessDTO uprocessDTO) throws URISyntaxException {
        log.debug("REST request to save Uprocess : {}", uprocessDTO);
        if (uprocessDTO.getId() != null) {
            throw new BadRequestAlertException("A new uprocess cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UprocessDTO result = uprocessService.save(uprocessDTO);
        return ResponseEntity
            .created(new URI("/api/uprocesses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /uprocesses/:id} : Updates an existing uprocess.
     *
     * @param id the id of the uprocessDTO to save.
     * @param uprocessDTO the uprocessDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uprocessDTO,
     * or with status {@code 400 (Bad Request)} if the uprocessDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uprocessDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uprocesses/{id}")
    public ResponseEntity<UprocessDTO> updateUprocess(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UprocessDTO uprocessDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Uprocess : {}, {}", id, uprocessDTO);
        if (uprocessDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uprocessDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uprocessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UprocessDTO result = uprocessService.save(uprocessDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uprocessDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /uprocesses/:id} : Partial updates given fields of an existing uprocess, field will ignore if it is null
     *
     * @param id the id of the uprocessDTO to save.
     * @param uprocessDTO the uprocessDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uprocessDTO,
     * or with status {@code 400 (Bad Request)} if the uprocessDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uprocessDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uprocessDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/uprocesses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UprocessDTO> partialUpdateUprocess(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UprocessDTO uprocessDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Uprocess partially : {}, {}", id, uprocessDTO);
        if (uprocessDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uprocessDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uprocessRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UprocessDTO> result = uprocessService.partialUpdate(uprocessDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uprocessDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /uprocesses} : get all the uprocesses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uprocesses in body.
     */
    @GetMapping("/uprocesses")
    public ResponseEntity<List<UprocessDTO>> getAllUprocesses(UprocessCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Uprocesses by criteria: {}", criteria);
        Page<UprocessDTO> page = uprocessQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /uprocesses/count} : count all the uprocesses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/uprocesses/count")
    public ResponseEntity<Long> countUprocesses(UprocessCriteria criteria) {
        log.debug("REST request to count Uprocesses by criteria: {}", criteria);
        return ResponseEntity.ok().body(uprocessQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /uprocesses/:id} : get the "id" uprocess.
     *
     * @param id the id of the uprocessDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uprocessDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uprocesses/{id}")
    public ResponseEntity<UprocessDTO> getUprocess(@PathVariable Long id) {
        log.debug("REST request to get Uprocess : {}", id);
        Optional<UprocessDTO> uprocessDTO = uprocessService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uprocessDTO);
    }

    /**
     * {@code DELETE  /uprocesses/:id} : delete the "id" uprocess.
     *
     * @param id the id of the uprocessDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/uprocesses/{id}")
    public ResponseEntity<Void> deleteUprocess(@PathVariable Long id) {
        log.debug("REST request to delete Uprocess : {}", id);
        uprocessService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
