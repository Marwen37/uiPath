package com.uipath.org.web.rest;

import com.uipath.org.repository.UjobRepository;
import com.uipath.org.service.UjobQueryService;
import com.uipath.org.service.UjobService;
import com.uipath.org.service.criteria.UjobCriteria;
import com.uipath.org.service.dto.UjobDTO;
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
 * REST controller for managing {@link com.uipath.org.domain.Ujob}.
 */
@RestController
@RequestMapping("/api")
public class UjobResource {

    private final Logger log = LoggerFactory.getLogger(UjobResource.class);

    private static final String ENTITY_NAME = "ujob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UjobService ujobService;

    private final UjobRepository ujobRepository;

    private final UjobQueryService ujobQueryService;

    public UjobResource(UjobService ujobService, UjobRepository ujobRepository, UjobQueryService ujobQueryService) {
        this.ujobService = ujobService;
        this.ujobRepository = ujobRepository;
        this.ujobQueryService = ujobQueryService;
    }

    /**
     * {@code POST  /ujobs} : Create a new ujob.
     *
     * @param ujobDTO the ujobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ujobDTO, or with status {@code 400 (Bad Request)} if the ujob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ujobs")
    public ResponseEntity<UjobDTO> createUjob(@RequestBody UjobDTO ujobDTO) throws URISyntaxException {
        log.debug("REST request to save Ujob : {}", ujobDTO);
        if (ujobDTO.getId() != null) {
            throw new BadRequestAlertException("A new ujob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UjobDTO result = ujobService.save(ujobDTO);
        return ResponseEntity
            .created(new URI("/api/ujobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ujobs/:id} : Updates an existing ujob.
     *
     * @param id the id of the ujobDTO to save.
     * @param ujobDTO the ujobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ujobDTO,
     * or with status {@code 400 (Bad Request)} if the ujobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ujobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ujobs/{id}")
    public ResponseEntity<UjobDTO> updateUjob(@PathVariable(value = "id", required = false) final Long id, @RequestBody UjobDTO ujobDTO)
        throws URISyntaxException {
        log.debug("REST request to update Ujob : {}, {}", id, ujobDTO);
        if (ujobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ujobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ujobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UjobDTO result = ujobService.save(ujobDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ujobDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ujobs/:id} : Partial updates given fields of an existing ujob, field will ignore if it is null
     *
     * @param id the id of the ujobDTO to save.
     * @param ujobDTO the ujobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ujobDTO,
     * or with status {@code 400 (Bad Request)} if the ujobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ujobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ujobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ujobs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UjobDTO> partialUpdateUjob(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UjobDTO ujobDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ujob partially : {}, {}", id, ujobDTO);
        if (ujobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ujobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ujobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UjobDTO> result = ujobService.partialUpdate(ujobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ujobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ujobs} : get all the ujobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ujobs in body.
     */
    @GetMapping("/ujobs")
    public ResponseEntity<List<UjobDTO>> getAllUjobs(UjobCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ujobs by criteria: {}", criteria);
        Page<UjobDTO> page = ujobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ujobs/count} : count all the ujobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ujobs/count")
    public ResponseEntity<Long> countUjobs(UjobCriteria criteria) {
        log.debug("REST request to count Ujobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(ujobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ujobs/:id} : get the "id" ujob.
     *
     * @param id the id of the ujobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ujobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ujobs/{id}")
    public ResponseEntity<UjobDTO> getUjob(@PathVariable Long id) {
        log.debug("REST request to get Ujob : {}", id);
        Optional<UjobDTO> ujobDTO = ujobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ujobDTO);
    }

    /**
     * {@code DELETE  /ujobs/:id} : delete the "id" ujob.
     *
     * @param id the id of the ujobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ujobs/{id}")
    public ResponseEntity<Void> deleteUjob(@PathVariable Long id) {
        log.debug("REST request to delete Ujob : {}", id);
        ujobService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
